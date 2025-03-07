package pl.lodz.coordinationsystem.volunteer.model;

import pl.lodz.coordinationsystem.maps.model.Location;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.security.model.Role;
import pl.lodz.coordinationsystem.security.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class Volunteer extends User {
    private String phoneNumber;
    private String skills;
    private List<Notification> notifications;
    private Location location;

    public Volunteer(Long id, String firstName, String lastName, String email, String password, Role role,
                     Boolean active, LocalDateTime createdAt, String phoneNumber, String skills,
                     List<Notification> notifications, Location location) {
        super(id, firstName, lastName, email, password, role, active, createdAt);
        this.phoneNumber = phoneNumber;
        this.skills = skills;
        this.notifications = notifications;
        this.location = location;
    }

    public Volunteer(String firstName, String lastName, String email){
        super(firstName, lastName, email);
        setActive(true);
    }

    public Volunteer() {
        setActive(true);
    }

    public String getStatus() {
        return notifications != null && !notifications.isEmpty() ? "BUSY" : "AVAILABLE";
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}