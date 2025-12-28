package com.uberplus.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "passengers")
@Data
@EqualsAndHashCode(callSuper = false)
public class Passenger extends User {
}

