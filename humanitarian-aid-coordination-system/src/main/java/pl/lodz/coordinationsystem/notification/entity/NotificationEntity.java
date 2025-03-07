package pl.lodz.coordinationsystem.notification.entity;

import jakarta.persistence.*;
import pl.lodz.coordinationsystem.maps.entity.LocationEntity;
import pl.lodz.coordinationsystem.notification.model.NotificationStatus;
import pl.lodz.coordinationsystem.resource.entity.AllocatedResourceEntity;
import pl.lodz.coordinationsystem.volunteer.entity.VolunteerEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description", nullable = false, length = 2000)
    private String description;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "notification_id")
    private List<VictimEntity> victims;
    @OneToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location;
    @OneToMany()
    @JoinColumn(name = "notification_id")
    private List<AllocatedResourceEntity> resources;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    public NotificationEntity(Long id, String description, List<VictimEntity> victims, LocationEntity location,
                              LocalDateTime createdAt, NotificationStatus status) {
        this.id = id;
        this.description = description;
        this.victims = victims;
        this.location = location;
        this.createdAt = createdAt;
        this.status = status;
    }

    public NotificationEntity() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<VictimEntity> getVictims() {
        return victims;
    }

    public void setVictims(List<VictimEntity> victims) {
        this.victims = victims;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public List<AllocatedResourceEntity> getResources() {
        return resources;
    }

    public void setResources(List<AllocatedResourceEntity> resources) {
        this.resources = resources;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime date) {
        this.createdAt = date;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }
}
