package pl.lodz.coordinationsystem.donation.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lodz.coordinationsystem.donation.entity.DonorEntity;

import java.time.LocalDateTime;

@Repository
public interface DonorRepository extends CrudRepository<DonorEntity, Long> {
    @Modifying
    @Query("UPDATE DonorEntity d SET " +
            "d.firstName = :firstName, " +
            "d.lastName = :lastName, " +
            "d.email = :email, " +
            "d.phoneNumber = :phoneNumber, " +
            "d.organizationName = :organizationName, " +
            "d.donationHistory = :donationHistory, " +
            "d.personalId = :personalId, " +
            "d.birthDate = :birthDate, " +
            "d.gender = :gender, " +
            "d.active = :active " +
            "WHERE d.id = :id")
    void updateDonorById(@Param("id") Long id,
                        @Param("firstName") String firstName,
                        @Param("lastName") String lastName,
                        @Param("email") String email,
                        @Param("phoneNumber") String phoneNumber,
                        @Param("organizationName") String organizationName,
                        @Param("donationHistory") String donationHistory,
                        @Param("personalId") String personalId,
                        @Param("birthDate") LocalDateTime birthDate,
                        @Param("gender") Character gender,
                        @Param("active") Boolean active);
}
