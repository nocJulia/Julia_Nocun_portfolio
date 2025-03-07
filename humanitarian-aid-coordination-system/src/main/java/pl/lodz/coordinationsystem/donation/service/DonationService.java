package pl.lodz.coordinationsystem.donation.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.coordinationsystem.communication.model.Message;
import pl.lodz.coordinationsystem.communication.service.IActors;
import pl.lodz.coordinationsystem.donation.model.Donation;
import pl.lodz.coordinationsystem.donation.model.DonationStatus;
import pl.lodz.coordinationsystem.donation.model.Donor;
import pl.lodz.coordinationsystem.donation.repository.DonationRepository;
import pl.lodz.coordinationsystem.donation.repository.DonorRepository;
import pl.lodz.coordinationsystem.security.model.Role;
import pl.lodz.coordinationsystem.security.services.ISecurity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class DonationService implements IDonations {
    private final DonorRepository donorRepository;
    private final DonationRepository donationRepository;
    private final ISecurity securityService;
    private final IActors actorsService;
    private final PasswordEncoder passwordEncoder;

    public DonationService(DonationRepository donationRepository, DonorRepository donorRepository,
                           ISecurity securityService, IActors actorsService, PasswordEncoder passwordEncoder) {
        this.donationRepository = donationRepository;
        this.donorRepository = donorRepository;
        this.securityService = securityService;
        this.actorsService = actorsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Long createNewDonation(Donation donation) {
        Long donationId =  donationRepository.save(DonationMapper.toEntity(donation)).getId();
        Role logisticianRole = securityService.getRoleByName("LOGISTICIAN").orElseThrow();
        Message message = new Message(logisticianRole, "New donation created",
                "New donation has been created with id: " + donationId);
        actorsService.addMessageForRole(message);
        return donationId;
    }

    @Override
    public List<Donation> findAllDonations() {
        return StreamSupport.stream(donationRepository.findAll().spliterator(), false)
                .map(DonationMapper::toModel)
                .toList();
    }

    @Override
    public Optional<Donation> findDonationById(Long id) {
        return donationRepository.findById(id).map(DonationMapper::toModel);
    }

    @Override
    @Transactional
    public Long createNewDonor(Donor donor) {
        donor.setCreatedAt(LocalDateTime.now());
        donor.setRole(securityService.getRoleByName("DONOR").orElseThrow());
        donor.setPassword(passwordEncoder.encode(donor.getPassword()));
        return donorRepository.save(DonorMapper.toEntity(donor)).getId();
    }

    @Override
    @Transactional
    public void updateDonorById(Long donorId, Donor updatedDonor) {
        Donor donor = findDonorById(donorId).orElseThrow();
        donor.setFirstName(updatedDonor.getFirstName());
        donor.setLastName(updatedDonor.getLastName());
        donor.setEmail(updatedDonor.getEmail());
        donor.setPhoneNumber(updatedDonor.getPhoneNumber());
        donor.setOrganizationName(updatedDonor.getOrganizationName());
        donor.setDonationHistory(updatedDonor.getDonationHistory());
        donor.setPersonalId(updatedDonor.getPersonalId());
        donor.setBirthDate(updatedDonor.getBirthDate());
        donor.setGender(updatedDonor.getGender());
        donor.setActive(updatedDonor.getActive());
        donorRepository.updateDonorById(donor.getId(), donor.getFirstName(), donor.getLastName(), donor.getEmail(),
                donor.getPhoneNumber(), donor.getOrganizationName(), donor.getDonationHistory(), donor.getPersonalId(),
                donor.getBirthDate(), donor.getGender(), donor.getActive());
    }

    @Override
    @Transactional
    public List<Donor> findAllDonors() {
        return StreamSupport.stream(donorRepository.findAll().spliterator(), false)
                .map(DonorMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public Optional<Donor> findDonorById(Long id) {
        return donorRepository.findById(id).map(DonorMapper::toModel);
    }

    @Override
    public List<Donation> findAllDonationsByDonorId(Long donorId) {
        return donationRepository.findAllByDonorId(donorId).stream()
                .map(DonationMapper::toModel)
                .toList();
    }

    @Override
    public List<Donation> findAllDonationsByStatus(DonationStatus status) {
        return donationRepository.findAllByStatus(status).stream()
                .map(DonationMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public void updateDonationStatusById(Long donationId, DonationStatus status) {
        Donation donation = donationRepository.findById(donationId).map(DonationMapper::toModel).orElseThrow();
        donation.setStatus(status);
        donationRepository.save(DonationMapper.toEntity(donation));
    }

    @Override
    public List<Donation> findAllDonationsByDonationDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return donationRepository.findAllByDonationDateGreaterThanAndDonationDateLessThan(startDate, endDate).stream()
                .map(DonationMapper::toModel)
                .toList();
    }
}
