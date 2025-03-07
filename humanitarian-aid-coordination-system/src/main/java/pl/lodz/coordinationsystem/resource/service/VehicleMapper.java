package pl.lodz.coordinationsystem.resource.service;

import org.springframework.lang.NonNull;
import pl.lodz.coordinationsystem.resource.entity.VehicleEntity;
import pl.lodz.coordinationsystem.resource.model.Vehicle;

public class VehicleMapper {
    public static VehicleEntity toEntity(@NonNull Vehicle vehicle) {
        return new VehicleEntity(vehicle.getRegistryNumber(), vehicle.getBrand(), vehicle.getModel(),
                vehicle.getVehicleType(), vehicle.getBootCapacity(), vehicle.isAvailable());
    }

    public static Vehicle toModel(@NonNull VehicleEntity vehicle) {
        return new Vehicle(vehicle.getRegistryNumber(), vehicle.getBrand(), vehicle.getModel(),
                vehicle.getVehicleType(), vehicle.getBootCapacity(), vehicle.isAvailable());

    }
}
