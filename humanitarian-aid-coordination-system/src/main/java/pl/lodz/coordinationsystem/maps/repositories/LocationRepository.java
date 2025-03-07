package pl.lodz.coordinationsystem.maps.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.lodz.coordinationsystem.maps.entity.LocationEntity;

public interface LocationRepository extends CrudRepository<LocationEntity, Long> {
}
