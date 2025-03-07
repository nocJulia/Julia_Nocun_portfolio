package pl.lodz.coordinationsystem.maps.services;

import pl.lodz.coordinationsystem.maps.model.Location;

import java.util.List;
import java.util.Optional;

public interface IMaps {
    List<Location> getAllLocations();

    Optional<Location> getLocationById(Long id);

    Long saveLocation(Location location);

    void deleteLocation(Location location);
}
