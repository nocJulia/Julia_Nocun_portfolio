package pl.lodz.coordinationsystem.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.coordinationsystem.communication.model.Message;
import pl.lodz.coordinationsystem.communication.service.IActors;
import pl.lodz.coordinationsystem.maps.model.Location;
import pl.lodz.coordinationsystem.maps.model.LocationType;
import pl.lodz.coordinationsystem.maps.services.IMaps;
import pl.lodz.coordinationsystem.maps.services.LocationMapper;
import pl.lodz.coordinationsystem.notification.entity.NotificationEntity;
import pl.lodz.coordinationsystem.notification.entity.VictimEntity;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.notification.model.NotificationStatus;
import pl.lodz.coordinationsystem.notification.model.Victim;
import pl.lodz.coordinationsystem.notification.repository.NotificationRepository;
import pl.lodz.coordinationsystem.security.model.Role;
import pl.lodz.coordinationsystem.security.services.ISecurity;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class NotificationService implements INotifications {
    private final NotificationRepository notificationRepository;
    private final IMaps mapsService;
    private final IActors actorsService;
    private final ISecurity securityService;

    public NotificationService(NotificationRepository notificationRepository, IMaps mapsService, IActors actorsService,
                               ISecurity securityService) {
        this.notificationRepository = notificationRepository;
        this.mapsService = mapsService;
        this.actorsService = actorsService;
        this.securityService = securityService;
    }

    @Override
    @Transactional
    public List<Notification> findAllNotifications() {
        return StreamSupport.stream(notificationRepository.findAll().spliterator(), false)
                .map(NotificationMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public Optional<Notification> findNotificationById(Long id) {
        return notificationRepository.findById(id).map(NotificationMapper::toModel);
    }

    @Override
    @Transactional
    public void assignNewVictimToNotification(Long notificationId, Victim victim) {
        Optional<NotificationEntity> notificationEntityOptional = notificationRepository.findById(notificationId);
        notificationEntityOptional.ifPresent(notificationEntity ->
                notificationEntity.getVictims().add(VictimMapper.toEntity(victim)));
    }

    @Override
    @Transactional
    public void removeVictimFromNotificationById(Long notificationId, Long victimId) {
        Optional<NotificationEntity> notificationEntityOptional = notificationRepository.findById(notificationId);
        notificationEntityOptional.ifPresent(notificationEntity -> {
            List<VictimEntity> victims = notificationEntity.getVictims();
            victims.removeIf(victimEntity -> victimEntity.getId().equals(victimId));
        });
    }

    @Override
    @Transactional
    public Long createNewNotification(Notification notification) {
        NotificationEntity save = notificationRepository.save(NotificationMapper.toEntity(notification));
        Role role = securityService.getRoleByName("COORDINATOR").orElseThrow();
        actorsService.addMessageForRole(new Message(role, "New notification created",
                "New notification has been created with id: " + save.getId()));
        return save.getId();
    }

    @Override
    @Transactional
    public void setLocationToNotification(Long id, Double latitude, Double longitude) {
        Optional<NotificationEntity> notificationEntityOptional = notificationRepository.findById(id);
        notificationEntityOptional.ifPresent(notificationEntity -> {
            if (notificationEntity.getLocation() != null) {
                mapsService.deleteLocation(LocationMapper.toModel(notificationEntity.getLocation()));
            }
            Long location_id = mapsService.saveLocation(new Location(latitude, longitude, "Location of notification with id: " + id, LocationType.NOTIFICATION));
            notificationEntity.setLocation(LocationMapper.toEntity(mapsService.getLocationById(location_id).orElseThrow()));
            notificationRepository.save(notificationEntity);
        });
    }

    @Override
    @Transactional
    public List<Notification> findNotificationsByStatus(NotificationStatus status) {
        return notificationRepository.findAllByStatus(status).stream()
                .map(NotificationMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public List<Notification> findNotificationsByCreationDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return notificationRepository.findNotificationEntitiesByCreatedAtGreaterThanAndCreatedAtLessThan(startDate, endDate).stream()
                .map(NotificationMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public void updateNotificationStatus(Long id, NotificationStatus status) {
        Optional<NotificationEntity> notificationEntityOptional = notificationRepository.findById(id);
        notificationEntityOptional.ifPresent(notificationEntity -> {
            notificationEntity.setStatus(status);
            notificationRepository.save(notificationEntity);
        });
        actorsService.addMessageForRole(new Message(securityService.getRoleByName("COORDINATOR").orElseThrow(),
                "Notification status updated", "Notification with id: " + id + " has been updated to status: " + status));
    }

    @Override
    @Transactional
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateNotificationDescription(Long id, String description) {
        Optional<NotificationEntity> notificationEntityOptional = notificationRepository.findById(id);
        notificationEntityOptional.ifPresent(notificationEntity -> {
            notificationEntity.setDescription(description);
            notificationRepository.save(notificationEntity);
        });
        actorsService.addMessageForRole(new Message(securityService.getRoleByName("COORDINATOR").orElseThrow(),
                "Notification description updated", "Notification with id: " + id + " has been updated with new description"));
    }
}
