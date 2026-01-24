import {ChangeDetectorRef, Component, inject, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {VehicleMarker} from '../../../shared/map/vehicle-marker';
import {VehiclesApiService} from '../../../shared/api/vehicles-api.service';
import {MapComponent} from '../../../shared/map/map';
import {CurrentRideStateService} from '../../services/current-ride-state.service';
import {UserService} from '../../../../core/services/user.service';
import {RideDTO, RideService} from '../../../../core/services/ride.service';
import {LatLng, RoutingService} from '../../../shared/services/routing.service';
import {resamplePolyline} from '../../../shared/map/route-utils';
import {LocationDTO} from '../../../shared/models/location';

type UiRideStatus = 'Assigned' | 'Started' | 'Finished' | 'Cancelled'
type PassengerItem = { id: number; name: string; email: string; role: 'You' | 'Passenger' };

@Component({
  selector: 'app-current-ride',
  standalone: true,
  imports: [CommonModule, FormsModule, MapComponent],
  templateUrl: './current-ride.html',
})


export class CurrentRideComponent implements OnInit, OnDestroy {
  private vehiclesApi = inject(VehiclesApiService);
  private cdr = inject(ChangeDetectorRef);
  private userService = inject(UserService);
  private rideService = inject(RideService);
  private routing = inject(RoutingService);
  public rideState = inject(CurrentRideStateService);

  ride: RideDTO | null = null;
  routeStart: { lat: number; lon: number } | null = null;
  routeEnd: { lat: number; lon: number } | null = null;
  waypoints: string[] = [];
  waypointLocations: LocationDTO[] = [];
  routePath: LatLng[] = [];
  routePoints: { lat: number; lon: number; label?: string }[] = [];

  private simStartTimeout?: number;
  private simMoveInterval?: number;
  private simAbort?: AbortController;

  vehicles: VehicleMarker[] = [];
  currentRideStatus: UiRideStatus = 'Started';
  fromAddress = '';
  toAddress = '';
  vehicleText = '';
  passengers: PassengerItem[] = [];
  etaMinutes = 1;
  reportNote = '';
  submittingReport = false;

  ngOnInit(): void {
    this.rideService.getMyInProgressRide().subscribe({
      next: (r) => {
        this.ride = r;
        this.currentRideStatus = 'Started';
        this.fromAddress = r.startLocation.address;
        this.toAddress = r.endLocation.address;
        this.routeStart = { lat: r.startLocation.latitude, lon: r.startLocation.longitude };
        this.routeEnd   = { lat: r.endLocation.latitude,   lon: r.endLocation.longitude };
        this.routePoints = [
          {lat: r.startLocation.latitude, lon: r.startLocation.longitude, label: 'Pickup'},
          ...(r.waypoints ?? []).map((w, i) => ({lat: w.latitude, lon: w.longitude, label: `Stop ${i + 1}`})),
          {lat: r.endLocation.latitude, lon: r.endLocation.longitude, label: 'Destination'},
        ];
        this.waypointLocations = (r.waypoints ?? []);
        this.waypoints = this.waypointLocations
          .map(w => w.address)
          .filter(Boolean);
        this.vehicleText =
          r.vehicleModel && r.vehicleLicensePlate
            ? `${r.vehicleModel} • ${r.vehicleLicensePlate}`
            : 'Vehicle';
        this.passengers = (r.passengers ?? []).map((p, idx: number) => ({
          id: idx + 1,
          name: `${p.firstName} ${p.lastName}`.trim(),
          email: p.email,
          role: 'Passenger' as const
        }));
        this.vehiclesApi.getDriverVehicleForMap(r.driverEmail).subscribe({
          next: (v) => {
            this.vehicles = [v];
            this.cdr.detectChanges();
            this.runSimulation(r);
          },
          error: (err) => console.error('Failed to load driver vehicle', err),
        });
      },
      error: () => {
        this.ride = null;
        this.vehicles = [];
        this.currentRideStatus = 'Cancelled';
        this.fromAddress = '';
        this.toAddress = '';
        this.routeStart = null;
        this.routeEnd = null;
        this.vehicleText = '';
      }
    });
  }

  private stopSimulation(): void {
    if (this.simStartTimeout) window.clearTimeout(this.simStartTimeout);
    if (this.simMoveInterval) window.clearInterval(this.simMoveInterval);
    this.simStartTimeout = undefined;
    this.simMoveInterval = undefined;

    this.simAbort?.abort();
    this.simAbort = undefined;
  }

  private sleep(ms: number): Promise<void> {
    return new Promise(res => setTimeout(res, ms));
  }

  private runSimulation(r: RideDTO) {
    this.stopSimulation();
    const stops: LatLng[] = [
      [r.startLocation.latitude, r.startLocation.longitude],
      ...(r.waypoints ?? []).map(w => [w.latitude, w.longitude] as LatLng),
      [r.endLocation.latitude, r.endLocation.longitude],
    ];
    if (!this.vehicles.length) return;

    this.vehicles = [{
      ...this.vehicles[0],
      lat: stops[0][0],
      lng: stops[0][1],
    }];
    this.cdr.detectChanges();

    const vehicleId = this.vehicles[0].id;
    this.vehiclesApi.updateVehiclePosition(vehicleId, {
      latitude: stops[0][0],
      longitude: stops[0][1],
    }).subscribe({ error: (e) => console.error('Position update failed', e) });

    this.simAbort?.abort();
    this.simAbort = new AbortController();
    const signal = this.simAbort.signal;

    this.simStartTimeout = window.setTimeout(async () => {
      try {
        for (let segIdx = 0; segIdx < stops.length - 1; segIdx++) {
          const from = stops[segIdx];
          const to = stops[segIdx + 1];
          const seg = await this.routing.fetchRoute(from, to, signal);
          const sampled = resamplePolyline(seg.coordinates, 100);
          const path: [number, number][] =
            sampled.length >= 2 ? sampled : ([from, to] as [number, number][]);

          for (let i = 0; i < path.length; i++) {
            if (!this.vehicles.length) return;

            const [lat, lng] = path[i];

            this.vehicles = [{
              ...this.vehicles[0],
              lat,
              lng,
            }];
            this.cdr.detectChanges();

            const vid = this.vehicles[0].id;
            this.vehiclesApi.updateVehiclePosition(vid, {
              latitude: lat,
              longitude: lng,
            }).subscribe({ error: (e) => console.error('Position update failed', e) });

            await this.sleep(1000);

            if (signal.aborted) return;
          }

          const isLastStop = segIdx === stops.length - 2;
          if (!isLastStop) {
            await this.sleep(3000);
            if (signal.aborted) return;
          }
        }

        const last = stops[stops.length - 1];
        this.vehicles = [{
          ...this.vehicles[0],
          lat: last[0],
          lng: last[1],
        }];
        this.cdr.detectChanges();

        this.vehiclesApi.updateVehiclePosition(vehicleId, {
          latitude: last[0],
          longitude: last[1],
        }).subscribe({ error: (e) => console.error('Final position update failed', e) });

        this.currentRideStatus = 'Finished';
        this.cdr.detectChanges();
        window.alert('Ride finished!');
      } catch (e: any) {
        if (e?.name === 'AbortError') return;
        console.error('Simulation failed', e);
      }
    }, 30_000);
  }
  onPanic(): void {
    if (this.rideState.panicSignal().pressed) return;
    const userId = this.userService.getCurrentUserId() ?? 0;
    const rideId = this.ride?.id;
    if(!rideId) return;
    this.rideState.setPanic(rideId, userId);
  }

  statusPillClasses: Record<UiRideStatus, string> = {
    Assigned: 'bg-blue-100 text-blue-700',
    Started: 'bg-green-100 text-green-700',
    Finished: 'bg-gray-100 text-slate-700',
    Cancelled: 'bg-red-100 text-red-700',
  };

  get isRideActive(): boolean {
    return this.currentRideStatus === 'Started' || this.currentRideStatus === 'Assigned';
  }

  get etaText(): string {
    if (!this.isRideActive) return '--';
    if (this.etaMinutes <= 1) return '< 1 min';
    return `${this.etaMinutes} min`;
  }

  submitReport(): void {
    return;
  }

  ngOnDestroy(): void {
    this.stopSimulation();
  }

  protected readonly CurrentRideStateService = CurrentRideStateService;
}
