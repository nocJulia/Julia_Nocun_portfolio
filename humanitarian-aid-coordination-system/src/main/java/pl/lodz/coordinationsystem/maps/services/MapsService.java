package pl.lodz.coordinationsystem.maps.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.lodz.coordinationsystem.maps.model.Location;
import pl.lodz.coordinationsystem.maps.repositories.LocationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class MapsService implements IMaps {
    private final LocationRepository locationRepository;

    public MapsService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> getAllLocations() {
        return StreamSupport.stream(locationRepository.findAll().spliterator(), false)
                .map(LocationMapper::toModel)
                .toList();
    }

    @Override
    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id).map(LocationMapper::toModel);
    }

    @Override
    @Transactional
    public Long saveLocation(Location location) {
        if (location.getLatitude() == 0 || location.getLongitude() == 0) {
            throw new IllegalArgumentException("Invalid location coordinates.");
        }
        return locationRepository.save(LocationMapper.toEntity(location)).getId();
    }

    @Override
    @Transactional
    public void deleteLocation(Location location) {
        locationRepository.delete(LocationMapper.toEntity(location));
    }
}
