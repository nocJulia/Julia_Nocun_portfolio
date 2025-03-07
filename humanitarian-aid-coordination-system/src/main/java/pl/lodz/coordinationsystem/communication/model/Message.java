package pl.lodz.coordinationsystem.communication.model;

import pl.lodz.coordinationsystem.security.model.Role;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private Role role;
    private String subject;
    private String content;
    private LocalDateTime date;

    public Message() {
    }

    public Message(Role role, String subject, String content) {
        this.role = role;
        this.subject = subject;
        this.content = content;
        this.date = LocalDateTime.now();
    }

    public Message(Long id, Role role, String subject, String content, LocalDateTime date) {
        this.id = id;
        this.role = role;
        this.subject = subject;
        this.content = content;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
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