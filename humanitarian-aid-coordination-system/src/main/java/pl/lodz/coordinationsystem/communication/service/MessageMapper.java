package pl.lodz.coordinationsystem.communication.service;

import org.springframework.lang.NonNull;
import pl.lodz.coordinationsystem.communication.entity.MessageEntity;
import pl.lodz.coordinationsystem.communication.model.Message;
import pl.lodz.coordinationsystem.security.services.RoleMapper;

public class MessageMapper {
    public static MessageEntity toEntity(@NonNull Message message) {
        return new MessageEntity(
                message.getId(), RoleMapper.toEntity(message.getRole()),
                message.getSubject(), message.getContent(), message.getDate()
        );
    }

    public static Message toModel(@NonNull MessageEntity messageEntity) {
        return new Message(
                messageEntity.getId(), RoleMapper.toModel(messageEntity.getRole()),
                messageEntity.getSubject(), messageEntity.getContent(), messageEntity.getDate()
        );
    }
}