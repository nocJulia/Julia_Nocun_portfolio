package pl.lodz.coordinationsystem.resource.service;

import pl.lodz.coordinationsystem.donation.model.Donation;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.resource.entity.VehicleEntity;
import pl.lodz.coordinationsystem.resource.model.AllocatedResource;
import pl.lodz.coordinationsystem.resource.model.Logistician;

import java.util.List;
import java.util.Optional;

public interface IResources {
    Long allocateResource(Donation donation, Notification notification, Integer quantity, VehicleEntity vehicle);
    Long createLogistician(Logistician logistician);
    List<Logistician> getAllLogisticians();
    List<AllocatedResource> getAllAllocatedResources();
    Optional<AllocatedResource> getAllocatedResourceById(Long id);
    void removeAllocatedResourceById(Long id);
    void setLocationTologistician(Long id, Double latitude, Double longitude);
    Optional<Logistician> getLogisticianById(Long id);
    void removeLogisticianById(Long id);
}
