package pl.lodz.coordinationsystem.resource.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lodz.coordinationsystem.donation.model.Donation;
import pl.lodz.coordinationsystem.donation.model.DonationStatus;
import pl.lodz.coordinationsystem.donation.service.DonationService;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.notification.model.NotificationStatus;
import pl.lodz.coordinationsystem.notification.service.NotificationService;
//import pl.lodz.coordinationsystem.resource.model.Transport;
import pl.lodz.coordinationsystem.resource.entity.VehicleEntity;
import pl.lodz.coordinationsystem.resource.model.Vehicle;
import pl.lodz.coordinationsystem.resource.repository.AllocatedResourceRepository;
import pl.lodz.coordinationsystem.resource.service.ResourcesService;
import pl.lodz.coordinationsystem.resource.service.VehicleService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class ResourceController {

    private final DonationService donationService;
    private final NotificationService notificationService;
    private final ResourcesService resourcesService;
    private final VehicleService vehicleService;

    public ResourceController(DonationService donationService, NotificationService notificationService,
                              ResourcesService resourcesService, VehicleService vehicleService) {
        this.donationService = donationService;
        this.notificationService = notificationService;
        this.resourcesService = resourcesService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/dashboard/resources/new")
    public String showResources(Model model) {
        List<Donation> donationList = donationService.findAllDonationsByStatus(DonationStatus.PENDING);
        List<Notification> notificationList = notificationService.findNotificationsByStatus(NotificationStatus.PENDING);
        List<VehicleEntity> availableVehicles = vehicleService.getAllVehicles();
        model.addAttribute("donationList", donationList);
        model.addAttribute("notificationList", notificationList);
        model.addAttribute("vehicles", availableVehicles);
        return "views/resources/allocateResource";
    }

    @PostMapping("/resource/create")
    public String createResource(
            @RequestParam("selectedDonation") Long donationId,
            @RequestParam("selectedNotification") Long notificationId,
            @RequestParam("selectedVehicle") Long vehicleId,
            @RequestParam("donationAmount") Long donationAmount,
            Model model) {

        Donation donation = donationService.findDonationById(donationId).get();
        Notification notification = notificationService.findNotificationById(notificationId).get();
        VehicleEntity vehicle = vehicleService.getVehicleById(vehicleId);
        vehicleService.setAvailable(vehicleId, false);
        Long id = resourcesService.allocateResource(donation, notification, donationAmount.intValue(), vehicle);

        notificationService.updateNotificationStatus(notificationId, NotificationStatus.IN_PROGRESS);

        return "redirect:/locations/newResource?resourceId=" + id;
    }


}
