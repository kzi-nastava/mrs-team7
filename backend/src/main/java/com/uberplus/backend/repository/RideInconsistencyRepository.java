package com.uberplus.backend.repository;

import com.uberplus.backend.model.RideInconsistency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideInconsistencyRepository extends JpaRepository<RideInconsistency, Integer> {
}
