package pl.lodz.coordinationsystem.maps.services;

import org.springframework.lang.NonNull;
import pl.lodz.coordinationsystem.maps.entity.NotificationLocation;
import pl.lodz.coordinationsystem.maps.entity.ResourceLocalization;
import pl.lodz.coordinationsystem.maps.entity.VolunteerLocalization;
import pl.lodz.coordinationsystem.maps.model.Location;
import pl.lodz.coordinationsystem.maps.entity.LocationEntity;
import pl.lodz.coordinationsystem.maps.model.LocationType;

public class LocationMapper {

    public static LocationEntity toEntity(@NonNull Location location) {
        return switch (location.getLocationType()) {
            case LocationType.VOLUNTEER -> new VolunteerLocalization(location.getId(), location.getLatitude(),
                    location.getLongitude(), location.getDescription());
            case LocationType.RESOURCE -> new ResourceLocalization(location.getId(), location.getLatitude(),
                    location.getLongitude(), location.getDescription());
            case LocationType.NOTIFICATION -> new NotificationLocation(location.getId(), location.getLatitude(),
                    location.getLongitude(), location.getDescription());
        };
    }

    public static Location toModel(@NonNull LocationEntity location) {
        LocationType type;
        if (location instanceof VolunteerLocalization) {
             type = LocationType.VOLUNTEER;
        } else if (location instanceof ResourceLocalization) {
            type = LocationType.RESOURCE;
        }
        else if (location instanceof NotificationLocation) {
            type = LocationType.NOTIFICATION;
        }
        else {
            throw new IllegalArgumentException("Invalid location type");
        }
        return new Location(location.getId(), location.getLatitude(),
                location.getLongitude(), location.getDescription(), type);
    }

}
