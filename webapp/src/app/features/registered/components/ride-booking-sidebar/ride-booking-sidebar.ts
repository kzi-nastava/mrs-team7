import { Component, inject, signal } from '@angular/core';
import { LocationSearchInput } from "../../../shared/components/location-search-input/location-search-input";
import { NominatimResult } from '../../../shared/services/nominatim.service';
import { RideBookingStateService } from '../../../../core/services/ride-booking-state.service';
import { NgOptimizedImage, Location, CommonModule } from '@angular/common';
import { UserService } from '../../../../core/services/user.service';
import { User } from '../../../../core/models/user';
import { FormsModule } from '@angular/forms';
import { VehicleType } from '../../../shared/models/vehicle';
import { RideOrderService } from '../../services/ride-order.service';
import { LocationDTO, nominatimToLocation } from '../../../shared/models/location';
import { SuccessAlert } from "../../../shared/components/success-alert";
import { ErrorAlert } from "../../../shared/components/error-alert";
import { RouterLink } from "@angular/router";

@Component({
  selector: 'ride-booking-sidebar',
  imports: [LocationSearchInput, NgOptimizedImage, CommonModule, FormsModule, SuccessAlert, ErrorAlert, RouterLink],
  templateUrl: './ride-booking-sidebar.html',
})
export class RideBookingSidebar {
  bookingState = inject(RideBookingStateService);
  location = inject(Location);
  userService = inject(UserService);
  rideOrderService = inject(RideOrderService);

  vehicleTypes: string[] = [];

  pets: boolean = false;
  infants: boolean = false;
  vehicleType: string = 'any';

  minDate!: string;
  maxDate!: string;
  scheduledDate = signal<string>('');

  passengerEmails = signal<string[]>([]);

  isSuccessOpen: boolean = false;
  successMessage: string = "Ride successfully booked!";

  isErrorOpen: boolean = false;
  errorTitle: string = "Error";
  errorMessage: string = "Error occured during ride booking.";

  step = signal<number>(1);
  user = signal<User>({
    id: '',
    firstName: '',
    lastName: '',
    email: '',
    address: '',
    phoneNumber: '',
    role: 'PASSENGER'
  });

  ngOnInit() {
    this.userService.currentUser$.subscribe(
      {
        next: user => (user !== null ? this.user.set(user) : '')
      }
    )
    this.vehicleTypes = Object.values(VehicleType);

    const now = new Date();
    this.scheduledDate.set(this.toDatetimeLocalValue(now));
    this.minDate = this.toDatetimeLocalValue(now);
    this.maxDate = this.toDatetimeLocalValue(new Date(now.getTime() + 5 * 60 * 60000));
  }

  onPickupSelected(res: NominatimResult) {
    this.bookingState.setPickup(res);
  }

  onDropoffSelected(res: NominatimResult) {
    this.bookingState.setDropoff(res);
  }

  addStop() {
    this.bookingState.addStop();
  }

  onStopSelected(index: number, res: NominatimResult) {
    this.bookingState.setStop(index, res);
    console.log(this.bookingState.stops());
    
  }

  removeStop(index: number) {
    this.bookingState.removeStop(index);
  }

  nextStep() {
    if (this.step() >= 3) return;
    this.step.set(this.step() + 1);
  }

  previousStep() {
    if (this.step() <= 1) {
      console.log(this.location.getState());
      
      this.location.back();
    }
    this.step.set(this.step() - 1);
  }

  // Dodaj errore
  bookRide() {
    const requestVehicleType: VehicleType | undefined =
      Object.values(VehicleType).includes(this.vehicleType as VehicleType)
      ? (this.vehicleType as VehicleType)
      : undefined;

    if(this.bookingState.pickup() == null) return;
    if(this.bookingState.dropoff() == null) return;

    let requestStops : LocationDTO[] = []

    this.bookingState.stops().forEach((res) => {
        if (res===null) {
          this.errorMessage="Ride stops cannot be empty.";
          this.isErrorOpen = true;
          return;
        }
        else {
          requestStops.push(nominatimToLocation(res))
        };
      }
    )

    if (this.passengerEmails().includes('')) {
      this.errorMessage="Passenger emails cannot be empty.";
      this.isErrorOpen = true;
      return;
    }

    this.rideOrderService.requestRide(
      nominatimToLocation(this.bookingState.pickup()!),
      nominatimToLocation(this.bookingState.dropoff()!),
      requestVehicleType,
      requestStops,
      this.infants, this.pets,
      this.passengerEmails(),
      new Date(this.scheduledDate()),
      this.bookingState.routeInfo()?.totalDistance!,
      undefined   
    ).subscribe({
      next: (ride => {
        this.successMessage = `Ride successfully assigned to driver: ${ride.driverEmail}!
        Car is expected to arrive at ${ride.scheduledTime}. Total price is: ${ride.totalPrice}.`;
        this.isSuccessOpen = true;
      }),
      error: (err => {
        this.errorMessage = err.error?.message || 'Booking failed. Please try again.';
        this.isErrorOpen = true;
      })
    });
  }

  private toDatetimeLocalValue(d: Date): string {
    const pad = (n: number) => n.toString().padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
  }

  isDateValid(date:string) : boolean {
    return date <= this.maxDate && date >= this.minDate;
  }

  addPassenger() {
    this.passengerEmails().push('');
  }

  removePassenger(index: number) {
    this.passengerEmails().splice(index, 1);
  }

  addEmail(e: FocusEvent, index: number) {
    const input: HTMLInputElement = e.target as HTMLInputElement;
    const value: string = input.value;

    this.passengerEmails()[index] = value
    console.log(this.passengerEmails())
  }

  closeErrorAlert(): void {
    this.isErrorOpen = false;
  }
}
