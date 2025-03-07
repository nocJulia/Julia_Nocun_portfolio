package pl.lodz.coordinationsystem.notification.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.coordinationsystem.communication.repository.MessageRepository;
import pl.lodz.coordinationsystem.maps.repositories.LocationRepository;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.notification.model.NotificationStatus;
import pl.lodz.coordinationsystem.notification.model.Victim;
import pl.lodz.coordinationsystem.notification.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class INotificationTest {
    @Autowired
    INotifications notificationService;
    List<Victim> victims;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    LocationRepository locationRepository;

    @BeforeEach
    void setUp() {
        victims = List.of(new Victim("John", "Down"));
    }

    @AfterEach
    void tearDown() {
        notificationRepository.deleteAll();
        messageRepository.deleteAll();
        locationRepository.deleteAll();
    }

    @Test
    void createdNewNotification() {
        notificationService.createNewNotification(new Notification("Test", victims));
        assertEquals(1, notificationService.findAllNotifications().size());
    }

    @Test
    void assignNewVictimToNotification() {
        Long notificationId = notificationService.createNewNotification(new Notification("Test", victims));
        notificationService.assignNewVictimToNotification(notificationId, new Victim("John", "Down"));
        assertEquals(2, notificationService.findNotificationById(notificationId).orElseThrow().getVictims().size());
    }

    @Test
    void removeVictimFromNotificationById() {
        Long notificationId = notificationService.createNewNotification(new Notification("Test", victims));
        Notification notification = notificationService.findNotificationById(notificationId).orElseThrow();
        notificationService.removeVictimFromNotificationById(notificationId, notification.getVictims().getFirst().getId());
        assertEquals(0, notificationService.findNotificationById(notificationId).orElseThrow().getVictims().size());
    }

    @Test
    void setNotificationLocation() {
        Long notificationId = notificationService.createNewNotification(new Notification("Test", victims));
        notificationService.setLocationToNotification(notificationId, 1.0, 1.0);
        notificationService.findNotificationById(notificationId);
        assertEquals(1.0, notificationService.findNotificationById(notificationId).orElseThrow().getLocation().getLatitude());
        assertEquals(1.0, notificationService.findNotificationById(notificationId).orElseThrow().getLocation().getLongitude());
    }

    @Test
    void findNotificationsByStatus() {
        notificationService.createNewNotification(new Notification("Test", victims));
        notificationService.createNewNotification(new Notification("Test", victims));
        notificationService.createNewNotification(new Notification("Test", victims));
        List<Notification> notifications = notificationService.findNotificationsByStatus(NotificationStatus.PENDING);
        assertEquals(3, notificationService.findNotificationsByStatus(NotificationStatus.PENDING).size());
        notificationService.updateNotificationStatus(notifications.getFirst().getId(), NotificationStatus.IN_PROGRESS);
        assertEquals(2, notificationService.findNotificationsByStatus(NotificationStatus.PENDING).size());
    }

    @Test
    void findNotificationsByCreationDateRange() {
        notificationService.createNewNotification(new Notification("Test", victims));
        notificationService.createNewNotification(new Notification("Test", victims));
        notificationService.createNewNotification(new Notification("Test", victims));
        assertEquals(3, notificationService.findNotificationsByCreationDateRange(
                LocalDateTime.now().minusMinutes(1), LocalDateTime.now().plusMinutes(1)).size());
    }

    @Test
    void findNotificationById() {
        Long notificationId = notificationService.createNewNotification(new Notification("Test", victims));
        assertEquals("Test", notificationService.findNotificationById(notificationId).orElseThrow().getDescription());
    }

    @Test
    void findAllNotifications() {
        notificationService.createNewNotification(new Notification("Test", victims));
        notificationService.createNewNotification(new Notification("Test", victims));
        notificationService.createNewNotification(new Notification("Test", victims));
        assertEquals(3, notificationService.findAllNotifications().size());
    }

    @Test
    void updateNotificationStatus() {
        Long notificationId = notificationService.createNewNotification(new Notification("Test", victims));
        notificationService.updateNotificationStatus(notificationId, NotificationStatus.IN_PROGRESS);
        assertEquals(NotificationStatus.IN_PROGRESS, notificationService.findNotificationById(notificationId).orElseThrow().getStatus());
    }

    @Test
    void deleteNotification() {
        Long notificationId = notificationService.createNewNotification(new Notification("Test", victims));
        notificationService.deleteNotification(notificationId);
        assertEquals(0, notificationService.findAllNotifications().size());
    }

    @Test
    void updateNotificationDescription() {
        Long notificationId = notificationService.createNewNotification(new Notification("Test", victims));
        notificationService.updateNotificationDescription(notificationId, "New description");
        assertEquals("New description", notificationService.findNotificationById(notificationId).orElseThrow().getDescription());
    }
}