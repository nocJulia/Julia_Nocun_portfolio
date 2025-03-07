package pl.lodz.coordinationsystem.resource.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.coordinationsystem.resource.entity.AllocatedResourceEntity;

@Repository
public interface AllocatedResourceRepository extends CrudRepository<AllocatedResourceEntity, Long> {
}
