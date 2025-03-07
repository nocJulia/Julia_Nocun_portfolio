package pl.lodz.coordinationsystem.communication.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.coordinationsystem.communication.entity.MessageEntity;
import pl.lodz.coordinationsystem.communication.entity.MessageStatusEntity;
import pl.lodz.coordinationsystem.communication.model.Message;
import pl.lodz.coordinationsystem.communication.model.MessageStatus;
import pl.lodz.coordinationsystem.communication.repository.MessageRepository;
import pl.lodz.coordinationsystem.communication.repository.MessageStatusRepository;
import pl.lodz.coordinationsystem.security.entity.BaseUserEntity;
import pl.lodz.coordinationsystem.security.repositories.BaseUserRepository;
import pl.lodz.coordinationsystem.security.services.RoleMapper;
import pl.lodz.coordinationsystem.security.services.ISecurity;
import pl.lodz.coordinationsystem.security.model.User;
import pl.lodz.coordinationsystem.security.entity.RoleEntity;
import pl.lodz.coordinationsystem.security.repositories.RoleRepository;
import pl.lodz.coordinationsystem.security.services.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActorsService implements IActors {

    private final MessageRepository messageRepository;
    private final MessageStatusRepository messageStatusRepository;
    private final RoleRepository roleRepository;
    private final BaseUserRepository baseUserRepository;

    public ActorsService(MessageRepository messageRepository, MessageStatusRepository messageStatusRepository, RoleRepository roleRepository, BaseUserRepository baseUserRepository) {
        this.messageRepository = messageRepository;
        this.messageStatusRepository = messageStatusRepository;
        this.roleRepository = roleRepository;
        this.baseUserRepository = baseUserRepository;
    }

    @Override
    @Transactional
    public Long addMessageForRole(Message message) {
        RoleEntity roleEntity = roleRepository.findByRoleName(message.getRole().getRoleName());
        if (roleEntity == null) {
            throw new IllegalArgumentException("Role not found: " + message.getRole().getRoleName());
        }

        MessageEntity messageEntity = new MessageEntity(
                null,
                roleEntity,
                message.getSubject(),
                message.getContent(),
                message.getDate()
        );
        messageEntity = messageRepository.save(messageEntity);

        List<BaseUserEntity> users = baseUserRepository.findByRole(roleEntity);
        for (BaseUserEntity user : users) {
            MessageStatusEntity messageStatusEntity = new MessageStatusEntity();
            messageStatusEntity.setMessage(messageEntity);
            messageStatusEntity.setUser(user);
            messageStatusEntity.setStatus(MessageStatus.UNREAD);
            messageStatusRepository.save(messageStatusEntity);
        }

        return messageEntity.getId();
    }

    @Override
    public List<Message> readMessagesForUser(Long id) {
        Optional<BaseUserEntity> userOptional = baseUserRepository.findById(id);
        if (userOptional.isPresent()) {
            List<MessageStatusEntity> messageStatusEntities = messageStatusRepository.findByUser(userOptional.get());
            return messageStatusEntities.stream()
                    .map(ms -> MessageMapper.toModel(ms.getMessage()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Transactional
    public void updateMessageStatus(Long messageId, Long userId, MessageStatus status) {
        Optional<MessageEntity> messageEntityOptional = messageRepository.findById(messageId);
        if (messageEntityOptional.isPresent()) {
            MessageEntity messageEntity = messageEntityOptional.get();
            Optional<BaseUserEntity> userOptional = baseUserRepository.findById(userId);
            if (userOptional.isPresent()) {
                BaseUserEntity user = userOptional.get();
                Optional<MessageStatusEntity> messageStatusEntityOptional = messageStatusRepository.findByMessageAndUser(messageEntity, user);
                if (messageStatusEntityOptional.isPresent()) {
                    MessageStatusEntity messageStatusEntity = messageStatusEntityOptional.get();
                    messageStatusEntity.setStatus(status);
                    messageStatusRepository.save(messageStatusEntity);
                } else {
                    throw new IllegalArgumentException("Message status not found for user: " + user.getId());
                }
            } else {
                throw new IllegalArgumentException("User not found: " + userId);
            }
        } else {
            throw new IllegalArgumentException("Message not found: " + messageId);
        }
    }

    @Transactional
    public Message getMessageById(Long messageId) {
        MessageEntity messageEntity = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));
        return MessageMapper.toModel(messageEntity);
    }

    @Transactional
    public List<MessageStatusEntity> readMessageStatusesForUser(Long userId) {
        Optional<BaseUserEntity> userOptional = baseUserRepository.findById(userId);
        if (userOptional.isPresent()) {
            return messageStatusRepository.findByUser(userOptional.get());
        }
        return new ArrayList<>();
    }
}