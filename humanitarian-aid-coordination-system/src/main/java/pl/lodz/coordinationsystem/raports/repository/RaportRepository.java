package pl.lodz.coordinationsystem.raports.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.coordinationsystem.raports.entity.ReportInfoEntity;

import java.util.Date;
import java.util.List;

@Repository
public interface RaportRepository extends CrudRepository<ReportInfoEntity, Long> {
    List<ReportInfoEntity> findByGenerationDate(Date generationDate );
    List<ReportInfoEntity> findByAuthorityId(Long authorityId);
}
