package pl.lodz.coordinationsystem.maps.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.coordinationsystem.maps.model.Location;
import pl.lodz.coordinationsystem.maps.model.LocationType;
import pl.lodz.coordinationsystem.maps.repositories.LocationRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IMapsTest {
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    IMaps mapsService;

    @AfterEach
    void tearDown() {
        locationRepository.deleteAll();
    }

    @Test
    void addLocation() {
        Long locationId = mapsService.saveLocation(
                new Location(51.0, 19.0, "Test", LocationType.VOLUNTEER));
        assertEquals(1, mapsService.getAllLocations().size());
        Location location = mapsService.getLocationById(locationId).orElseThrow();
        assertEquals(51.0, location.getLatitude());
        assertEquals(19.0, location.getLongitude());
    }

    @Test
    void editLocation() {
        Long locationId = mapsService.saveLocation(
                new Location(51.0, 19.0, "Test", LocationType.VOLUNTEER));
        Location location = mapsService.getLocationById(locationId).orElseThrow();
        location.setLatitude(60.0);
        Long locationId2 = mapsService.saveLocation(location);
        assertEquals(locationId, locationId2);
        assertEquals(60.0, location.getLatitude());
    }

    @Test
    void deleteLocation() {
        Long locationId = mapsService.saveLocation(
                new Location(51.0, 19.0, "Test", LocationType.VOLUNTEER));
        assertEquals(1, mapsService.getAllLocations().size());
        Location location = mapsService.getLocationById(locationId).orElseThrow();
        mapsService.deleteLocation(location);
        assertEquals(0, mapsService.getAllLocations().size());
    }

    @Test
    void getAllLocations() {
        Long locationId1 = mapsService.saveLocation(
                new Location(51.0, 19.0, "Test", LocationType.VOLUNTEER));
        Long locationId2 = mapsService.saveLocation(
                new Location(511.0, 119.0, "Test", LocationType.RESOURCE));
        assertEquals(2, mapsService.getAllLocations().size());
        Location location1 = mapsService.getLocationById(locationId1).orElseThrow();
        mapsService.deleteLocation(location1);
        assertEquals(1, mapsService.getAllLocations().size());
        Location location2 = mapsService.getLocationById(locationId2).orElseThrow();
        mapsService.deleteLocation(location2);
        assertEquals(0, mapsService.getAllLocations().size());
    }

    @Test
    void getLocationById() {
        Long locationId1 = mapsService.saveLocation(
                new Location(51.0, 19.0, "Test", LocationType.VOLUNTEER));
        Long locationId2 = mapsService.saveLocation(
                new Location(511.0, 119.0, "Test", LocationType.RESOURCE));
        Location location1 = mapsService.getLocationById(locationId1).orElseThrow();
        Location location2 = mapsService.getLocationById(locationId2).orElseThrow();

        assertEquals(location1.getId(), locationId1);
        assertEquals(location2.getId(), locationId2);
    }
}