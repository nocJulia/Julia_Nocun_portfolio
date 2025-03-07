package pl.lodz.coordinationsystem.donation.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.coordinationsystem.communication.repository.MessageRepository;
import pl.lodz.coordinationsystem.donation.model.*;
import pl.lodz.coordinationsystem.donation.repository.DonationRepository;
import pl.lodz.coordinationsystem.donation.repository.DonorRepository;
import pl.lodz.coordinationsystem.security.model.Role;
import pl.lodz.coordinationsystem.security.services.ISecurity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IDonationsTest {
    @Autowired
    IDonations donationService;
    @Autowired
    DonationRepository donationsRepository;
    @Autowired
    DonorRepository donorRepository;
    @Autowired
    ISecurity securityService;
    @Autowired
    MessageRepository messageRepository;

    private Donor testDonor;
    private Donor testDonor2;

    @BeforeEach
    void setUp() {
        Role testRole = securityService.getRoleByName("DONOR").orElseThrow();
        testDonor = new Donor("John", "Doe", "john.doe@example.com", "password", testRole, true,
                LocalDateTime.now());
        testDonor2 = new Donor("Jane", "Doe", "jane.doe@example.com", "password", testRole, true,
                LocalDateTime.now());
        donationsRepository.deleteAll();
        donorRepository.deleteAll();
        messageRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        donationsRepository.deleteAll();
        donorRepository.deleteAll();
        messageRepository.deleteAll();
    }

    @Test
    void createNewDonation() {
        Long donorId = donationService.createNewDonor(testDonor);
        testDonor = donationService.findDonorById(donorId).orElseThrow();
        Donation testDonation = new Donation("Test", DonationType.FOOD, BigDecimal.TEN, DonationCurrency.PLN,
                DonationSize.SMALL, LocalDateTime.now(), DonationStatus.PENDING, testDonor.getId());
        Long donationId = donationService.createNewDonation(testDonation);
        assertNotNull(donationService.findDonationById(donationId));
        assertEquals(1, donationService.findAllDonations().size());

    }

    @Test
    void findAllDonations() {
        Long donorId = donationService.createNewDonor(testDonor);
        testDonor = donationService.findDonorById(donorId).orElseThrow();
        Long donorId2 = donationService.createNewDonor(testDonor2);
        testDonor2 = donationService.findDonorById(donorId2).orElseThrow();
        Donation testDonation = new Donation("Test", DonationType.FOOD, BigDecimal.TEN, DonationCurrency.PLN,
                DonationSize.SMALL, LocalDateTime.now(), DonationStatus.PENDING, testDonor.getId());
        donationService.createNewDonation(testDonation);
        Donation testDonation2 = new Donation("Test2", DonationType.FOOD, BigDecimal.TEN, DonationCurrency.PLN,
                DonationSize.SMALL, LocalDateTime.now().minusDays(15), DonationStatus.PENDING, testDonor2.getId());
        donationService.createNewDonation(testDonation2);
        assertEquals(2, donationService.findAllDonations().size());
    }

    @Test
    void findDonationById() {
        Long donorId = donationService.createNewDonor(testDonor);
        testDonor = donationService.findDonorById(donorId).orElseThrow();
        Donation testDonation = new Donation("Test", DonationType.FOOD, BigDecimal.TEN, DonationCurrency.PLN,
                DonationSize.SMALL, LocalDateTime.now(), DonationStatus.PENDING, testDonor.getId());
        Long donationId = donationService.createNewDonation(testDonation);
        assertNotNull(donationService.findDonationById(donationId));
    }

    @Test
    void createNewDonor() {
        Long donorId = donationService.createNewDonor(testDonor);
        assertNotNull(donationService.findDonorById(donorId));
        assertEquals(1, donationService.findAllDonors().size());
    }

    @Test
    void findAllDonors() {
        donationService.createNewDonor(testDonor);
        donationService.createNewDonor(testDonor2);
        assertEquals(2, donationService.findAllDonors().size());
    }

    @Test
    void findDonorById() {
        Long donorId = donationService.createNewDonor(testDonor);
        assertNotNull(donationService.findDonorById(donorId));
    }

    @Test
    void findAllDonationsByDonorId() {
        Long donorId = donationService.createNewDonor(testDonor);
        testDonor = donationService.findDonorById(donorId).orElseThrow();
        Donation testDonation = new Donation("Test", DonationType.FOOD, BigDecimal.TEN, DonationCurrency.PLN,
                DonationSize.SMALL, LocalDateTime.now(), DonationStatus.PENDING, testDonor.getId());
        donationService.createNewDonation(testDonation);
        Donation testDonation2 = new Donation("Test2", DonationType.FOOD, BigDecimal.TEN, DonationCurrency.PLN,
                DonationSize.SMALL, LocalDateTime.now().minusDays(15), DonationStatus.PENDING, testDonor.getId());
        donationService.createNewDonation(testDonation2);
        assertEquals(2, donationService.findAllDonationsByDonorId(donorId).size());
    }

    @Test
    void findAllDonationsByStatus() {
        Long donorId = donationService.createNewDonor(testDonor);
        testDonor = donationService.findDonorById(donorId).orElseThrow();
        Donation testDonation = new Donation("Test", DonationType.FOOD, BigDecimal.TEN, DonationCurrency.PLN,
                DonationSize.SMALL, LocalDateTime.now(), DonationStatus.PENDING, testDonor.getId());
        donationService.createNewDonation(testDonation);
        Donation testDonation2 = new Donation("Test2", DonationType.FOOD, BigDecimal.TEN, DonationCurrency.PLN,
                DonationSize.SMALL, LocalDateTime.now().minusDays(15), DonationStatus.PENDING, testDonor.getId());
        donationService.createNewDonation(testDonation2);
        assertEquals(2, donationService.findAllDonationsByStatus(DonationStatus.PENDING).size());
    }

    @Test
    void updateDonationStatusById() {
        Long donorId = donationService.createNewDonor(testDonor);
        testDonor = donationService.findDonorById(donorId).orElseThrow();
        Donation testDonation = new Donation("Test", DonationType.FOOD, BigDecimal.TEN, DonationCurrency.PLN,
                DonationSize.SMALL, LocalDateTime.now(), DonationStatus.PENDING, testDonor.getId());
        Long donationId = donationService.createNewDonation(testDonation);
        donationService.updateDonationStatusById(donationId, DonationStatus.ASSIGNED);
        assertEquals(DonationStatus.ASSIGNED, donationService.findDonationById(donationId).orElseThrow().getStatus());
    }

    @Test
    void findAllDonationsByDonationDateRange() {
        Long donorId = donationService.createNewDonor(testDonor);
        testDonor = donationService.findDonorById(donorId).orElseThrow();
        Donation testDonation = new Donation("Test", DonationType.FOOD, BigDecimal.TEN, DonationCurrency.PLN,
                DonationSize.SMALL, LocalDateTime.now(), DonationStatus.PENDING, testDonor.getId());
        donationService.createNewDonation(testDonation);
        Donation testDonation2 = new Donation("Test2", DonationType.FOOD, BigDecimal.TEN, DonationCurrency.PLN,
                DonationSize.SMALL, LocalDateTime.now().minusDays(15), DonationStatus.PENDING, testDonor.getId());
        donationService.createNewDonation(testDonation2);
        assertEquals(2, donationService.findAllDonationsByDonationDateRange(LocalDateTime.now().minusDays(16), LocalDateTime.now().plusDays(1)).size());
        assertEquals(1, donationService.findAllDonationsByDonationDateRange(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)).size());
    }
}
