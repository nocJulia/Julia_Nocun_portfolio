package pl.lodz.coordinationsystem.volunteer.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.coordinationsystem.communication.repository.MessageRepository;
import pl.lodz.coordinationsystem.maps.model.Location;
import pl.lodz.coordinationsystem.maps.model.LocationType;
import pl.lodz.coordinationsystem.maps.repositories.LocationRepository;
import pl.lodz.coordinationsystem.maps.services.IMaps;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.notification.model.Victim;
import pl.lodz.coordinationsystem.notification.repository.NotificationRepository;
import pl.lodz.coordinationsystem.notification.service.INotifications;
import pl.lodz.coordinationsystem.security.model.Role;
import pl.lodz.coordinationsystem.security.repositories.BaseUserRepository;
import pl.lodz.coordinationsystem.security.services.ISecurity;
import pl.lodz.coordinationsystem.volunteer.model.Volunteer;
import pl.lodz.coordinationsystem.volunteer.repository.VolunteerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VolunteerServiceTest {
    @Autowired
    VolunteerService volunteerService;
    @Autowired
    VolunteerRepository volunteerRepository;
    @Autowired
    IMaps mapsService;
    @Autowired
    INotifications notificationService;
    @Autowired
    ISecurity securityService;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    BaseUserRepository baseUserRepository;


    private Volunteer testVolunteer;
    private Location testLocation;
    private List<Victim> victims;

    @BeforeEach
    void setUp() {
        Role testRole = securityService.getRoleByName("VOLUNTEER").orElseThrow();

        testVolunteer = new Volunteer("John","Doe","john.doe@example.com");
        testVolunteer.setPassword("password");
        testVolunteer.setCreatedAt(LocalDateTime.now());
        testVolunteer.setPhoneNumber("1234567890");
        testVolunteer.setRole(testRole);

        testLocation = new Location(51.759, 19.456, "Test Location", LocationType.VOLUNTEER);
        victims = List.of(new Victim("John", "Down"));

    }

    @AfterEach
    void tearDown() {
        volunteerRepository.deleteAll();
        notificationRepository.deleteAll();
        locationRepository.deleteAll();
        messageRepository.deleteAll();
        baseUserRepository.deleteAll();
    }

    @Test
    void createVolunteerTest() {
        Long volunteerId = volunteerService.createVolunteer(testVolunteer);
        assertNotNull(volunteerId);

        var savedVolunteer = volunteerRepository.findById(volunteerId);
        assertTrue(savedVolunteer.isPresent());
        assertEquals("John", savedVolunteer.get().getFirstName());
        assertEquals("Doe", savedVolunteer.get().getLastName());
        assertEquals("john.doe@example.com", savedVolunteer.get().getEmail());
    }

    @Test
    void setVolunteerLocationTest() {
        Long volunteerId = volunteerService.createVolunteer(testVolunteer);
        assertNotNull(volunteerId);

        Long locationId = mapsService.saveLocation(testLocation);

        Location savedLocation = mapsService.getLocationById(locationId).orElseThrow();

        volunteerService.setVolunteerLocation(volunteerId, savedLocation);

        Optional<Volunteer> updatedVolunteer = volunteerService.findVolunteerById(volunteerId);
        assertTrue(updatedVolunteer.isPresent());
        assertEquals(testLocation.getLatitude(), updatedVolunteer.get().getLocation().getLatitude());
        assertEquals(testLocation.getLongitude(), updatedVolunteer.get().getLocation().getLongitude());
        assertEquals(testLocation.getDescription(), updatedVolunteer.get().getLocation().getDescription());
    }

    @Test
    void assignNotificationToVolunteer() {
        Long volunteerId = volunteerService.createVolunteer(testVolunteer);
        assertNotNull(volunteerId);

        Long notificationId = notificationService.createNewNotification(new Notification("Test", victims));
        assertNotNull(notificationId);

        volunteerService.assignNotificationToVolunteer(volunteerId, notificationService.findNotificationById(notificationId).orElseThrow());

        Optional<Volunteer> updatedVolunteer = volunteerService.findVolunteerById(volunteerId);
        assertTrue(updatedVolunteer.isPresent());

        List<Notification> notifications = updatedVolunteer.get().getNotifications();
        assertTrue(notifications.stream().anyMatch(notification -> notification.getId().equals(notificationId)));

        assertEquals(1, notifications.size());
    }

    @Test
    void findAllVolunteers() {
        volunteerService.createVolunteer(testVolunteer);

        var volunteers = volunteerService.findAllVolunteers();
        assertFalse(volunteers.isEmpty());
        assertEquals(1, volunteers.size());
        assertEquals("John", volunteers.get(0).getFirstName());
    }

    @Test
    void findVolunteerById() {
        Long volunteerId = volunteerService.createVolunteer(testVolunteer);
        assertNotNull(volunteerId);

        Optional<Volunteer> volunteer = volunteerService.findVolunteerById(volunteerId);
        assertTrue(volunteer.isPresent());
        assertEquals("John", volunteer.get().getFirstName());
    }

    @Test
    void removeNotificationFromVolunteerById() {
        Long volunteerId = volunteerService.createVolunteer(testVolunteer);
        assertNotNull(volunteerId);

        Long notificationId = notificationService.createNewNotification(new Notification("Test Notification", null));
        assertNotNull(notificationId);

        Notification notification = notificationService.findNotificationById(notificationId).orElseThrow();

        volunteerService.assignNotificationToVolunteer(volunteerId, notification);
        volunteerService.removeNotificationFromVolunteerById(volunteerId, notification);

        Optional<Volunteer> updatedVolunteer = volunteerService.findVolunteerById(volunteerId);
        assertTrue(updatedVolunteer.isPresent());
        assertFalse(updatedVolunteer.get().getNotifications().contains(notificationId));
    }

}