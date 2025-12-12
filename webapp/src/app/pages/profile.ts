import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../components/header.component';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent],
  template: `
    <div class="min-h-screen bg-app-bg">
      <app-header></app-header>
      
      <div class="flex flex-col justify-center items-center flex-1 px-4 py-8 md:py-16">
        <div class="w-full max-w-2xl rounded-[44px] bg-app-dark border border-app-border p-6 md:p-8">
          <!-- Profile Image Section -->
          <div class="flex flex-col items-center mb-8">
            <div class="relative">
              <div class="w-32 h-32 md:w-40 md:h-40 rounded-full bg-app-accent flex items-center justify-center">
                <img 
                  src="https://api.builder.io/api/v1/image/assets/TEMP/7bb9d35edb1cdec2ff902574931097ba1a48e419?width=308"
                  alt="Profile"
                  class="w-28 h-28 md:w-36 md:h-36 rounded-full object-cover"
                />
              </div>
              <!-- Edit Icon -->
              <div class="absolute bottom-2 right-2 w-8 h-8 md:w-10 md:h-10 rounded-full bg-app-accent flex items-center justify-center cursor-pointer hover:opacity-80 transition">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <g clip-path="url(#clip0_98_186)">
                    <path d="M11.3334 2.00004C11.5085 1.82494 11.7163 1.68605 11.9451 1.59129C12.1739 1.49653 12.4191 1.44775 12.6667 1.44775C12.9143 1.44775 13.1595 1.49653 13.3883 1.59129C13.6171 1.68605 13.8249 1.82494 14 2.00004C14.1751 2.17513 14.314 2.383 14.4088 2.61178C14.5036 2.84055 14.5523 3.08575 14.5523 3.33337C14.5523 3.58099 14.5036 3.82619 14.4088 4.05497C14.314 4.28374 14.1751 4.49161 14 4.66671L5.00004 13.6667L1.33337 14.6667L2.33337 11L11.3334 2.00004Z" stroke="#1E1E1E" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                  </g>
                  <defs>
                    <clipPath id="clip0_98_186">
                      <rect width="16" height="16" fill="white"/>
                    </clipPath>
                  </defs>
                </svg>
              </div>
            </div>
          </div>

          <!-- Form Fields -->
          <div class="space-y-6">
            <!-- Name and Surname Row -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <!-- Name Input -->
              <div class="flex flex-col gap-2">
                <label class="text-app-bg text-sm font-medium">Name</label>
                <input
                  type="text"
                  placeholder="Value"
                  class="w-full px-4 py-3 rounded-lg bg-white border border-app-border text-app-text-secondary placeholder-app-text-secondary focus:outline-none focus:ring-2 focus:ring-app-accent"
                />
              </div>

              <!-- Surname Input -->
              <div class="flex flex-col gap-2">
                <label class="text-app-bg text-sm font-medium">Surname</label>
                <input
                  type="text"
                  placeholder="Value"
                  class="w-full px-4 py-3 rounded-lg bg-white border border-app-border text-app-text-secondary placeholder-app-text-secondary focus:outline-none focus:ring-2 focus:ring-app-accent"
                />
              </div>
            </div>

            <!-- Email Input -->
            <div class="flex flex-col gap-2">
              <div class="flex items-center gap-2">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M18.3333 4.99992C18.3333 4.08325 17.5833 3.33325 16.6666 3.33325H3.33329C2.41663 3.33325 1.66663 4.08325 1.66663 4.99992M18.3333 4.99992V14.9999C18.3333 15.9166 17.5833 16.6666 16.6666 16.6666H3.33329C2.41663 16.6666 1.66663 15.9166 1.66663 14.9999V4.99992M18.3333 4.99992L9.99996 10.8333L1.66663 4.99992" stroke="#F7F7F5" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
                <label class="text-app-bg text-sm font-medium">Email</label>
              </div>
              <input
                type="email"
                placeholder="Value"
                class="w-full px-4 py-3 rounded-lg bg-white border border-app-border text-app-text-secondary placeholder-app-text-secondary focus:outline-none focus:ring-2 focus:ring-app-accent"
              />
            </div>

            <!-- Address Input -->
            <div class="flex flex-col gap-2">
              <div class="flex items-center gap-2">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M10 10.0001C10.4584 10.0001 10.8507 9.83689 11.1771 9.5105C11.5035 9.18411 11.6667 8.79175 11.6667 8.33341C11.6667 7.87508 11.5035 7.48272 11.1771 7.15633C10.8507 6.82994 10.4584 6.66675 10 6.66675C9.54171 6.66675 9.14935 6.82994 8.82296 7.15633C8.49657 7.48272 8.33337 7.87508 8.33337 8.33341C8.33337 8.79175 8.49657 9.18411 8.82296 9.5105C9.14935 9.83689 9.54171 10.0001 10 10.0001ZM10 16.1251C11.6945 14.5695 12.9514 13.1563 13.7709 11.8855C14.5903 10.6147 15 9.48619 15 8.50008C15 6.98619 14.5174 5.74661 13.5521 4.78133C12.5868 3.81605 11.4028 3.33341 10 3.33341C8.59726 3.33341 7.41324 3.81605 6.44796 4.78133C5.48268 5.74661 5.00004 6.98619 5.00004 8.50008C5.00004 9.48619 5.40976 10.6147 6.22921 11.8855C7.04865 13.1563 8.3056 14.5695 10 16.1251ZM10 18.3334C7.76393 16.4306 6.09379 14.6633 4.98962 13.0313C3.88546 11.3994 3.33337 9.88897 3.33337 8.50008C3.33337 6.41675 4.00351 4.75703 5.34379 3.52091C6.68407 2.2848 8.23615 1.66675 10 1.66675C11.7639 1.66675 13.316 2.2848 14.6563 3.52091C15.9966 4.75703 16.6667 6.41675 16.6667 8.50008C16.6667 9.88897 16.1146 11.3994 15.0105 13.0313C13.9063 14.6633 12.2362 16.4306 10 18.3334Z" fill="#F7F7F5"/>
                </svg>
                <label class="text-app-bg text-sm font-medium">Address</label>
              </div>
              <input
                type="text"
                placeholder="Value"
                class="w-full px-4 py-3 rounded-lg bg-white border border-app-border text-app-text-secondary placeholder-app-text-secondary focus:outline-none focus:ring-2 focus:ring-app-accent"
              />
            </div>

            <!-- Phone Number Input -->
            <div class="flex flex-col gap-2">
              <div class="flex items-center gap-2">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <g clip-path="url(#clip0_98_158)">
                    <path d="M18.3333 14.0999V16.5999C18.3343 16.832 18.2867 17.0617 18.1937 17.2744C18.1008 17.487 17.9644 17.6779 17.7934 17.8348C17.6224 17.9917 17.4205 18.1112 17.2006 18.1855C16.9808 18.2599 16.7478 18.2875 16.5167 18.2666C13.9523 17.988 11.4892 17.1117 9.32498 15.7083C7.31151 14.4288 5.60443 12.7217 4.32499 10.7083C2.91663 8.53426 2.04019 6.05908 1.76665 3.48325C1.74583 3.25281 1.77321 3.02055 1.84707 2.80127C1.92092 2.58199 2.03963 2.38049 2.19562 2.2096C2.35162 2.03871 2.54149 1.90218 2.75314 1.80869C2.9648 1.7152 3.1936 1.6668 3.42499 1.66658H5.92498C6.32941 1.6626 6.72148 1.80582 7.02812 2.06953C7.33476 2.33324 7.53505 2.69946 7.59165 3.09992C7.69717 3.89997 7.89286 4.68552 8.17499 5.44158C8.2871 5.73985 8.31137 6.06401 8.24491 6.37565C8.17844 6.68729 8.02404 6.97334 7.79998 7.19992L6.74165 8.25825C7.92795 10.3445 9.65536 12.072 11.7417 13.2583L12.8 12.1999C13.0266 11.9759 13.3126 11.8215 13.6243 11.755C13.9359 11.6885 14.26 11.7128 14.5583 11.8249C15.3144 12.107 16.0999 12.3027 16.9 12.4083C17.3048 12.4654 17.6745 12.6693 17.9388 12.9812C18.203 13.2931 18.3435 13.6912 18.3333 14.0999Z" stroke="#F7F7F5" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </g>
                  <defs>
                    <clipPath id="clip0_98_158">
                      <rect width="20" height="20" fill="white"/>
                    </clipPath>
                  </defs>
                </svg>
                <label class="text-app-bg text-sm font-medium">Phone Number</label>
              </div>
              <input
                type="tel"
                placeholder="Value"
                class="w-full px-4 py-3 rounded-lg bg-white border border-app-border text-app-text-secondary placeholder-app-text-secondary focus:outline-none focus:ring-2 focus:ring-app-accent"
              />
            </div>
          </div>

          <!-- Buttons -->
          <div class="mt-8 space-y-4">
            <!-- Change Password Button -->
            <button class="w-full py-3 px-4 rounded-full border-4 border-app-accent bg-app-dark text-app-accent font-medium hover:bg-opacity-80 transition">
              Change Password
            </button>

            <!-- Update Profile Button -->
            <button class="w-full py-3 px-4 rounded-full bg-app-accent text-app-dark font-medium hover:opacity-90 transition">
              Update Profile
            </button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class ProfileComponent {}
