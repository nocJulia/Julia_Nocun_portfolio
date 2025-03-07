package pl.lodz.coordinationsystem.volunteer.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.coordinationsystem.maps.entity.LocationEntity;
import pl.lodz.coordinationsystem.maps.model.Location;
import pl.lodz.coordinationsystem.maps.services.IMaps;
import pl.lodz.coordinationsystem.maps.services.LocationMapper;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.notification.model.NotificationStatus;
import pl.lodz.coordinationsystem.notification.service.INotifications;
import pl.lodz.coordinationsystem.notification.service.NotificationMapper;
import pl.lodz.coordinationsystem.volunteer.entity.VolunteerEntity;
import pl.lodz.coordinationsystem.volunteer.model.Volunteer;
import pl.lodz.coordinationsystem.volunteer.repository.VolunteerRepository;
import pl.lodz.coordinationsystem.communication.model.Message;
import pl.lodz.coordinationsystem.communication.service.IActors;
import pl.lodz.coordinationsystem.security.model.Role;
import pl.lodz.coordinationsystem.security.services.ISecurity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class VolunteerService implements IVolunteer {

    private final VolunteerRepository volunteerRepository;
    private final IMaps mapsService;
    private final INotifications notificationService;
    private final IActors actorsService;
    private final ISecurity securityService;
    private final PasswordEncoder passwordEncoder;

    public VolunteerService(VolunteerRepository volunteerRepository, IMaps mapsService, INotifications notificationService,
                            IActors actorsService, ISecurity securityService, PasswordEncoder passwordEncoder) {
        this.volunteerRepository = volunteerRepository;
        this.mapsService = mapsService;
        this.notificationService = notificationService;
        this.actorsService = actorsService;
        this.securityService = securityService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Long createVolunteer(Volunteer volunteer) {
        volunteer.setCreatedAt(LocalDateTime.now());
        volunteer.setRole(securityService.getRoleByName("VOLUNTEER").orElseThrow());
        volunteer.setPassword(passwordEncoder.encode(volunteer.getPassword()));
        return volunteerRepository.save(VolunteerMapper.toEntity(volunteer)).getId();
    }

    @Override
    @Transactional
    public void setVolunteerLocation(Long volunteerId, Location location) {
        VolunteerEntity volunteerEntity = volunteerRepository.findById(volunteerId).orElseThrow();
        LocationEntity locationEntity = LocationMapper.toEntity(location);
        volunteerEntity.setLocation(locationEntity);
        volunteerRepository.save(volunteerEntity);

        Role coordinatorRole = securityService.getRoleByName("COORDINATOR").orElseThrow();
        actorsService.addMessageForRole(new Message(coordinatorRole, "New Location Assigned",
                "Volunteer with ID: " + volunteerId + " has been assigned a new location: " + locationEntity.getDescription()));
    }

    @Override
    @Transactional
    public void setVolunteerLocationById(Long volunteerId, Long locationId) {
        Location location = mapsService.getLocationById(locationId).orElseThrow();
        setVolunteerLocation(volunteerId, location);
    }

    @Override
    @Transactional
    public void assignNotificationToVolunteer(Long volunteerId, Notification notification) {
        var volunteerEntity = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer not found with id: " + volunteerId));

        var notificationEntity = NotificationMapper.toEntity(notification);

        if (volunteerEntity.getNotifications().stream()
                .noneMatch(existingNotification -> existingNotification.getId().equals(notificationEntity.getId()))) {
            volunteerEntity.getNotifications().add(notificationEntity);
        }

        volunteerRepository.save(volunteerEntity);

        Role coordinatorRole = securityService.getRoleByName("COORDINATOR").orElseThrow();
        actorsService.addMessageForRole(new Message(coordinatorRole, "New Notification Assigned",
                "Volunteer with ID: " + volunteerId + " has been assigned a new notification with ID: " + notificationEntity.getId()));
    }

    @Override
    @Transactional
    public List<Volunteer> findAllVolunteers() {
        return StreamSupport.stream(volunteerRepository.findAll().spliterator(), false)
                .map(VolunteerMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public Optional<Volunteer> findVolunteerById(Long id) {
        return volunteerRepository.findById(id).map(VolunteerMapper::toModel);
    }

    @Override
    @Transactional
    public List<Volunteer> filterByStatus(List<Volunteer> volunteers, String status) {
        return volunteers.stream()
                .filter(volunteer -> "BUSY".equalsIgnoreCase(status) != volunteer.getNotifications().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Volunteer> filterByState(List<Volunteer> volunteers, String state) {
        return volunteers.stream()
                .filter(volunteer -> "ACTIVE".equalsIgnoreCase(state) == volunteer.getActive())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Notification> getAvailableNotificationsForVolunteer(Volunteer volunteer) {
        List<Notification> allNotifications = notificationService.findAllNotifications();
        Set<Long> assignedNotificationIds = volunteer.getNotifications().stream()
                .map(Notification::getId)
                .collect(Collectors.toSet());

        return allNotifications.stream()
                .filter(notification -> !assignedNotificationIds.contains(notification.getId()))
                .filter(notification -> notification.getStatus() == NotificationStatus.PENDING
                        || notification.getStatus() == NotificationStatus.APPROVED)
                .toList();
    }

    @Override
    @Transactional
    public void updateVolunteerById(Long volunteerId, Volunteer updatedVolunteer) {
        Volunteer volunteer = findVolunteerById(volunteerId).orElseThrow();
        volunteer.setFirstName(updatedVolunteer.getFirstName());
        volunteer.setLastName(updatedVolunteer.getLastName());
        volunteer.setPhoneNumber(updatedVolunteer.getPhoneNumber());
        volunteer.setActive(updatedVolunteer.getActive());
        volunteer.setSkills(updatedVolunteer.getSkills());
        volunteerRepository.save(VolunteerMapper.toEntity(volunteer));
    }

    @Override
    @Transactional
    public void updatePassword(Long volunteerId, String newPassword) {
        VolunteerEntity volunteerEntity = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer not found with id: " + volunteerId));

        volunteerEntity.setPassword(passwordEncoder.encode(newPassword));
        volunteerRepository.save(volunteerEntity);
    }


    @Override
    @Transactional
    public void addNotification(Long volunteerId, Long notificationId) {
        Notification notification = notificationService.findNotificationById(notificationId).orElseThrow();
        assignNotificationToVolunteer(volunteerId, notification);
    }

    @Override
    @Transactional
    public void removeNotification(Long volunteerId, Long notificationId) {
        Notification notification = notificationService.findNotificationById(notificationId).orElseThrow();
        removeNotificationFromVolunteerById(volunteerId,  notification);
    }

    @Override
    @Transactional
    public void removeNotificationFromVolunteerById(Long volunteerId, Notification notification) {
        Optional<VolunteerEntity> volunteerEntityOptional = volunteerRepository.findById(volunteerId);
        volunteerEntityOptional.ifPresent(volunteerEntity -> {
            volunteerEntity.getNotifications().removeIf(notif -> notif.getId().equals(notification.getId()));
            volunteerRepository.save(volunteerEntity);
        });

        Role coordinatorRole = securityService.getRoleByName("COORDINATOR").orElseThrow();
        actorsService.addMessageForRole(new Message(coordinatorRole, "Notification Removed",
                "Notification with ID: " + notification.getId() + " has been removed from volunteer with ID: " + volunteerId));
    }

}