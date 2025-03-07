package pl.lodz.coordinationsystem.notification.service;

import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.notification.model.NotificationStatus;
import pl.lodz.coordinationsystem.notification.model.Victim;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface for services related to notification operations.
 */
public interface INotifications {

    /**
     * Returns a list of all notifications.
     *
     * @return a list of all notifications
     */
    List<Notification> findAllNotifications();

    /**
     * Returns a notification by its ID.
     *
     * @param id the ID of the notification
     * @return an Optional containing the notification if it exists
     */
    Optional<Notification> findNotificationById(Long id);

    /**
     * Assigns a new victim to a notification with the specified ID.
     *
     * @param notificationId the ID of the notification
     * @param victim the victim to be assigned to the notification
     */
    void assignNewVictimToNotification(Long notificationId, Victim victim);

    /**
     * Removes a victim from a notification by the victim's ID.
     *
     * @param notificationId the ID of the notification
     * @param victimId the ID of the victim to be removed
     */
    void removeVictimFromNotificationById(Long notificationId, Long victimId);

    /**
     * Creates a new notification and returns its ID.
     *
     * @param notification the notification to be created
     * @return the ID of the newly created notification
     */
    Long createNewNotification(Notification notification);


    void setLocationToNotification(Long id, Double latitude, Double longitude);

    /**
     * Returns a list of notifications with the specified status.
     *
     * @param status the status of the notifications to be returned
     * @return a list of notifications with the specified status
     */
    List<Notification> findNotificationsByStatus(NotificationStatus status);

    /**
     * Returns a list of notifications created within the specified date range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a list of notifications created within the specified date range
     */
    List<Notification> findNotificationsByCreationDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Updates the status of a notification with the specified ID.
     *
     * @param id the ID of the notification
     * @param status the new status to be set
     */
    void updateNotificationStatus(Long id, NotificationStatus status);

    /**
     * Deletes a notification by its ID.
     *
     * @param id the ID of the notification to be deleted
     */
    void deleteNotification(Long id);

    /**
     * Updates the description of a notification with the specified ID.
     *
     * @param id the ID of the notification
     * @param description the new description to be set
     */
    void updateNotificationDescription(Long id, String description);
}

