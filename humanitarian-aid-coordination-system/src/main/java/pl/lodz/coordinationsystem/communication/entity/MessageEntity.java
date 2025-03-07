package pl.lodz.coordinationsystem.communication.entity;

import jakarta.persistence.*;
import pl.lodz.coordinationsystem.security.entity.RoleEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    public MessageEntity() {
    }

    public MessageEntity(Long id, RoleEntity role, String subject, String content, LocalDateTime date) {
        this.id = id;
        this.role = role;
        this.subject = subject;
        this.content = content;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}