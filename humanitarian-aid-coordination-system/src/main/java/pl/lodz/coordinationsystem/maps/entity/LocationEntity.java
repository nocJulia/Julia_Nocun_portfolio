package pl.lodz.coordinationsystem.maps.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "locations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "location_type", discriminatorType = DiscriminatorType.STRING)
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitude;
    private double longitude;
    private String description;

    public LocationEntity(Long id, double latitude, double longitude, String description) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public LocationEntity() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
