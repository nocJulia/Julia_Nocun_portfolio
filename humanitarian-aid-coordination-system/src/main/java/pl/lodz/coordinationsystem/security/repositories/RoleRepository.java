package pl.lodz.coordinationsystem.security.repositories;

import pl.lodz.coordinationsystem.security.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    RoleEntity findByRoleName(String roleName);
}
