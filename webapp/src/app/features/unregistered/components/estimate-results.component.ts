import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-estimate-results',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="fixed inset-0 z-[1001] bg-black/50 flex items-center justify-center p-4 animate-slideUp">
      <div class="bg-white rounded-3xl shadow-2xl w-full max-w-md max-h-[80vh] overflow-hidden">
        <div class="bg-green-50 rounded-t-3xl p-6 text-center">
          <div class="text-3xl font-bold text-green-700 mb-2">{{ estimateRange }}</div>
          <div class="text-lg text-green-800 mb-4">{{ distance }} km</div>
        </div>
        <div class="p-6 space-y-3">
          <button class="w-full bg-blue-500 hover:bg-blue-600 text-white py-4 px-6 rounded-2xl text-base font-semibold transition-colors" (click)="onMapView()">
            View on map
          </button>
          <button class="w-full bg-[var(--color-app-accent)] hover:bg-[#A9D53A] text-app-dark py-4 px-6 rounded-2xl text-base font-semibold transition-colors" (click)="onBookRide()">
            Book a ride
          </button>
          <button class="w-full text-gray-500 py-3 text-sm" (click)="close.emit()">Cancel</button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    @keyframes slideUp {
      from { transform: translateY(100%); opacity: 0; }
      to { transform: translateY(0); opacity: 1; }
    }
    .animate-slideUp {
      animation: slideUp 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    }
  `]
})
export class EstimateResultsComponent {
  @Output() close = new EventEmitter<void>();
  estimateRange = '€10-€13';
  distance = '10-13';
  
  onMapView() { console.log('Map'); }
  onBookRide() { console.log('Book'); }
}
