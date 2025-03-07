package pl.lodz.coordinationsystem.communication.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.coordinationsystem.communication.entity.MessageStatusEntity;
import pl.lodz.coordinationsystem.communication.model.Message;
import pl.lodz.coordinationsystem.communication.model.MessageStatus;
import pl.lodz.coordinationsystem.communication.repository.MessageRepository;
import pl.lodz.coordinationsystem.communication.repository.MessageStatusRepository;
import pl.lodz.coordinationsystem.security.entity.AdminEntity;
import pl.lodz.coordinationsystem.security.model.Role;
import pl.lodz.coordinationsystem.security.repositories.BaseUserRepository;
import pl.lodz.coordinationsystem.security.services.ISecurity;
import pl.lodz.coordinationsystem.security.services.RoleMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IActorsTest {

    @Autowired
    IActors actorsService;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessageStatusRepository messageStatusRepository;

    @Autowired
    ISecurity securityService;

    @Autowired
    BaseUserRepository userRepository;

    Role role;
    AdminEntity user;

    @BeforeEach
    void setUp() {
        role = securityService.getRoleByName("ADMIN").orElseThrow();

        user = new AdminEntity(null, "John", "Doe", "john.doe@example.com",
                "password", true, LocalDateTime.now(), RoleMapper.toEntity(role), "IT", 1);
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        messageStatusRepository.deleteAll();
        messageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void addMessageForRole() {
        Message message = new Message(role, "Test Subject", "Test Content");
        Long messageId = actorsService.addMessageForRole(message);
        assertNotNull(messageId);
        assertTrue(messageRepository.findById(messageId).isPresent());

        List<MessageStatusEntity> statusEntities = messageStatusRepository.findByMessage(messageRepository.findById(messageId).get());
        assertEquals(1, statusEntities.size());
        assertEquals(MessageStatus.UNREAD, statusEntities.get(0).getStatus());
    }

    @Test
    void addMessageForRoleThrowsExceptionWhenRoleNotFound() {
        Message message = new Message(new Role(null, "ROLE_NON_EXISTENT", null), "Test Subject", "Test Content");
        assertThrows(IllegalArgumentException.class, () -> actorsService.addMessageForRole(message));
    }

    @Test
    void readMessagesForUser() {
        Message message = new Message(role, "Test Subject", "Test Content");
        actorsService.addMessageForRole(message);
        List<Message> messages = actorsService.readMessagesForUser(user.getId());
        assertEquals(1, messages.size());
        assertEquals("Test Subject", messages.get(0).getSubject());
    }

    @Test
    void readMessagesForUserReturnsEmptyListWhenNoMessages() {
        List<Message> messages = actorsService.readMessagesForUser(user.getId());
        assertTrue(messages.isEmpty());
    }

    @Test
    void updateMessageStatus() {
        Message message = new Message(role, "Test Subject", "Test Content");
        Long messageId = actorsService.addMessageForRole(message);

        actorsService.updateMessageStatus(messageId, user.getId(), MessageStatus.READ);

        Optional<MessageStatusEntity> statusEntity = messageStatusRepository.findByMessageAndUser(messageRepository.findById(messageId).get(), user);
        assertTrue(statusEntity.isPresent());
        assertEquals(MessageStatus.READ, statusEntity.get().getStatus());
    }
}