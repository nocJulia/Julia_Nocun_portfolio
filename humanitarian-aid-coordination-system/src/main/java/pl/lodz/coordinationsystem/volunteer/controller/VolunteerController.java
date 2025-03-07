package pl.lodz.coordinationsystem.volunteer.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.notification.service.NotificationService;
import pl.lodz.coordinationsystem.security.model.User;
import pl.lodz.coordinationsystem.security.services.UserService;
import pl.lodz.coordinationsystem.volunteer.model.Volunteer;
import pl.lodz.coordinationsystem.volunteer.service.VolunteerService;

import java.util.List;
import java.util.Optional;

@Controller
public class VolunteerController {

    private final VolunteerService volunteerService;
    private final NotificationService notificationService;
    private final UserService userService;

    public VolunteerController(VolunteerService volunteerService, NotificationService notificationService, UserService userService) {
        this.volunteerService = volunteerService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

//    @GetMapping("/volunteers/new")
//    public String showVolunteerForm(Model model) {
//        Volunteer volunteer = new Volunteer();
//        model.addAttribute("volunteer", volunteer);
//        return "views/volunteers/newVolunteer";
//    }

//    @PostMapping("/volunteers/save")
//    public String saveVolunteer(Volunteer volunteer, RedirectAttributes redirectAttributes) {
//        try {
//            volunteerService.createVolunteer(volunteer);
//            return "redirect:/";
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("toast", "Failed to create a volunteer.");
//            return "redirect:/volunteers/new";
//        }
//    }

    @GetMapping("/dashboard/volunteers")
    public String showVolunteersDashboard(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "state", required = false) String state,
            Model model) {
        List<Volunteer> volunteers = volunteerService.findAllVolunteers();

        if (status != null && !status.isEmpty()) {
            volunteers = volunteerService.filterByStatus(volunteers, status);
        }
        if (state != null && !state.isEmpty()) {
            volunteers = volunteerService.filterByState(volunteers, state);
        }
        model.addAttribute("volunteers", volunteers);
        model.addAttribute("statuses", List.of("BUSY", "AVAILABLE"));
        model.addAttribute("states", List.of("ACTIVE", "ARCHIVED"));
        return "views/volunteers/volunteers";
    }

    @GetMapping("/dashboard/volunteers/details/{id}")
    public String getVolunteerDetails(@PathVariable("id") Long id, Model model) {
        Volunteer volunteer = volunteerService.findVolunteerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer not found with id: " + id));
        List<Notification> availableNotifications = volunteerService.getAvailableNotificationsForVolunteer(volunteer);


        model.addAttribute("volunteer", volunteer);
        model.addAttribute("assignedNotifications", volunteer.getNotifications());
        model.addAttribute("availableNotifications", availableNotifications);
        return "views/volunteers/volunteerDetails";
    }

    @PostMapping("/dashboard/volunteers/{id}/update")
    public String updateVolunteerDetails(@PathVariable("id") Long id,
                                         @ModelAttribute Volunteer updatedVolunteer,
                                         RedirectAttributes redirectAttributes) {
        try {
            volunteerService.updateVolunteerById(id, updatedVolunteer);
            redirectAttributes.addFlashAttribute("toast", "Volunteer details updated successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to update volunteer details.");
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/dashboard/volunteers/details/" + id;
    }

    @PostMapping("/dashboard/volunteers/{id}/notifications/add")
    public String addNotificationToVolunteer(@PathVariable("id") Long volunteerId,
                                             @RequestParam Long notificationId,
                                             RedirectAttributes redirectAttributes) {
        try {
            volunteerService.addNotification(volunteerId, notificationId);
            redirectAttributes.addFlashAttribute("toast", "Notification assigned successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to assign notification: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/dashboard/volunteers/details/" + volunteerId;
    }

    @PostMapping("/dashboard/volunteers/{id}/notifications/remove")
    public String removeNotificationFromVolunteer(@PathVariable("id") Long volunteerId,
                                                  @RequestParam Long notificationId,
                                                  RedirectAttributes redirectAttributes) {
        try {
            volunteerService.removeNotification(volunteerId, notificationId);
            redirectAttributes.addFlashAttribute("toast", "Notification removed successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to remove notification: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/dashboard/volunteers/details/" + volunteerId;
    }

    @GetMapping("dashboard/volunteerAccountDetails")
    public String showVolunteerAccountDetails(Model model) {
        // Get the current user's email from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        // Retrieve the user and their volunteer profile
        Optional<User> user = userService.getUserByEmail(currentPrincipalName);
        Long id = user.map(User::getId).orElseThrow(() -> new IllegalArgumentException("User not found with email: " + currentPrincipalName));
        Volunteer volunteer = volunteerService.findVolunteerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer not found with id: " + id));

        model.addAttribute("volunteer", volunteer);
        return "views/volunteers/volunteerAccountDetails";
    }

    @PostMapping("dashboard/volunteerAccountDetails/update")
    public String updateVolunteerAccountDetails(@ModelAttribute Volunteer updatedVolunteer, RedirectAttributes redirectAttributes) {
        // Get the current user's email from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Optional<User> user = userService.getUserByEmail(currentPrincipalName);
        Long id = user.map(User::getId).orElseThrow(() -> new IllegalArgumentException("User not found with email: " + currentPrincipalName));

        try {
            volunteerService.updateVolunteerById(id, updatedVolunteer);
            redirectAttributes.addFlashAttribute("toast", "Volunteer details updated successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to update volunteer details.");
            redirectAttributes.addFlashAttribute("toastType", "error");
        }

        return "redirect:/dashboard/volunteerAccountDetails";
    }
}