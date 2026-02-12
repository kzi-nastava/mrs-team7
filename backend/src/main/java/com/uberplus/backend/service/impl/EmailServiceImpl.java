package com.uberplus.backend.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.uberplus.backend.model.*;
import com.uberplus.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {

    private final SendGrid sg;
    private final String fromEmail;
    @Value("${app.frontend.activation-url:http://localhost:4200/activate?token=}")
    private String activationUrl;
    @Value("${app.frontend.driver-activation-url:http://localhost:4200/activate-driver?token=}")
    private String driverActivationURl;

    public EmailServiceImpl(@Value("${sendgrid.api.key}") String apiKey, @Value("${sendgrid.from.email}") String fromEmail) {
        this.sg = new SendGrid(apiKey);
        this.fromEmail = fromEmail;
    }
    @Override
    public void sendActivationEmail(Passenger user){
        String token = user.getActivationToken();
        String activationLink = activationUrl + token;
        Email from = new Email(fromEmail, "UberPLUS");
        Email to = new Email(user.getEmail());
        Personalization  personalization = new Personalization ();
        personalization.addDynamicTemplateData("firstName", user.getFirstName());
        personalization.addDynamicTemplateData("activationLink", activationLink);
        personalization.addTo(to);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.addPersonalization(personalization);
        mail.setTemplateId("d-cfc193aca9e34d6998b0fff380c38d92");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            sg.api(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to send activation email", e);
        }
    }

    @Override
    public void sendDriverActivationEmail(Driver driver) {
        String token = driver.getActivationToken();
        String activationLink = driverActivationURl + token;
        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("firstName", driver.getFirstName());
        personalization.addDynamicTemplateData("activationLink", activationLink);
        personalization.addTo(new Email(driver.getEmail()));

        Mail mail = new Mail();
        mail.setFrom(new Email(fromEmail, "UberPLUS"));
        mail.setTemplateId("d-cfc193aca9e34d6998b0fff380c38d92");
        mail.addPersonalization(personalization);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            sg.api(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to send activation email", e);
        }
    }

    @Override
    public void sendPasswordResetEmail(User user, String token) {
        String link = "http://localhost:4200/signIn?token=" + token;
        Email from = new Email(fromEmail, "UberPLUS");
        Email to = new Email(user.getEmail());
        Personalization  personalization = new Personalization ();
        personalization.addDynamicTemplateData("firstName", user.getFirstName());
        personalization.addDynamicTemplateData("resetLink", link);
        personalization.addTo(to);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.addPersonalization(personalization);
        mail.setTemplateId("d-1368ae54b78c4abe9b9b0ae3a7189974");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            sg.api(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    @Override
    public void sendRideAcceptedEmail(Ride ride, Passenger passenger) {
        String rideTrackingLink = "http://localhost:4200/user/current-ride";

        Email from = new Email(fromEmail, "UberPLUS");
        Email to = new Email(passenger.getEmail());

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("firstName", passenger.getFirstName());
        personalization.addDynamicTemplateData("trackingLink", rideTrackingLink);
        personalization.addTo(to);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.addPersonalization(personalization);
        mail.setTemplateId("d-38d95cf62b7a4a78b33a4a27bb02c7d4");

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException("Failed to send ride accepted email", e);
        }
    }

    @Override
    public void sendRideCompletedEmail(Ride ride, Passenger passenger) {
        Email from = new Email(fromEmail, "UberPLUS");
        Email to = new Email(passenger.getEmail());

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("firstName", passenger.getFirstName());
        personalization.addDynamicTemplateData("rideId", ride.getId());
        personalization.addDynamicTemplateData("destinationAddress", ride.getEndLocation().getAddress());
        personalization.addDynamicTemplateData("totalPrice", String.format("%.2f", ride.getTotalPrice()));
        personalization.addTo(to);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.addPersonalization(personalization);
        mail.setTemplateId("d-3dc73d3eeafe4510897d5513833511ea");

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to send ride completed email", e);
        }
    }
}
