package pl.lodz.coordinationsystem.maps.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VOLUNTEER")
public class VolunteerLocalization extends LocationEntity {

    public VolunteerLocalization(Long id, double latitude, double longitude, String description) {
        super(id, latitude, longitude, description);
    }

    public VolunteerLocalization() {

    }
}
