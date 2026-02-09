package com.example.mobileapp.features.shared.api.dto;

import java.util.List;

public class OsrmRouteResponse {
    public List<Route> routes;

    public static class Route {
        public double distance;
        public double duration;
        public Geometry geometry;
    }

    public static class Geometry {
        public List<List<Double>> coordinates;
    }
}
