//package pl.lodz.coordinationsystem.reports.services;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import pl.lodz.coordinationsystem.communication.repository.MessageRepository;
//import pl.lodz.coordinationsystem.donation.model.*;
//import pl.lodz.coordinationsystem.donation.repository.DonationRepository;
//import pl.lodz.coordinationsystem.donation.repository.DonorRepository;
//import pl.lodz.coordinationsystem.donation.service.IDonations;
//import pl.lodz.coordinationsystem.maps.repositories.LocationRepository;
//import pl.lodz.coordinationsystem.notification.model.Notification;
//import pl.lodz.coordinationsystem.notification.model.Victim;
//import pl.lodz.coordinationsystem.notification.repository.NotificationRepository;
//import pl.lodz.coordinationsystem.notification.service.INotifications;
//import pl.lodz.coordinationsystem.raports.entity.ReportInfoEntity;
//import pl.lodz.coordinationsystem.raports.model.Report;
//import pl.lodz.coordinationsystem.raports.model.ReportFileType;
//import pl.lodz.coordinationsystem.raports.model.ReportType;
//import pl.lodz.coordinationsystem.raports.repository.RaportRepository;
//import pl.lodz.coordinationsystem.raports.service.IReporting;
//import pl.lodz.coordinationsystem.resource.service.IResources;
//import pl.lodz.coordinationsystem.security.model.Role;
//import pl.lodz.coordinationsystem.security.model.User;
//import pl.lodz.coordinationsystem.security.repositories.BaseUserRepository;
//import pl.lodz.coordinationsystem.security.services.ISecurity;
//import pl.lodz.coordinationsystem.volunteer.model.Volunteer;
//import pl.lodz.coordinationsystem.volunteer.repository.VolunteerRepository;
//import pl.lodz.coordinationsystem.volunteer.service.IVolunteer;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class IReportingTest {
//    @Autowired
//    private INotifications notificationService;
//    @Autowired
//    private NotificationRepository notificationRepository;
//    @Autowired
//    private IVolunteer volunteerService;
//    @Autowired
//    private VolunteerRepository volunteerRepository;
//    @Autowired
//    private IReporting reportingService;
//    @Autowired
//    private ISecurity securityService;
//    @Autowired
//    private RaportRepository raportRepository;
//    @Autowired
//    LocationRepository locationRepository;
//    @Autowired
//    MessageRepository messageRepository;
//    @Autowired
//    BaseUserRepository baseUserRepository;
//    @Autowired
//    private IDonations donationService;
//    @Autowired
//    private IResources resourcesService;
//    @Autowired
//    private DonationRepository donationsRepository;
//    @Autowired
//    private DonorRepository donorRepository;
//
//    private Long donorId;
//    private Long volunteerId;
//    private Long notificationUserId;
//    private Long donationUserId;
//    private User testUser;
//    private User testAdminUser;
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;
//    private Donor testDonor;
//    private Donor testDonor2;
//    private Donation testDonation;
//
//
//
//    @BeforeEach
//    void setUp() {
//
//        Role testRoleDonor = securityService.getRoleByName("DONOR").orElseThrow();
//        Role testAdminRole = securityService.getRoleByName("ADMIN").orElseThrow();
//
//        testAdminUser = new User("Bob", "Builder", "BobBuilder@mail.com", "Bobpasswd", testAdminRole, true,
//                LocalDateTime.now());
//
//        //todo create admin for generateReportDonations()
//
//        testDonor = new Donor("John", "Doe", "john.doe@example.com", "password", testRoleDonor, true,
//                LocalDateTime.now());
//
//        Long donorId = donationService.createNewDonor(testDonor);
//
//        testDonor = donationService.findDonorById(donorId).orElseThrow();
//
//        Donation testDonation = new Donation("Test", DonationType.FOOD, BigDecimal.TEN, DonationCurrency.PLN,
//                DonationSize.SMALL, LocalDateTime.now(), DonationStatus.PENDING, donationService.findDonorById(donorId).orElseThrow().getId() );
//
//        Long donationId = donationService.createNewDonation(testDonation);
//
//    }
//
//    @AfterEach
//    void tearDown() {
//        raportRepository.deleteAll();
//        notificationRepository.deleteAll();
//        volunteerRepository.deleteAll();
//        locationRepository.deleteAll();
//        messageRepository.deleteAll();
//        donationsRepository.deleteAll();
//        donorRepository.deleteAll();
//        baseUserRepository.deleteAll();
//    }
//
//    @Test
//    void generateReportForDonorTest() {
//        Report report = reportingService.generateReportForDonor(testDonor.getId());
//        List<ReportInfoEntity> listaReport = (List<ReportInfoEntity>) raportRepository.findAll();
//
//        assertEquals(report.getReportType(), ReportType.DONATION);
//        assertEquals(report.getReportFileType(), ReportFileType.JSON);
//        assertEquals(report.getUserId(), testDonor.getId());
//        assert(!listaReport.isEmpty());
//    }
//
//    @Test
//    void generateReportDonations() {
//        System.out.println(testAdminUser.getId());
//        Report report = reportingService.generateReportDonations(startTime, endTime, testDonor.getId());
//        List<ReportInfoEntity> listaReport = (List<ReportInfoEntity>) raportRepository.findAll();
//
//        assertEquals(report.getReportType(), ReportType.DONATION);
//        assertEquals(report.getReportFileType(), ReportFileType.JSON);
//        assert(!listaReport.isEmpty());
//    }
//
//    @Test
//    void generateReportVolunteerTest() {
//        Role testRoleVolunteer = securityService.getRoleByName("VOLUNTEER").orElseThrow();
//
//        Volunteer testVolunteer = new Volunteer();
//        testVolunteer.setFirstName("John");
//        testVolunteer.setLastName("Doe");
//        testVolunteer.setEmail("john.doe@example.com");
//        testVolunteer.setPassword("password");
//        testVolunteer.setCreatedAt(LocalDateTime.now());
//        testVolunteer.setActive(true);
//        testVolunteer.setPhoneNumber("1234567890");
//        testVolunteer.setRole(testRoleVolunteer);
//
//        //TODO end test when volounteers make constructor without id
//
//        //testLocation = new Location(51.759, 19.456, "Test Location", LocationType.VOLUNTEER);
//        //victims = List.of(new Victim("John", "Down"));
//
//        //Long volunteerId = volunteerService.createVolunteer(testVolunteer);
//
//        /*Report report = reportingService.generateReportVolunteer(volunteerId);
//        List<ReportInfoEntity> listaReport = (List<ReportInfoEntity>) raportRepository.findAll();
//
//        assertEquals(report.getReportType(), ReportType.VOLUNTEER);
//        assertEquals(report.getReportFileType(), ReportFileType.JSON);
//        assertEquals(report.getReportData(), volunteerService.findAllVolunteers());
//        assert(!listaReport.isEmpty());*/
//
//    }
//
//    @Test
//    void generateReportNotificationsTest() {
//        List<Victim> victims = List.of(new Victim("John", "Down"));
//
//        Long notificationUserId =  notificationService.createNewNotification(new Notification("Test", victims));
//
//        Report report = reportingService.generateReportNotifications(startTime, endTime, testDonor.getId());
//        List<ReportInfoEntity> listaReport = (List<ReportInfoEntity>) raportRepository.findAll();
//
//        assertEquals(report.getReportType(), ReportType.NOTIFICATION);
//        assertEquals(report.getReportFileType(), ReportFileType.JSON);
//        assert(!listaReport.isEmpty());
//    }
//
//    @Test
//    void generateReportResourcesTest() {
//
//        Role role = securityService.getRoleByName("LOGISTICIAN").orElseThrow();
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
//
//        /*Report report = reportingService.generateReportResources(userId);
//        List<ReportInfoEntity> listaReport = (List<ReportInfoEntity>) raportRepository.findAll();
//
//        assertEquals(report.getReportType(), ReportType.RESOURCE);
//        assertEquals(report.getReportFileType(), ReportFileType.JSON);
//        assertEquals(report.getReportData(), resourcesService.getAllAllocatedResources());
//        assert(!listaReport.isEmpty());*/
//    }
//}