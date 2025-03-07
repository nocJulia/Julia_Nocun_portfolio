package pl.lodz.coordinationsystem.resource.model;

import pl.lodz.coordinationsystem.donation.model.Donation;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.resource.entity.TransportEntity;

public class AllocatedResource {
    private Donation donation;
    private Notification notification;
    private int quantity;

    private Transport transport;

    public AllocatedResource(Donation donation, Notification notification, int quantity, Transport transport) {
        this.donation = donation;
        this.notification = notification;
        this.quantity = quantity;
        this.transport = transport;
    }

    public Donation getDonation() {
        return donation;
    }

    public Notification getNotification() {
        return notification;
    }

    public int getQuantity() {
        return quantity;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}
