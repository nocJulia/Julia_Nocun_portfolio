package pl.lodz.coordinationsystem.security.services;

import org.springframework.lang.NonNull;
import pl.lodz.coordinationsystem.security.entity.RoleEntity;
import pl.lodz.coordinationsystem.security.model.Role;

public class RoleMapper {
    public static RoleEntity toEntity(@NonNull Role role) {
        return new RoleEntity(role.getId(), role.getRoleName(), role.getDescription());
    }

    public static Role toModel(@NonNull RoleEntity roleEntity) {
        return new Role(roleEntity.getId(), roleEntity.getRoleName(), roleEntity.getDescription());
    }
}
