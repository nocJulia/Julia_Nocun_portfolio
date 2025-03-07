package pl.lodz.coordinationsystem.resource.service;

import org.springframework.stereotype.Service;
import pl.lodz.coordinationsystem.resource.entity.VehicleEntity;
import pl.lodz.coordinationsystem.resource.entity.VehicleType;
import pl.lodz.coordinationsystem.resource.model.Vehicle;
import pl.lodz.coordinationsystem.resource.repository.VehicleRepository;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }


    public void createVehicle(Vehicle vehicle) {

        vehicleRepository.save(new VehicleEntity(vehicle.getRegistryNumber(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getVehicleType(),
                vehicle.getBootCapacity(),
                true));
    }
    public List<VehicleEntity> getAllVehicles() {
        return vehicleRepository.findByisAvailable(true);
    }
    public VehicleEntity getVehicleById(long id) {
        return vehicleRepository.findById(id).get();
    }
    public void setAvailable(Long id, boolean isAvailable){
        vehicleRepository.setAvailability(id, isAvailable);
    }

}
