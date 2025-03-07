package pl.lodz.coordinationsystem.resource.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.lodz.coordinationsystem.resource.entity.VehicleEntity;
import pl.lodz.coordinationsystem.resource.entity.VehicleType;

import java.util.List;

public interface VehicleRepository extends CrudRepository<VehicleEntity, Long> {

    List<VehicleEntity> findByisAvailable(boolean isAvailable);

    @Transactional
    @Modifying
    @Query("UPDATE VehicleEntity v SET v.isAvailable = :isAvailable WHERE v.id = :id")
    int setAvailability(Long id, boolean isAvailable);

}
