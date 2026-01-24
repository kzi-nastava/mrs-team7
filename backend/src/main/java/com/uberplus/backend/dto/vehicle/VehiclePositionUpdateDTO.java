package com.uberplus.backend.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiclePositionUpdateDTO {
    private double latitude;
    private double longitude;
}