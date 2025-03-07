package pl.lodz.coordinationsystem.maps.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("NOTIFICATION")
public class NotificationLocation extends LocationEntity {

    public NotificationLocation(Long id, double latitude, double longitude, String description) {
        super(id, latitude, longitude, description);
    }

    public NotificationLocation() {

    }
}
