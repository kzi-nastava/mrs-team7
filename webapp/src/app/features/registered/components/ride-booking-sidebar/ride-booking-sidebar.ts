import { Component } from '@angular/core';
import { LocationSearchInput } from "../../../shared/components/location-search-input/location-search-input";
import { NominatimResult } from '../../../shared/services/nominatim.service';
// sidebar inputs connected with state service
import { RideBookingStateService } from '../../../../core/services/ride-booking-state.service';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'ride-booking-sidebar',
  imports: [LocationSearchInput, NgOptimizedImage],
  templateUrl: './ride-booking-sidebar.html',
})
export class RideBookingSidebar {
  constructor(public bookingState: RideBookingStateService) {}

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
  }

  removeStop(index: number) {
    this.bookingState.removeStop(index);
  }
}
