package pl.lodz.coordinationsystem.donation.service;

import pl.lodz.coordinationsystem.donation.model.Donation;
import pl.lodz.coordinationsystem.donation.model.DonationStatus;
import pl.lodz.coordinationsystem.donation.model.Donor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface for services related to donation and donor operations.
 */
public interface IDonations {

    /**
     * Creates a new donation and returns its ID.
     *
     * @param donation the donation to be added
     * @return the ID of the newly created donation
     */
    Long createNewDonation(Donation donation);

    /**
     * Returns a list of all donations.
     *
     * @return a list of all donations
     */
    List<Donation> findAllDonations();

    /**
     * Returns a donation by its ID.
     *
     * @param id the ID of the donation
     * @return an Optional containing the donation if it exists
     */
    Optional<Donation> findDonationById(Long id);

    /**
     * Creates a new donor and returns its ID.
     *
     * @param donor the donor to be added
     * @return the ID of the newly created donor
     */
    Long createNewDonor(Donor donor);

    /**
     * Updates an existing donor.
     *
     * @param donorId the ID of the donor to be updated
     * @param donor the updated donor
     */
    void updateDonorById(Long donorId, Donor donor);

    /**
     * Returns a list of all donors.
     *
     * @return a list of all donors
     */
    List<Donor> findAllDonors();

    /**
     * Returns a donor by its ID.
     *
     * @param id the ID of the donor
     * @return an Optional containing the donor if it exists
     */
    Optional<Donor> findDonorById(Long id);

    /**
     * Returns all donations made by a donor with the specified ID.
     *
     * @param donorId the ID of the donor
     * @return a list of all donations made by the donor
     */
    List<Donation> findAllDonationsByDonorId(Long donorId);

    /**
     * Returns all donations with the specified status.
     *
     * @param status the status of the donations
     * @return a list of all donations with the specified status
     */
    List<Donation> findAllDonationsByStatus(DonationStatus status);

    /**
     * Update donation status by its ID.
     *
     * @param donationId the ID of the donation
     * @param status the status of the donation
     */
    void updateDonationStatusById(Long donationId, DonationStatus status);

    /**
     * Returns all donations made within the specified date range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a list of all donations made within the specified date range
     */
    List<Donation> findAllDonationsByDonationDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
