import { inject, Injectable } from "@angular/core";
import { LocationDTO } from "../../shared/models/location";
import { RideDTO } from "../../shared/models/ride";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { ConfigService } from "../../../core/services/config.service";
import { VehicleType, vehicleTypeKeyFromValue } from "../../shared/models/vehicle";

export interface RideCreationDTO {
    startLocation: LocationDTO,
    endLocation: LocationDTO,
    vehicleType?: keyof typeof VehicleType,
    waypoints: LocationDTO[],
    babyFriendly: boolean,
    petFriendly: boolean,
    linkedPassengerEmails: string[],
    scheduledTime: string,
    estimatedDurationMinutes: number,
    favoriteRouteId?: number,

    distance: number //novo
}

@Injectable({
  providedIn: 'root'
})
export class RideOrderService {
    http = inject(HttpClient);
    config = inject(ConfigService);

    requestRide(startLocation: LocationDTO, endLocation: LocationDTO, vehicleType: VehicleType | undefined, waypoints: LocationDTO[], babyFriendly: boolean,
        petFriendly: boolean, linkedPassengerEmails: string[], scheduledTime: string, distance: number, estimatedDurationMinutes: number, favoriteRouteId: number | undefined) : Observable<RideDTO> {
        
        let vehicleTypeDTO: (keyof typeof VehicleType) | undefined;

        if(vehicleType) {
            vehicleTypeDTO = vehicleTypeKeyFromValue(vehicleType);
        }

        let body : RideCreationDTO = {
            startLocation,
            endLocation,
            waypoints,
            vehicleType: vehicleTypeDTO,
            babyFriendly,
            petFriendly,
            linkedPassengerEmails,
            scheduledTime,
            distance,
            estimatedDurationMinutes,
            favoriteRouteId
        }

        return this.http.post<RideDTO>(this.config.ridesUrl, body);
    }
}