package pl.lodz.coordinationsystem.resource.entity;

import jakarta.persistence.*;
import pl.lodz.coordinationsystem.resource.model.Vehicle;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transports")
public class TransportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private VehicleEntity vehicle;

    @ManyToOne
    @JoinColumn(name = "allocated_resource_id")
    private AllocatedResourceEntity allocatedResourceEntity;


    public TransportEntity(LocalDateTime startTime, LocalDateTime endTime, VehicleEntity vehicleEntity, AllocatedResourceEntity allocatedResourceEntity) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.vehicle = vehicleEntity;
        this.allocatedResourceEntity = allocatedResourceEntity;
    }

    public TransportEntity() {
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public AllocatedResourceEntity getAllocatedResourceEntity() {
        return allocatedResourceEntity;
    }

    public void setAllocatedResourceEntity(AllocatedResourceEntity allocatedResourceEntity) {
        this.allocatedResourceEntity = allocatedResourceEntity;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }
}
