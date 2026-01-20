import { HttpClient } from "@angular/common/http";
import { ConfigService } from "./config.service";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs/internal/Observable";
export interface RideEstimateDTO {
  estimatedDistance: number;
  vehicleType: 'STANDARD' | 'LUXURY' | 'VAN';
}
export interface RideEstimateResponseDTO {
  finalPrice: number;
  priceDisplay: string;
}
@Injectable({
  providedIn: 'root'
})

export class RideService{
    constructor(
    private http: HttpClient,
    private config: ConfigService
  ) {}

  public calculatePriceEstimate(request: RideEstimateDTO): Observable<RideEstimateResponseDTO> {
    return this.http.post<{finalPrice: number, priceDisplay: string}>(this.config.priceEstimateUrl, request);
  }

}