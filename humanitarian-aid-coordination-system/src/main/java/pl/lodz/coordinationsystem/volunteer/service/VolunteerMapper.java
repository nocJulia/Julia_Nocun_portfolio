package pl.lodz.coordinationsystem.volunteer.service;

import org.springframework.lang.NonNull;
import pl.lodz.coordinationsystem.maps.entity.LocationEntity;
import pl.lodz.coordinationsystem.notification.entity.NotificationEntity;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.notification.service.NotificationMapper;
import pl.lodz.coordinationsystem.security.services.RoleMapper;
import pl.lodz.coordinationsystem.volunteer.entity.VolunteerEntity;
import pl.lodz.coordinationsystem.volunteer.model.Volunteer;
import pl.lodz.coordinationsystem.maps.model.Location;
import pl.lodz.coordinationsystem.maps.services.LocationMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class VolunteerMapper {
    public static VolunteerEntity toEntity(@NonNull Volunteer volunteer) {
        List<NotificationEntity> notificationEntities = Optional.ofNullable(volunteer.getNotifications())
                .orElse(Collections.emptyList())
                .stream()
                .map(NotificationMapper::toEntity)
                .toList();

        LocationEntity locationEntity = Optional.ofNullable(volunteer.getLocation())
                .map(LocationMapper::toEntity)
                .orElse(null);

        return new VolunteerEntity(volunteer.getId(), volunteer.getFirstName(), volunteer.getLastName(),
                volunteer.getEmail(), volunteer.getPassword(), volunteer.getActive(), volunteer.getCreatedAt(),
                RoleMapper.toEntity(volunteer.getRole()), volunteer.getPhoneNumber(), volunteer.getSkills(),
                locationEntity, notificationEntities);
    }

    public static Volunteer toModel(@NonNull VolunteerEntity volunteerEntity) {

        List<Notification> notifications = Optional.ofNullable(volunteerEntity.getNotifications())
                .orElse(Collections.emptyList())
                .stream()
                .map(NotificationMapper::toModel)
                .toList();

        Location location = Optional.ofNullable(volunteerEntity.getLocation())
                .map(LocationMapper::toModel)
                .orElse(null);

        return new Volunteer(volunteerEntity.getId(), volunteerEntity.getFirstName(), volunteerEntity.getLastName(),
                volunteerEntity.getEmail(), volunteerEntity.getPassword(), RoleMapper.toModel(volunteerEntity.getRole()),
                volunteerEntity.getActive(), volunteerEntity.getCreatedAt(), volunteerEntity.getPhoneNumber(),
                volunteerEntity.getSkills(), notifications, location);
    }
}