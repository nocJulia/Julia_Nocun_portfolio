//package pl.lodz.coordinationsystem.resource.service;
//
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import pl.lodz.coordinationsystem.communication.repository.MessageRepository;
//import pl.lodz.coordinationsystem.donation.model.*;
//import pl.lodz.coordinationsystem.donation.repository.DonationRepository;
//import pl.lodz.coordinationsystem.donation.repository.DonorRepository;
//import pl.lodz.coordinationsystem.donation.service.DonationService;
//import pl.lodz.coordinationsystem.maps.repositories.LocationRepository;
//import pl.lodz.coordinationsystem.notification.model.Notification;
//import pl.lodz.coordinationsystem.notification.model.Victim;
//import pl.lodz.coordinationsystem.notification.repository.NotificationRepository;
//import pl.lodz.coordinationsystem.notification.service.NotificationService;
//import pl.lodz.coordinationsystem.resource.entity.VehicleEntity;
//import pl.lodz.coordinationsystem.resource.model.Logistician;
//import pl.lodz.coordinationsystem.resource.model.Vehicle;
//import pl.lodz.coordinationsystem.resource.repository.AllocatedResourceRepository;
//import pl.lodz.coordinationsystem.resource.repository.LogisticianRepository;
//import pl.lodz.coordinationsystem.security.model.Role;
//import pl.lodz.coordinationsystem.security.repositories.BaseUserRepository;
//import pl.lodz.coordinationsystem.security.services.ISecurity;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class IResourcesTest {
//    @Autowired
//    IResources resourcesSystem;
//    @Autowired
//    NotificationRepository notificationRepository;
//    @Autowired
//    LocationRepository locationRepository;
//    @Autowired
//    AllocatedResourceRepository allocatedResourceRepository;
//    @Autowired
//    MessageRepository messageRepository;
//    @Autowired
//    DonationService donationService;
//    @Autowired
//    NotificationService notificationService;
//    @Autowired
//    ISecurity securityService;
//    @Autowired
//    LogisticianRepository logisticianRepository;
//    @Autowired
//    DonorRepository donorRepository;
//    @Autowired
//    BaseUserRepository userBaseRepository;
//    @Autowired
//    DonationRepository donationRepository;
//    Role role;
//
//    @BeforeEach
//    void setUp() {
//        role = securityService.getRoleByName("LOGISTICIAN").orElseThrow();
//
//        Donor donor = new Donor(null, "John", "Down", "john@gmail.com", "1234", role, true,
//                LocalDateTime.now(), "testOrg", "", "", 'm',
//                LocalDateTime.now(), "000000000", null);
//        Long donor_id = donationService.createNewDonor(donor); // Zapis i uzyskanie ID darczyńcy
//
//        Donation donation = new Donation(null, "testDonationForResources", DonationType.CLOTHES,
//                new BigDecimal(15), DonationCurrency.PLN,
//                DonationSize.MEDIUM, new BigDecimal(50),
//                LocalDateTime.now(), DonationStatus.PENDING,
//                donor_id);
//        donationService.createNewDonation(donation);
//
//        List<Victim> victims = List.of(new Victim("John", "Down"));
//        Notification notification = new Notification("Test", victims);
//        notificationService.createNewNotification(notification);
//    }
//
//
//    @AfterEach
//    void tearDown() {
//        locationRepository.deleteAll();
//        allocatedResourceRepository.deleteAll();
//        messageRepository.deleteAll();
//        notificationRepository.deleteAll();
//        logisticianRepository.deleteAll();
//        donationRepository.deleteAll();
//        donorRepository.deleteAll();
//        userBaseRepository.deleteAll();
//    }
//
//    @Test
//    void createdNewAllocatedResource() {
//        assertEquals(0, resourcesSystem.getAllAllocatedResources().size());
//
//        Notification notification = notificationService.findAllNotifications().getFirst();
//        Donation donation = donationService.findAllDonations().getFirst();
//        VehicleEntity vehicle = new VehicleEntity();
//
//        resourcesSystem.allocateResource(donation, notification, 5, vehicle);
//
//        assertEquals(1, resourcesSystem.getAllAllocatedResources().size());
//    }
//
//    @Test
//    void createNewLogitician() {
//        var logistician = new Logistician(null, "testFirst", "testLast", "io@gmail.com",
//                "12345", role, true, LocalDateTime.now(), null);
//        assertEquals(0, resourcesSystem.getAllLogisticians().size());
//        resourcesSystem.createLogistician(logistician);
//        assertEquals(1, resourcesSystem.getAllLogisticians().size());
//    }
//
//}