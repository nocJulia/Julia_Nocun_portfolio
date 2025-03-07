package pl.lodz.coordinationsystem.volunteer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.coordinationsystem.volunteer.entity.VolunteerEntity;

@Repository
public interface VolunteerRepository extends CrudRepository<VolunteerEntity, Long> {

}
