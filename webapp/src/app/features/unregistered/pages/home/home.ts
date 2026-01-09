import { Component } from '@angular/core';
import { HeaderComponent } from '../../../shared/components/header.component';
import { MapComponent } from '../../../shared/map/map';
import {VehicleMarker} from '../../../shared/map/vehicle-marker';

@Component({
  selector: 'app-unregistered-home',
  standalone: true,
  imports: [HeaderComponent, MapComponent],
  templateUrl: './home.html',
})
export class UnregisteredHomeComponent {
  vehicles: VehicleMarker[] = [
    { id: 1, lat: 45.2559, lng: 19.8453, status: 'OCCUPIED', label: '1' },
    { id: 2, lat: 45.2574, lng: 19.8406, status: 'AVAILABLE', label: '2' },
    { id: 3, lat: 45.2592, lng: 19.8469, status: 'AVAILABLE', label: '3' },
    { id: 4, lat: 45.2529, lng: 19.8424, status: 'OCCUPIED', label: '4' },
    { id: 5, lat: 45.2538, lng: 19.8504, status: 'AVAILABLE', label: '5' },

    { id: 6, lat: 45.2417, lng: 19.8429, status: 'OCCUPIED', label: '6' },
    { id: 7, lat: 45.2448, lng: 19.8486, status: 'AVAILABLE', label: '7' },
    { id: 8, lat: 45.2396, lng: 19.8517, status: 'OCCUPIED', label: '8' },
    { id: 9, lat: 45.2473, lng: 19.8552, status: 'AVAILABLE', label: '9' },
    { id: 10, lat: 45.2368, lng: 19.8451, status: 'AVAILABLE', label: '10' },

    { id: 11, lat: 45.2469, lng: 19.8327, status: 'OCCUPIED', label: '11' },
    { id: 12, lat: 45.2488, lng: 19.8379, status: 'AVAILABLE', label: '12' },
    { id: 13, lat: 45.2502, lng: 19.8296, status: 'AVAILABLE', label: '13' },
    { id: 14, lat: 45.2446, lng: 19.8358, status: 'OCCUPIED', label: '14' },

    { id: 15, lat: 45.2672, lng: 19.8069, status: 'AVAILABLE', label: '15' },
    { id: 16, lat: 45.2698, lng: 19.8138, status: 'OCCUPIED', label: '16' },
    { id: 17, lat: 45.2649, lng: 19.8120, status: 'AVAILABLE', label: '17' },
    { id: 18, lat: 45.2626, lng: 19.8048, status: 'OCCUPIED', label: '18' },

    { id: 19, lat: 45.2775, lng: 19.7886, status: 'AVAILABLE', label: '19' },
    { id: 20, lat: 45.2807, lng: 19.7979, status: 'OCCUPIED', label: '20' },
    { id: 21, lat: 45.2746, lng: 19.7952, status: 'AVAILABLE', label: '21' },
    { id: 22, lat: 45.2831, lng: 19.7899, status: 'OCCUPIED', label: '22' },

    { id: 23, lat: 45.2866, lng: 19.8427, status: 'AVAILABLE', label: '23' },
    { id: 24, lat: 45.2912, lng: 19.8516, status: 'OCCUPIED', label: '24' },
    { id: 25, lat: 45.2839, lng: 19.8532, status: 'AVAILABLE', label: '25' },
    { id: 26, lat: 45.2884, lng: 19.8349, status: 'OCCUPIED', label: '26' },

    { id: 27, lat: 45.2644, lng: 19.8548, status: 'AVAILABLE', label: '27' },
    { id: 28, lat: 45.2683, lng: 19.8573, status: 'OCCUPIED', label: '28' },
    { id: 29, lat: 45.2706, lng: 19.8511, status: 'AVAILABLE', label: '29' },

    { id: 30, lat: 45.2897, lng: 19.7892, status: 'OCCUPIED', label: '30' },
    { id: 31, lat: 45.2934, lng: 19.8011, status: 'AVAILABLE', label: '31' },
    { id: 32, lat: 45.2873, lng: 19.8036, status: 'OCCUPIED', label: '32' },

    { id: 33, lat: 45.2357, lng: 19.8112, status: 'AVAILABLE', label: '33' },
    { id: 34, lat: 45.2324, lng: 19.8189, status: 'OCCUPIED', label: '34' },
    { id: 35, lat: 45.2296, lng: 19.8077, status: 'AVAILABLE', label: '35' },
    { id: 36, lat: 45.2389, lng: 19.8208, status: 'OCCUPIED', label: '36' },

    { id: 37, lat: 45.2460, lng: 19.8040, status: 'AVAILABLE', label: '37' },
    { id: 38, lat: 45.2419, lng: 19.7996, status: 'OCCUPIED', label: '38' },

    { id: 39, lat: 45.2479, lng: 19.8722, status: 'AVAILABLE', label: '39' },
    { id: 40, lat: 45.2515, lng: 19.8796, status: 'OCCUPIED', label: '40' },
    { id: 41, lat: 45.2448, lng: 19.8834, status: 'AVAILABLE', label: '41' },

    { id: 42, lat: 45.2322, lng: 19.8978, status: 'OCCUPIED', label: '42' },
    { id: 43, lat: 45.2269, lng: 19.8894, status: 'AVAILABLE', label: '43' },

    { id: 44, lat: 45.2365, lng: 19.8539, status: 'AVAILABLE', label: '44' },
    { id: 45, lat: 45.2408, lng: 19.8607, status: 'OCCUPIED', label: '45' },

    { id: 46, lat: 45.3002, lng: 19.8281, status: 'AVAILABLE', label: '46' },
    { id: 47, lat: 45.3048, lng: 19.8456, status: 'OCCUPIED', label: '47' },
    { id: 48, lat: 45.2971, lng: 19.8522, status: 'AVAILABLE', label: '48' },

    { id: 49, lat: 45.2442, lng: 19.9086, status: 'OCCUPIED', label: '49' },
    { id: 50, lat: 45.2374, lng: 19.9158, status: 'AVAILABLE', label: '50' },

    { id: 51, lat: 45.2729, lng: 19.8316, status: 'OCCUPIED', label: '51' },
  ];
}
