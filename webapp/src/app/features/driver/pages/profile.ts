import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProfileInfoCard } from '../../shared/components/profile-info-card';
import { User } from '../../shared/models/user';
import { Vehicle } from '../../shared/models/vehicle';

@Component({
  selector: 'driver-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, ProfileInfoCard],
  template: `
    <div class="min-h-screen bg-white">
      <div class="flex flex-col min-h-screen">
        <div class="flex flex-1">
          <!-- Main Content -->
          <main class="flex flex-1 p-8 justify-center items-center">
            <div class="flex flex-col gap-6 w-full">

            <!-- Work Time Progress Card -->
              <div class="border-[1.5px] border-gray-200 rounded-3xl shadow-lg p-8 flex flex-col gap-6">
                <h3 class="text-[22px] font-normal font-poppins text-black leading-8.25"> Driver Activity</h3>

                <div class="w-full flex items-center justify-between">
                  <div class="text-sm text-gray-600">
                    Time worked today
                  </div>

                  <div class="text-[22px] font-normal font-poppins text-black leading-8.25">
                    {{ timeWorkedHours }}h {{ timeWorkedMinutesRemainder }}m
                  </div>
                </div>

                <!-- Progress bar container -->
                <div class="w-full">
                  <div
                    aria-valuemin="0"
                    aria-valuemax="480"
                    [attr.aria-valuenow]="timeWorkedMinutes"
                    class="w-full h-3 bg-gray-200 rounded-full overflow-hidden">
                    <!-- filled bar -->
                    <div
                      [style.width.%]="progressPercent"
                      class="h-full rounded-full transition-all duration-500 bg-lime-400"
                    ></div>
                  </div>

                    <div class="flex justify-between text-xs text-gray-500 mt-2">
                    <span>0h</span>
                    <span>8h</span>
                  </div>

                </div>
              </div>
              <!-- Info Card -->
              <profile-info-card [user]="user"> </profile-info-card>

              <!-- Vehicle Info -->
              <div class="border-[1.5px] border-gray-200 rounded-3xl shadow-lg p-8 flex flex-col gap-11">
                <h3 class="text-[22px] font-normal font-poppins text-black leading-8.25">Vehicle Information</h3>

                <div class="flex flex-col gap-2.5">
                  <!-- Vehicle Model Type Row -->
                  <div class="flex gap-2.5 flex-col sm:flex-row">
                    <div class="flex-1 flex flex-col">
                      <label class="text-sm font-normal font-poppins flex items-center text-gray-700">
                        Vehicle Model</label>

                        <p class="text-[20px] font-normal font-poppins text-black leading-8.25">
                          {{ vehicleModel }}
                        </p>
                    </div>

                    <div class="flex-1 flex flex-col">
                      <label class="text-sm font-normal font-poppins flex items-center text-gray-700">
                        Vehicle Type</label>

                        <p class="text-[20px] font-normal font-poppins text-black leading-8.25">
                          {{ vehicle.type }}
                        </p>
                    </div>

                  </div>

                  <!-- License plates Seats Row -->
                  <div class="flex gap-2.5 flex-col sm:flex-row">
                    <div class="flex-1 flex flex-col">
                      <label class="text-sm font-normal font-poppins flex items-center text-gray-700">
                        License Plate</label>

                        <p class="text-[20px] font-normal font-poppins text-black leading-8.25">
                          {{ vehicle.licensePlate }}
                        </p>
                    </div>

                    <div class="flex-1 flex flex-col">
                      <label class="text-sm font-normal font-poppins flex items-center text-gray-700">
                        Seats</label>

                        <p class="text-[20px] font-normal font-poppins text-black leading-8.25">
                          {{ vehicle.seatCount }}
                        </p>
                    </div>

                  </div>

                  <!-- Infant Pet Support -->
                  <div class="flex gap-2.5 flex-col sm:flex-row">
                    <div class="flex-1 flex flex-col">
                      <label class="text-sm font-normal font-poppins flex items-center text-gray-700">
                        Infant Support</label>

                        <p class="text-[20px] font-normal font-poppins text-black leading-8.25">
                          {{ vehicle.babyFriendly ? 'True' : 'False' }}
                        </p>
                    </div>

                    <div class="flex-1 flex flex-col">
                      <label class="text-sm font-normal font-poppins flex items-center text-gray-700">
                        Pet Support</label>

                        <p class="text-[20px] font-normal font-poppins text-black leading-8.25">
                          {{ vehicle.petsFriendly ? 'True' : 'False' }}
                        </p>
                    </div>

                  </div>

                </div>
              </div>


            </div>
          </main>
        </div>

        <!-- Change Password Modal -->
      <div
        *ngIf="isChangePasswordOpen"
        class="fixed inset-0 z-50 flex items-center justify-center"
        (click)="closeChangePassword()"
        aria-modal="true"
        role="dialog"
      >
        <!-- Backdrop -->
        <div class="absolute inset-0 bg-black/50"></div>

        <!-- Modal Card -->
        <div
          class="relative bg-white rounded-2xl shadow-xl w-full max-w-md mx-4 p-6 z-10"
          (click)="$event.stopPropagation()"
        >
          <!-- Header -->
          <div class="flex items-center justify-between">
            <h4 class="text-lg font-medium">Change Password</h4>
            <button
              aria-label="Close"
              (click)="closeChangePassword()"
              class="text-gray-500 hover:text-gray-700 cursor-pointer"
              type="button"
            >
              &times;
            </button>
          </div>

          <!-- Form -->
          <form #pwForm="ngForm" (ngSubmit)="closeChangePassword()" class="mt-4 space-y-4">
            <div class="flex flex-col gap-2">
              <label class="text-sm text-gray-700">Old Password</label>
              <input
                type="password"
                name="oldPassword"
                required
                minlength="1"
                [(ngModel)]="oldPassword"
                class="input-base"
                placeholder="Enter old password"
                autocomplete="current-password"
              />
            </div>

            <div class="flex flex-col gap-2">
              <label class="text-sm text-gray-700">New Password</label>
              <input
                type="password"
                name="newPassword"
                required
                minlength="8"
                [(ngModel)]="newPassword"
                class="input-base"
                placeholder="Enter new password (min 8 chars)"
                autocomplete="new-password"
              />
            </div>

            <div class="flex flex-col gap-2">
              <label class="text-sm text-gray-700">Confirm Password</label>
              <input
                type="password"
                name="confirmPassword"
                required
                minlength="8"
                [(ngModel)]="confirmPassword"
                class="input-base"
                placeholder="Confirm new password"
                autocomplete="new-password"
              />
            </div>

            <div *ngIf="passwordError" class="text-sm text-red-600">{{ passwordError }}</div>

            <div class="flex gap-3 justify-end pt-2">
              <button
                type="button"
                class="h-10 px-4 rounded-full border border-gray-300 text-sm text-gray-700 hover:bg-gray-50 cursor-pointer"
                (click)="closeChangePassword()"
              >
                Cancel
              </button>

              <button
                type="submit"
                class="h-10 px-4 rounded-full bg-lime-400 text-sm text-neutral-900 hover:bg-lime-500 cursor-pointer"
                (click)="closeChangePassword()"
              >
                Confirm
              </button>
            </div>
          </form>
        </div>


      </div>
    </div>
  `,
})
export class DriverProfileComponent {
  vehicleModel: string = "Model"

