package pl.lodz.coordinationsystem.resource.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.lodz.coordinationsystem.resource.entity.TransportEntity;

import java.util.List;

public interface TransportRepository extends CrudRepository<TransportEntity, Long> {
    @Query("SELECT t FROM TransportEntity t where t.endTime is null")
    List<TransportEntity> getAllTransportsRunning();

    @Modifying
    @Transactional
    @Query("UPDATE TransportEntity t SET t.endTime = CURRENT_TIMESTAMP WHERE t.id = :id AND t.endTime IS NULL")
    void setEndDateToNowIfNotSet(@Param("id") Long id);

}
