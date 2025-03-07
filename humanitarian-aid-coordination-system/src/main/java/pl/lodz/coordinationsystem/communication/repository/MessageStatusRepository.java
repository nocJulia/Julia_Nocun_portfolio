package pl.lodz.coordinationsystem.communication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.coordinationsystem.communication.entity.MessageEntity;
import pl.lodz.coordinationsystem.communication.entity.MessageStatusEntity;
import pl.lodz.coordinationsystem.communication.model.MessageStatus;
import pl.lodz.coordinationsystem.security.entity.BaseUserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageStatusRepository extends CrudRepository<MessageStatusEntity, Long> {
    List<MessageStatusEntity> findByUser(BaseUserEntity user);
    List<MessageStatusEntity> findByUserAndStatus(BaseUserEntity user, MessageStatus status);
    List<MessageStatusEntity> findByMessage(MessageEntity message);
    Optional<MessageStatusEntity> findByMessageAndUser(MessageEntity message, BaseUserEntity user);
}