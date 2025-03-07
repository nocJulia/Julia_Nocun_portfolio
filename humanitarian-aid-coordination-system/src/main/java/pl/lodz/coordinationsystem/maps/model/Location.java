package pl.lodz.coordinationsystem.maps.model;

public class Location {

    private Long id;
    private double latitude;
    private double longitude;
    private String description;
    private LocationType locationType;

    public Location(Long id, double latitude, double longitude, String description, LocationType locationType) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.locationType = locationType;
    }

    public Location(double latitude, double longitude, String description, LocationType locationType) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.locationType = locationType;
    }

    public Location() {}

    public Long getId() {
        return id;
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

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }
}
