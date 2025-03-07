package pl.lodz.coordinationsystem.donation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.coordinationsystem.donation.entity.DonationEntity;
import pl.lodz.coordinationsystem.donation.model.DonationStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonationRepository extends CrudRepository<DonationEntity, Long> {
    List<DonationEntity> findAllByDonorId(Long donorId);
    List<DonationEntity> findAllByStatus(DonationStatus status);
    List<DonationEntity> findAllByDonationDateGreaterThanAndDonationDateLessThan(
            LocalDateTime donationDateIsGreaterThan, LocalDateTime donationDateIsLessThan);
}
