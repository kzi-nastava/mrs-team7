import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RegisterRequestDto } from '../../../../core/auth/register-request.dto';
import { VehicleType } from '../../../shared/models/vehicle';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-driver-registration',
  imports: [FormsModule, CommonModule],
  templateUrl: './driver-registration.html',
})
export class DriverRegistration {
  firstName = '';
  lastName = '';
  email = '';
  address = '';
  phone = '';
  password = '';
  confirmPassword = '';

  vehicleTypes: string[] = [];

  pets: boolean = false;
  infants: boolean = false;
  vehicleType: string = '';
  vehicleModel: string = '';
  licensePlate: string = '';
  passengers: number = 0;

  registrationSuccess = false;
  registrationError: string | null = null;
  isSubmitting = false;

  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  constructor(public authService : AuthService) {
  }

  ngOnInit() {
      this.vehicleTypes = Object.values(VehicleType);
    }

  onSubmit() {
    this.isSubmitting = true;
    console.log("Submitted");
    

    let req: RegisterRequestDto = {
      email: this.email,
      password: this.password,
      confirmPassword: this.confirmPassword,
      firstName: this.firstName,
      lastName: this.lastName,
      address: this.address,
      phoneNumber: this.phone
    }
    
    this.authService.register(req).subscribe({
          next: () => {
            this.isSubmitting = false;
            this.registrationSuccess = true;
            this.cdr.detectChanges();
            console.log('Signup request sent successfully!');
          },
          error: (err) => {
            this.isSubmitting = false;
            this.registrationError = err.error?.message || 'Registration failed. Please try again.';
            this.cdr.detectChanges();
            console.error('Signup failed', err);
          }}
        )
      }
  closeSuccessPopup() {
    this.registrationSuccess = false;
    this.router.navigate(['/signin']);
  }
}
