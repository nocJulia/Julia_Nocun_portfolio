package pl.lodz.coordinationsystem.notification.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.coordinationsystem.notification.entity.NotificationEntity;
import pl.lodz.coordinationsystem.notification.model.NotificationStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<NotificationEntity, Long> {
    List<NotificationEntity> findNotificationEntitiesByCreatedAtGreaterThanAndCreatedAtLessThan(
            LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan);

    List<NotificationEntity> findAllByStatus(NotificationStatus status);
}
