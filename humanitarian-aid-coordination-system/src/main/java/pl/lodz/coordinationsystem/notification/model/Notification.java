package pl.lodz.coordinationsystem.notification.model;

import pl.lodz.coordinationsystem.maps.model.Location;
import pl.lodz.coordinationsystem.resource.model.AllocatedResource;

import java.time.LocalDateTime;
import java.util.List;

public class Notification {
    private Long id;
    private String description;
    private List<Victim> victims;
    private Location location;
    private List<AllocatedResource> resources;;
    private NotificationStatus status;
    private LocalDateTime createdAt;

    public Notification(Long id, String description, List<Victim> victims, Location location,
                        List<AllocatedResource> resources,
                        NotificationStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.description = description;
        this.victims = victims;
        this.location = location;
        this.resources = resources;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Notification(String description, List<Victim> victims) {
        this.description = description;
        this.victims = victims;
        status = NotificationStatus.PENDING;
        createdAt = LocalDateTime.now();
    }

    public Notification() {
        status = NotificationStatus.PENDING;
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Victim> getVictims() {
        return victims;
    }

    public void setVictims(List<Victim> victims) {
        this.victims = victims;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<AllocatedResource> getResources() {
        return resources;
    }

    public void setResources(List<AllocatedResource> resources) {
        this.resources = resources;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", victims=" + victims +
                ", location=" + location +
                ", resources=" + resources +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
