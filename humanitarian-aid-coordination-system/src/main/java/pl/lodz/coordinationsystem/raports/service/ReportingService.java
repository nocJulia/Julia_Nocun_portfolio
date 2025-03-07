package pl.lodz.coordinationsystem.raports.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.coordinationsystem.donation.model.Donation;
import pl.lodz.coordinationsystem.donation.model.Donor;
import pl.lodz.coordinationsystem.donation.service.IDonations;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.notification.service.INotifications;
import pl.lodz.coordinationsystem.raports.entity.ReportInfoEntity;
import pl.lodz.coordinationsystem.raports.model.Report;
import pl.lodz.coordinationsystem.raports.model.ReportFileType;
import pl.lodz.coordinationsystem.raports.model.ReportType;
import pl.lodz.coordinationsystem.raports.repository.RaportRepository;
import pl.lodz.coordinationsystem.resource.model.AllocatedResource;
import pl.lodz.coordinationsystem.resource.service.IResources;
import pl.lodz.coordinationsystem.security.model.User;
import pl.lodz.coordinationsystem.security.services.ISecurity;
import pl.lodz.coordinationsystem.volunteer.model.Volunteer;
import pl.lodz.coordinationsystem.volunteer.service.IVolunteer;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReportingService implements IReporting {
    private final INotifications notificationService;
    private final IVolunteer volunteerService;
    private final IDonations donationService;
    private final IResources resourcesService;
    private final ISecurity securityService;
    RaportRepository raportRepository;;

    public ReportingService(INotifications notificationService, IVolunteer volunteerService, IDonations donationService, IResources resourcesService, ISecurity securityService, RaportRepository raportRepository) {
        this.notificationService = notificationService;
        this.volunteerService = volunteerService;
        this.donationService = donationService;
        this.resourcesService = resourcesService;
        this.securityService = securityService;
        this.raportRepository = raportRepository;
    }

    @Override
    public Report<Volunteer> generateReportVolunteer(Long userId) {
        List<Volunteer> list = new ArrayList<>();
        list = volunteerService.findAllVolunteers();
        Report<Volunteer> report = new Report<Volunteer>(1L, ReportType.VOLUNTEER, ReportFileType.HTML, list, userId);
        saveReportInfoToDb(report);
        return report;
    }

    @Override
    public Report<Notification> generateReportNotifications(LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        List<Notification> list = new ArrayList<>();
        list = notificationService.findNotificationsByCreationDateRange(startDate, endDate);
        Report<Notification> report = new Report<Notification>(1L, ReportType.NOTIFICATION, ReportFileType.HTML, list,userId);
        saveReportInfoToDb(report);
        return report;
    }

    @Override
    public Report<Donation> generateReportForDonor(Long userId) {
        List<Donation> list = new ArrayList<>();
        list = donationService.findAllDonationsByDonorId(userId);
        Report<Donation> report = new Report<Donation>(null, ReportType.DONATION, ReportFileType.HTML, list,userId);
        saveReportInfoToDb(report);
        return report;
    }

    @Override
    public Report<Donation> generateReportDonations(LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        List<Donation> list = new ArrayList<>();
        list = donationService.findAllDonationsByDonationDateRange(startDate, endDate);
        Report<Donation> report = new Report<Donation>(null, ReportType.DONATION, ReportFileType.HTML, list, userId);
        saveReportInfoToDb(report);
        return report;
    }

    @Override
    public Report<AllocatedResource> generateReportResources(Long userId) {
        List<AllocatedResource> list = new ArrayList<>();
        list = resourcesService.getAllAllocatedResources();
        Report<AllocatedResource> report = new Report<AllocatedResource>(null, ReportType.RESOURCE, ReportFileType.HTML, list, userId);
        saveReportInfoToDb(report);
        return report;
    }

    protected void saveReportInfoToDb(Report report) {
        //TODO GDZIE INTERFEJS DO POBIERANIA CURRENT USER dziękujemy mikizakr@gmail.com za metode
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = securityService.getUserByEmail(currentPrincipalName);
        Long id = user.map(User::getId).orElseThrow(() -> new IllegalArgumentException("User not found with email: " + currentPrincipalName));
        ReportInfoEntity reportInfoEntity = new ReportInfoEntity(report.getId(), id, new Date());
        raportRepository.save(reportInfoEntity);
    }
}