  user: User = {
    id: '1',
    firstName: 'Andrew',
    lastName: 'Wilson',
    email: 'andrewwilson@email.com',
    address: 'Novi Sad',
    phoneNumber: '+381 65 123 1233'
  }

  vehicle: Vehicle = {
    id: 0,
    model: 'Model',
    type: 'Type',
    licensePlate: 'DSDSDS-111',
    seatCount: 10,
    babyFriendly: true,
    petsFriendly: false
  }

  isChangePasswordOpen: boolean = false;

  oldPassword: string = '';
  newPassword: string = '';
  confirmPassword: string = '';
  passwordError: string | null = null;

  timeWorkedMinutes: number = 150;

  // maximum minutes = 8 hours
  readonly MAX_WORK_MINUTES = 8 * 60;

  get progressPercent(): number {
    const pct = (this.timeWorkedMinutes / this.MAX_WORK_MINUTES) * 100;
    return Math.min(100, Math.max(0, Math.round(pct)));
  }

  get timeWorkedHours(): number {
    return Math.floor(Math.min(this.timeWorkedMinutes, this.MAX_WORK_MINUTES) / 60);
  }
  get timeWorkedMinutesRemainder(): number {
    return Math.floor(Math.min(this.timeWorkedMinutes, this.MAX_WORK_MINUTES) % 60);
  }

  openChangePassword(): void {
    this.isChangePasswordOpen = true;
    this.passwordError = null;
    this.clearPasswordFields();
  }

  closeChangePassword(): void {
    this.isChangePasswordOpen = false;
    this.clearPasswordFields();
    this.passwordError = null;
  }

  clearPasswordFields(): void {
    this.oldPassword = '';
    this.newPassword = '';
    this.confirmPassword = '';
  }
  
}
