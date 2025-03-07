package pl.lodz.coordinationsystem.resource.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.coordinationsystem.resource.entity.LogisticianEntity;

@Repository
public interface LogisticianRepository extends CrudRepository<LogisticianEntity, Long> {
}
