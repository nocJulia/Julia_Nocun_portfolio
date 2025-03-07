package pl.lodz.coordinationsystem.notification.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.notification.model.NotificationStatus;
import pl.lodz.coordinationsystem.notification.model.Victim;
import pl.lodz.coordinationsystem.notification.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class NotificationController {
    NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications/new")
    public String showNotificationForm(Model model) {
        Notification notification = new Notification();
        ArrayList<Victim> victims = new ArrayList<>();
        victims.add(new Victim());
        notification.setVictims(victims);
        model.addAttribute("notification", notification);
        return "views/notifications/newNotification";
    }

    @PostMapping("/notifications/save")
    public String save(Notification notification, RedirectAttributes redirectAttributes) {
        try {
            Long id = notificationService.createNewNotification(notification);
            return "redirect:/locations/newNotification?notificationId=" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "The notification could not be saved");
            return "redirect:/notifications/new";
        }
    }

    @GetMapping("/dashboard/notifications")
    public String showNotificationsDashboard(
            @RequestParam(value = "status", required = false) NotificationStatus status, Model model) {
        List<Notification> notifications;
        if (status != null) {
            notifications = notificationService.findNotificationsByStatus(status);
        } else {
            notifications = notificationService.findAllNotifications();
        }
        model.addAttribute("notifications", notifications);
        model.addAttribute("statuses", NotificationStatus.values());
        return "views/notifications/notifications";
    }

    @GetMapping("/dashboard/notifications/details/{id}")
    public String getNotificationDetails(@PathVariable("id") Long id, Model model) {
        Notification notification = notificationService.findNotificationById(id).orElseThrow();
        model.addAttribute("notification", notification);
        model.addAttribute("statuses", NotificationStatus.values());
        return "views/notifications/notificationDetails";
    }

    @PostMapping("/dashboard/notifications/{id}/change-status")
    public String changeNotificationStatus(@PathVariable("id") Long id, NotificationStatus status, RedirectAttributes redirectAttributes) {
        try {
            notificationService.updateNotificationStatus(id, status);
            redirectAttributes.addFlashAttribute("toast", "The notification status has been changed");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "The notification status could not be changed");
        }
        return "redirect:/dashboard/notifications/details/" + id;
    }

    @PostMapping("/dashboard/notifications/{id}/delete")
    public String deleteNotification(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            notificationService.deleteNotification(id);
            redirectAttributes.addFlashAttribute("toast", "The notification has been deleted");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "The notification could not be deleted");
            return "redirect:/dashboard/notifications/details/" + id;
        }
        return "redirect:/dashboard/notifications";
    }

}
