import { Component, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-estimate-panel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <div class="font-sans fixed bottom-6 left-6 z-[1000] w-80 bg-white rounded-3xl shadow-[0px_13px_27px_0px_rgba(0,0,0,0.25)] items-center gap-4 p-6 max-w-sm md:bottom-8 md:left-8 md:w-96">
        <h3 class="text-3xl text-center font-semibold text-black md:text-3xl lg:text-3xl font-semibold mb-4 md:mb-4">Go anywhere</h3>
        <form #f = "ngForm" (ngSubmit)="onSubmit(f)" class="space-y-3">
            <input type="text" placeholder="Pickup location" class="input-estimate w-full">
            <input type="text" placeholder="Dropoff location" class="input-estimate w-full">
            <button class="w-full bg-[var(--color-app-accent)] hover:bg-[#A9D53A] text-app-dark px-6 py-3 rounded-3xl font-semibold transition-colors shadow-[0px_7px_27px_0px_rgba(0,0,0,0.25)] font-[Poppins] text-base">
            Get estimate
            </button>
</form>
</div>
`,
})
export class EstimatePanelComponent {
    @ViewChild('f') estimateForm!: NgForm;

  onSubmit(form: NgForm) {
    if (form.valid) {
      console.log('Estimate:', form.value);
      // Next: emit/geocode
    }
  }
}