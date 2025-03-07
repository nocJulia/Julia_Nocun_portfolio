package pl.lodz.coordinationsystem.communication.entity;

import jakarta.persistence.*;
import pl.lodz.coordinationsystem.communication.model.MessageStatus;
import pl.lodz.coordinationsystem.security.entity.BaseUserEntity;

@Entity
@Table(name = "message_status")
public class MessageStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MessageStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private BaseUserEntity user;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private MessageEntity message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public BaseUserEntity getUser() {
        return user;
    }

    public void setUser(BaseUserEntity user) {
        this.user = user;
    }

    public MessageEntity getMessage() {
        return message;
    }

    public void setMessage(MessageEntity message) {
        this.message = message;
    }
}