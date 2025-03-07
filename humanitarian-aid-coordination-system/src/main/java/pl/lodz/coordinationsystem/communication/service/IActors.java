package pl.lodz.coordinationsystem.communication.service;

import pl.lodz.coordinationsystem.communication.entity.MessageStatusEntity;
import pl.lodz.coordinationsystem.communication.model.Message;
import pl.lodz.coordinationsystem.communication.model.MessageStatus;

import java.util.List;

/**
 * Interface for services related to actor operations.
 */
public interface IActors {

    /**
     * Adds a message for users with a specific role.
     *
     * @param message the message to be added
     */
    Long addMessageForRole(Message message);

    /**
     * Reads messages for a user with a given ID.
     *
     * @param id the id of the user
     * @return a list of messages for the user
     */
    List<Message> readMessagesForUser(Long id);
    /**
     * Updates the status of a message for the current user.
     *
     * @param messageId the id of the message
     * @param status the new status of the message
     */
    void updateMessageStatus(Long messageId, Long userId, MessageStatus status);
    /**
     * Gets a message by its ID.
     *
     * @param messageId the id of the message
     * @return the message with the given ID
     */
    Message getMessageById(Long messageId);
    /**
     * Reads message statuses for a user with a given ID.
     *
     * @param userId the id of the user
     * @return a list of message statuses for the user
     */
    List<MessageStatusEntity> readMessageStatusesForUser(Long userId);
}