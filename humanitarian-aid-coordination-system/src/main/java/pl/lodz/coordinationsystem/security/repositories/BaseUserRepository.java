package pl.lodz.coordinationsystem.security.repositories;

import pl.lodz.coordinationsystem.security.entity.BaseUserEntity;
import pl.lodz.coordinationsystem.security.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaseUserRepository extends CrudRepository<BaseUserEntity, Long> {
    Optional<BaseUserEntity> findByEmail(String email);
    List<BaseUserEntity> findByRole(RoleEntity role);
}
