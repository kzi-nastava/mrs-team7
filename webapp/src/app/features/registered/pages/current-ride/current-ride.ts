import {Component, inject, OnInit, ChangeDetectorRef, OnDestroy} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VehicleMarker } from '../../../shared/map/vehicle-marker';
import { VehiclesApiService } from '../../../shared/api/vehicles-api.service';
import { MapComponent } from '../../../shared/map/map';
import { CurrentRideStateService } from '../../services/current-ride-state.service';
import { UserService } from '../../../../core/services/user.service';
import { RideDTO, RideService } from '../../../../core/services/ride.service';
import { RoutingService, LatLng } from '../../../shared/services/routing.service';
import { resamplePolyline } from '../../../shared/map/route-utils';

type UiRideStatus = 'Assigned' | 'Started' | 'Finished' | 'Cancelled'
type PassengerItem = { id: number; name: string; role: 'You' | 'Passenger' };

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

  private ride: RideDTO | null = null;
  routeStart: { lat: number; lon: number } | null = null;
  routeEnd: { lat: number; lon: number } | null = null;

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
        this.vehicleText =
          r.vehicleModel && r.vehicleLicensePlate
            ? `${r.vehicleModel} • ${r.vehicleLicensePlate}`
            : 'Vehicle';
        this.passengers = (r.passengerEmails ?? []).map((email, idx) => ({
          id: idx + 1,
          name: email,
          role: 'Passenger'
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

  private runSimulation(r: RideDTO) {
    this.stopSimulation();

    this.simStartTimeout = window.setTimeout(async () => {
      if (!this.vehicles.length) return;

      // auto ide na start location
      this.vehicles = [{
        ...this.vehicles[0],
        lat: r.startLocation.latitude,
        lng: r.startLocation.longitude
      }];
      this.cdr.detectChanges();

      // fetch rute
      const from: LatLng = [r.startLocation.latitude, r.startLocation.longitude];
      const to: LatLng = [r.endLocation.latitude, r.endLocation.longitude];

      this.simAbort?.abort();
      this.simAbort = new AbortController();

      let seg;
      try {
        seg = await this.routing.fetchRoute(from, to, this.simAbort.signal);
      } catch (e: any) {
        if (e?.name === 'AbortError') return;
        console.error('Failed to fetch route', e);
        return;
      }

      // resample na sto metara
      const sampled = resamplePolyline(seg.coordinates, 100);

      // ako nema dovoljno tacaka skoci na kraj
      if (sampled.length < 2) {
        this.vehicles = [{
          ...this.vehicles[0],
          lat: r.endLocation.latitude,
          lng: r.endLocation.longitude
        }];
        this.cdr.detectChanges();
        window.alert('Ride finished!');
        this.currentRideStatus = 'Finished';
        this.cdr.detectChanges();
        return;
      }

      // kretanje po tackama (1s jedna tacka)
      let i = 0;

      this.simMoveInterval = window.setInterval(() => {
        if (!this.vehicles.length) return;

        if (i >= sampled.length) {
          this.stopSimulation();
          window.alert('Ride finished!');
          this.currentRideStatus = 'Finished';
          this.cdr.detectChanges();
          return;
        }

        const [lat, lng] = sampled[i++];

        this.vehicles = [{
          ...this.vehicles[0],
          lat,
          lng
        }];
        this.cdr.detectChanges();

        // ovo dolje cuva u bazu
        const vehicleId = this.vehicles[0].id;

        this.vehiclesApi.updateVehiclePosition(vehicleId, {
          latitude: lat,
          longitude: lng
        }).subscribe();

      }, 1000);

    }, 10_000);
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
}
