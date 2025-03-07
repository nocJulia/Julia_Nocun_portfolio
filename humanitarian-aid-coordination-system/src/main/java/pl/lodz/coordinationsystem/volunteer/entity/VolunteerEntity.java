package pl.lodz.coordinationsystem.volunteer.entity;

import jakarta.persistence.*;
import pl.lodz.coordinationsystem.maps.entity.LocationEntity;
import pl.lodz.coordinationsystem.notification.entity.NotificationEntity;
import pl.lodz.coordinationsystem.security.entity.BaseUserEntity;
import pl.lodz.coordinationsystem.security.entity.RoleEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "volunteers")
@DiscriminatorValue("VOLUNTEER")
public class VolunteerEntity extends BaseUserEntity {
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "skills")
    private String skills;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @ManyToMany
    @JoinTable(
            name = "assigned_volunteers",
            joinColumns = @JoinColumn(name = "volunteer_id"),
            inverseJoinColumns = @JoinColumn(name = "notification_id")
    )
    private List<NotificationEntity> notifications;


    public VolunteerEntity() {}

    public VolunteerEntity(Long id, String firstName, String lastName, String email, String password, Boolean active,
                           LocalDateTime createdAt, RoleEntity role, String phoneNumber, String skills,
                           LocationEntity location, List<NotificationEntity> notifications) {
        super(id, firstName, lastName, email, password, active, createdAt, role);
        this.phoneNumber = phoneNumber;
        this.skills = skills;
        this.location = location;
        this.notifications = notifications;
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

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public List<NotificationEntity> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationEntity> notifications) {
        this.notifications = notifications;
    }
}