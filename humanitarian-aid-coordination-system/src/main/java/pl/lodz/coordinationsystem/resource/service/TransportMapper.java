package pl.lodz.coordinationsystem.resource.service;

import org.springframework.lang.NonNull;
import pl.lodz.coordinationsystem.resource.entity.TransportEntity;
import pl.lodz.coordinationsystem.resource.model.Transport;

public class TransportMapper {

    public static TransportEntity toEntity(@NonNull Transport transport) {
        return new TransportEntity(transport.getStartTime(), transport.getEndTime(),
                VehicleMapper.toEntity(transport.getVehicle()),
                AllocatedResourceMapper.toEntity(transport.getAllocatedResource()));
    }

    public static Transport toModel(@NonNull TransportEntity transportEntity) {
        return new Transport(transportEntity.getStartTime(), transportEntity.getEndTime(), VehicleMapper.toModel(
                transportEntity.getVehicle()), null);

    }

}
