package pl.lodz.coordinationsystem.security.services;

import org.springframework.lang.NonNull;
import pl.lodz.coordinationsystem.security.entity.BaseUserEntity;
import pl.lodz.coordinationsystem.security.model.User;

public class UserMapper {
    public static User toModel(@NonNull BaseUserEntity user) {
        return new User(
                user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(),
                RoleMapper.toModel(user.getRole()), user.getActive(), user.getCreatedAt()
        );
    }
}
