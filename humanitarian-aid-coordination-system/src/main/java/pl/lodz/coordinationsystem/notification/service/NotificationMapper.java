package pl.lodz.coordinationsystem.notification.service;

import org.springframework.lang.NonNull;
import pl.lodz.coordinationsystem.maps.services.LocationMapper;
import pl.lodz.coordinationsystem.notification.entity.NotificationEntity;
import pl.lodz.coordinationsystem.notification.entity.VictimEntity;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.notification.model.Victim;
import pl.lodz.coordinationsystem.resource.model.AllocatedResource;
import pl.lodz.coordinationsystem.resource.service.AllocatedResourceMapper;

import java.util.ArrayList;
import java.util.List;

public class NotificationMapper {
    public static NotificationEntity toEntity(@NonNull Notification notification) {
        List<VictimEntity> victimEntities = (notification.getVictims() != null)
                ? notification.getVictims().stream()
                .map(VictimMapper::toEntity)
                .toList()
                : List.of();
        if (notification.getLocation() == null) {
            return new NotificationEntity(notification.getId(), notification.getDescription(),
                    victimEntities, null,
                    notification.getCreatedAt(), notification.getStatus());
        }
        return new NotificationEntity(notification.getId(), notification.getDescription(),
                victimEntities, LocationMapper.toEntity(notification.getLocation()),
                notification.getCreatedAt(), notification.getStatus());

    }

    public static Notification toModel(@NonNull NotificationEntity notificationEntity) {
        List<Victim> victims = notificationEntity.getVictims().stream()
                .map(VictimMapper::toModel).toList();
        List<AllocatedResource> allocatedResources = notificationEntity.getResources().stream()
                .map(AllocatedResourceMapper::toModel).toList();
        if (notificationEntity.getLocation() == null) {
            return new Notification(notificationEntity.getId(), notificationEntity.getDescription(), victims,
                    null, allocatedResources, notificationEntity.getStatus(),
                    notificationEntity.getCreatedAt());
        }
        return new Notification(notificationEntity.getId(), notificationEntity.getDescription(), victims,
                LocationMapper.toModel(notificationEntity.getLocation()), allocatedResources,
                notificationEntity.getStatus(), notificationEntity.getCreatedAt());
    }

    public static Notification toModelForResources(@NonNull NotificationEntity notificationEntity) {
        List<Victim> victims = notificationEntity.getVictims().stream()
                .map(VictimMapper::toModel).toList();
        List<AllocatedResource> allocatedResources = new ArrayList<>();
        if (notificationEntity.getLocation() == null) {
            return new Notification(notificationEntity.getId(), notificationEntity.getDescription(), victims,
                    null, allocatedResources, notificationEntity.getStatus(),
                    notificationEntity.getCreatedAt());
        }
        return new Notification(notificationEntity.getId(), notificationEntity.getDescription(), victims,
                LocationMapper.toModel(notificationEntity.getLocation()), allocatedResources,
                notificationEntity.getStatus(), notificationEntity.getCreatedAt());
    }
}
