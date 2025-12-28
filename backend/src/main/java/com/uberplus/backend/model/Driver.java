package com.uberplus.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drivers")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends User {

    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Vehicle vehicle;

    @Column(nullable = false)
    private boolean available = false;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private double workedMinutesLast24h = 0.0;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private List<Ride> rides = new ArrayList<>();

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private List<Rating> ratings = new ArrayList<>();

    @Column
    private Double averageRating;

}

