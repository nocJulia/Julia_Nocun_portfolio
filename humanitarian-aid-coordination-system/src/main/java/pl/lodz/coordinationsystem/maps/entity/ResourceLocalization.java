package pl.lodz.coordinationsystem.maps.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("RESOURCE")
public class ResourceLocalization extends LocationEntity {
    public ResourceLocalization(Long id, double latitude, double longitude, String description) {
        super(id, latitude, longitude, description);
    }

    public ResourceLocalization() {

    }

}
