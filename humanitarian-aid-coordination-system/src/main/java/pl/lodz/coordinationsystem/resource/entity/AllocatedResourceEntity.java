package pl.lodz.coordinationsystem.resource.entity;

import jakarta.persistence.*;
import pl.lodz.coordinationsystem.donation.entity.DonationEntity;
import pl.lodz.coordinationsystem.notification.entity.NotificationEntity;

@Entity
@Table(name = "allocated_resources")
public class AllocatedResourceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "donation_id", nullable = false)
    private DonationEntity donationEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_id", nullable = false)
    private NotificationEntity notification;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transport_id", nullable = false)
    private TransportEntity transportEntity;
    @Column(name = "quantity", nullable = false)
    private int quantity;

    public AllocatedResourceEntity() {
    }

    public AllocatedResourceEntity(DonationEntity donationEntity, NotificationEntity notificationEntity, int quantity,TransportEntity transportEntity) {
        this.donationEntity = donationEntity;
        this.notification = notificationEntity;
        this.quantity = quantity;
        this.transportEntity = transportEntity;
    }

    public Long getId() {
        return id;
    }

    public DonationEntity getDonationEntity() {
        return donationEntity;
    }

    public void setDonationEntity(DonationEntity donationEntity) {
        this.donationEntity = donationEntity;
    }

    public void setTransportEntity(TransportEntity transportEntity) {
        this.transportEntity = transportEntity;
    }

    public TransportEntity getTransportEntity() {
        return transportEntity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DonationEntity getDonation() {
        return donationEntity;
    }

    public void setDonation(DonationEntity donationEntity) {
        this.donationEntity = donationEntity;
    }

    public NotificationEntity getNotification() {
        return notification;
    }

    public void setNotification(NotificationEntity notificationEntity) {
        this.notification = notificationEntity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
