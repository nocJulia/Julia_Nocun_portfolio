package pl.lodz.coordinationsystem.resource.service;

import org.springframework.lang.NonNull;
import pl.lodz.coordinationsystem.maps.services.LocationMapper;
import pl.lodz.coordinationsystem.resource.entity.LogisticianEntity;
import pl.lodz.coordinationsystem.resource.model.Logistician;
import pl.lodz.coordinationsystem.security.services.RoleMapper;

public class LogisticianMapper {
    public static LogisticianEntity toEntity(@NonNull Logistician logistician) {
        return new LogisticianEntity(
                logistician.getId(),
                logistician.getFirstName(),
                logistician.getLastName(),
                logistician.getEmail(),
                logistician.getPassword(),
                logistician.getActive(),
                logistician.getCreatedAt(),
                RoleMapper.toEntity(logistician.getRole()),
                LocationMapper.toEntity(logistician.getLocation())
                );
    }
    public static Logistician toModel(@NonNull LogisticianEntity logisticianEntity) {
        return new Logistician(
                logisticianEntity.getId(),
                logisticianEntity.getFirstName(),
                logisticianEntity.getLastName(),
                logisticianEntity.getEmail(),
                logisticianEntity.getPassword(),
                RoleMapper.toModel(logisticianEntity.getRole()),
                logisticianEntity.getActive(),
                logisticianEntity.getCreatedAt(),
                LocationMapper.toModel(logisticianEntity.getLocation())
        );
    }
}
