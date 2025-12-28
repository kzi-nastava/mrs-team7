package com.uberplus.backend.repository;

import com.uberplus.backend.model.FavoriteRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRouteRepository extends JpaRepository<FavoriteRoute, Integer> {
}
