package pl.lodz.coordinationsystem.volunteer.service;

import pl.lodz.coordinationsystem.maps.model.Location;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.volunteer.model.Volunteer;

import java.util.List;
import java.util.Optional;

/**
 * Interface for services related to volunteer operations.
 */
public interface IVolunteer {

    /**
     * Creates a new volunteer and returns their ID.
     *
     * @param volunteer the volunteer to be created
     * @return the ID of the newly created volunteer
     */
    Long createVolunteer(Volunteer volunteer);

    /**
     * Updates the location of a volunteer.
     *
     * @param volunteerId the ID of the volunteer
     * @param location the new location of the volunteer
     */
    void setVolunteerLocation(Long volunteerId, Location location);

    /**
     * Sets the location of a volunteer by their ID and the ID of the location.
     *
     * @param volunteerId the ID of the volunteer
     * @param locationId the ID of the location
     */
    void setVolunteerLocationById(Long volunteerId, Long locationId);

    /**
     * Assigns a notification to a volunteer.
     *
     * @param volunteerId the ID of the volunteer
     * @param notification the notification to be assigned
     */
    void assignNotificationToVolunteer(Long volunteerId, Notification notification);

    /**
     * Returns a list of all volunteers.
     *
     * @return a list of all volunteers
     */
    List<Volunteer> findAllVolunteers();

    /**
     * Returns a volunteer by their ID.
     *
     * @param id the ID of the volunteer
     * @return an Optional containing the volunteer if they exist
     */
    Optional<Volunteer> findVolunteerById(Long id);

    /**
     * Removes a notification assigned to a volunteer by the notification ID.
     *
     * @param volunteerId the ID of the volunteer
     * @param notification the ID of the notification to be removed
     */
    void removeNotificationFromVolunteerById(Long volunteerId, Notification notification);

    /**
     * Filters volunteers by their status (e.g., BUSY or AVAILABLE).
     *
     * @param volunteers the list of volunteers to filter
     * @param status the status to filter by
     * @return the filtered list of volunteers
     */
    List<Volunteer> filterByStatus(List<Volunteer> volunteers, String status);

    /**
     * Filters volunteers by their state (e.g., ACTIVE or ARCHIVED).
     *
     * @param volunteers the list of volunteers to filter
     * @param state the state to filter by
     * @return the filtered list of volunteers
     */
    List<Volunteer> filterByState(List<Volunteer> volunteers, String state);

    /**
     * Returns notifications that are not assigned to the given volunteer.
     *
     * @param volunteer the volunteer
     * @return the list of unassigned notifications
     */
    List<Notification> getAvailableNotificationsForVolunteer(Volunteer volunteer);

    /**
     * Updates the details of a volunteer by their ID.
     *
     * @param volunteerId the ID of the volunteer to update
     * @param updatedVolunteer the updated details of the volunteer
     */
    void updateVolunteerById(Long volunteerId, Volunteer updatedVolunteer);

    /**
     * Updates the password of a volunteer.
     *
     * @param volunteerId the ID of the volunteer whose password is to be updated
     * @param newPassword the new password to be set for the volunteer
     */
    void updatePassword(Long volunteerId, String newPassword);

    /**
     * Assigns a notification to a volunteer by its ID.
     *
     * @param volunteerId the ID of the volunteer
     * @param notificationId the ID of the notification
     */
    void addNotification(Long volunteerId, Long notificationId);

    /**
     * Removes a notification from a volunteer by its ID.
     *
     * @param volunteerId the ID of the volunteer
     * @param notificationId the ID of the notification to remove
     */
    void removeNotification(Long volunteerId, Long notificationId);
}