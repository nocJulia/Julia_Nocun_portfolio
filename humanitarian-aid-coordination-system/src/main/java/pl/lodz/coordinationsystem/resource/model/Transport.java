package pl.lodz.coordinationsystem.resource.model;

import jakarta.persistence.Column;
import pl.lodz.coordinationsystem.resource.entity.AllocatedResourceEntity;
import pl.lodz.coordinationsystem.resource.entity.VehicleType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Transport {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Vehicle vehicle;
    private AllocatedResource allocatedResource;

    public Transport(LocalDateTime startTime, LocalDateTime endTime, Vehicle vehicle,  AllocatedResource allocatedResources) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.vehicle = vehicle;
        this.allocatedResource = allocatedResources;
    }

    public Transport() {
    }

    public int getId() {
        return id;
    }

    public AllocatedResource getAllocatedResource() {
        return allocatedResource;
    }

    public void setAllocatedResources(AllocatedResource allocatedResource) {
        this.allocatedResource = allocatedResource;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setAllocatedResource(AllocatedResource allocatedResource) {
        this.allocatedResource = allocatedResource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transport transport = (Transport) o;

        if (id != transport.id) return false;
        if (!Objects.equals(startTime, transport.startTime)) return false;
        return Objects.equals(endTime, transport.endTime);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }
}
