import { Component, EventEmitter, Input, Output, SimpleChange, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { User } from '../models/user';
import { log } from 'console';

@Component({
  selector: 'profile-info-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
      <div class="flex flex-col">
        <div class="flex flex-col gap-6 w-full">
              <!-- Profile Header Card -->
              <div class="border-[1.5px] border-gray-200 rounded-3xl shadow-lg p-8">
                <div class="flex items-center gap-6">
                  <!-- Profile Picture -->
                  <div class="relative group w-30 h-30 cursor-pointer">
                    <div class="w-full h-full rounded-full border-[3px] border-app-accent p-0.5">
                      <img
                        class="w-full h-full rounded-full object-cover group-hover:brightness-80"
                        src="defaultprofile.png"
                        alt="Profile"
                      />
                    </div>
                    <button class="absolute bottom-0 right-0 w-9 h-9 bg-app-accent rounded-full shadow-lg flex items-center justify-center">
                      <img src="camera.svg" alt="Camera">
                    </button>
                  </div>

                  <!-- User Info -->
                  <div class="flex flex-col gap-1">
                    <h2 class="text-[28px] font-normal font-poppins text-black leading-10.5">{{user.firstName}} {{user.lastName}}</h2>
                    <p class="text-base font-normal font-poppins text-gray-600">{{user.email}}</p>
                  </div>
                </div>
              </div>

              <!-- Info Card -->
              <div class="border-[1.5px] border-gray-200 rounded-3xl shadow-lg p-8 flex flex-col gap-11">
                <h3 class="text-[22px] font-normal font-poppins text-black leading-8.25">Personal Information</h3>

                <!-- Form Fields -->
                <div class="flex flex-col gap-2.5">
                  <!-- First Name & Last Name Row -->
                  <div class="flex gap-2.5 flex-col sm:flex-row">
                    <div class="flex-1 flex flex-col gap-2">
                      <label class="text-sm font-normal font-poppins flex items-center text-gray-700">
                        <img class="w-6 h-6" src="profile.svg" alt="Phone" />
                        First Name</label>
                      <input
                        type="text"
                        [(ngModel)]="editableUser.firstName"
                        class="input-base"
                        placeholder="First name"
                      />
                    </div>
                    <div class="flex-1 flex flex-col gap-2">
                      <label class="text-sm font-normal font-poppins flex items-center text-gray-700">
                        <img class="w-6 h-6" src="profile.svg" alt="Phone" />
                        Last Name</label>
                      <input
                        type="text"
                        [(ngModel)]="editableUser.lastName"
                        class="input-base"
                        placeholder="Last name"
                      />
                    </div>
                  </div>

                  <!-- Email Field -->
                  <div class="flex flex-col gap-2">
                    <label class="text-sm font-normal font-poppins text-gray-700 flex items-center gap-2">
                      <img class="w-6 h-6" src="mail.svg" alt="Mail"/>
                      Email
                    </label>
                    <input
                      type="email"
                      [(ngModel)]="editableUser.email"
                      class="input-base"
                      placeholder="E-Mail"
                    />
                  </div>

                  <!-- Address Field -->
                  <div class="flex flex-col gap-2">
                    <label class="text-sm font-normal font-poppins text-gray-700 flex items-center gap-2">
                      <img class="w-6 h-6" src="location.svg" alt="address"/>
                      Address
                    </label>
                    <input
                      type="text"
                      [(ngModel)]="editableUser.address"
                      class="input-base"
                      placeholder="Address"
                    />
                  </div>

                  <!-- Phone Number Field -->
                  <div class="flex flex-col gap-2">
                    <label class="text-sm font-normal font-poppins text-gray-700 flex items-center gap-2">
                      <img class="w-6 h-6" src="phone.svg" alt="Phone" />
                      Phone Number
                    </label>
                    <input
                      type="tel"
                      [(ngModel)]="editableUser.phoneNumber"
                      class="input-base"
                      placeholder="Phone number"
                    />
                  </div>
                </div>

                <!-- Action Buttons -->
                <div class="flex flex-col gap-3">
                  <button (click)="onOpenPswdChange()" class="cursor-pointer h-12 border-[1.5px] border-gray-300 rounded-full text-sm font-normal font-poppins text-gray-700 hover:bg-gray-50 transition-colors">
                    Change Password
                  </button>

                  <div class="flex gap-4 flex-col sm:flex-row">
                    <button class="flex-1 h-12 bg-lime-400 rounded-full text-sm font-normal font-poppins text-neutral-900 hover:bg-lime-500 transition-colors">
                      Update Profile
                    </button>
                    <button (click)="onCancel()" class="cursor-pointer flex-1 h-12 border-[1.5px] border-gray-300 rounded-full text-sm font-normal font-poppins text-gray-700 hover:bg-gray-50 transition-colors">
                      Cancel
                    </button>
                  </div>
                </div>
              </div>
        </div>

      </div>
  `,
})
export class ProfileInfoCard {
  @Input() user!: User;
  @Output() save = new EventEmitter<User>();
  @Output() cancel = new EventEmitter<void>();
  @Output() editAvatar = new EventEmitter<void>();
  @Output() openPswdChange = new EventEmitter<void>();
  
  editableUser!:User;

  ngOnInit(): void {
    this.editableUser = { ...this.user };
  }

  onOpenPswdChange(): void {
    this.openPswdChange.emit();
  }

  onSave(): void {
    this.save.emit(this.editableUser);
  }

  onCancel(): void {
    console.log(this.user);
    console.log(this.editableUser);
    
    
    this.editableUser = { ...this.user }

    console.log(this.editableUser);
    
  }

}
