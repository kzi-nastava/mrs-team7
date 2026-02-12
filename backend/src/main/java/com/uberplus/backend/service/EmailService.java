package com.uberplus.backend.service;

import com.uberplus.backend.model.Driver;
import com.uberplus.backend.model.Passenger;
import com.uberplus.backend.model.Ride;
import com.uberplus.backend.model.User;

public interface EmailService {
    void sendActivationEmail(Passenger user);

    void sendDriverActivationEmail(Driver driver);

    void sendPasswordResetEmail(User user, String token);

    void sendRideAcceptedEmail(Ride ride, Passenger passenger);

    void sendRideCompletedEmail(Ride ride, Passenger passenger);
}
