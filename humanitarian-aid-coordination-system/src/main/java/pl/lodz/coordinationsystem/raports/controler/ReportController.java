package pl.lodz.coordinationsystem.raports.controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.coordinationsystem.donation.model.Donation;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.raports.model.Report;
import pl.lodz.coordinationsystem.raports.service.ReportingService;
import pl.lodz.coordinationsystem.resource.model.AllocatedResource;
import pl.lodz.coordinationsystem.security.services.ISecurity;
import pl.lodz.coordinationsystem.volunteer.model.Volunteer;

import java.time.LocalDateTime;
@Controller
@RequestMapping("/dashboard/reports")
public class ReportController {

    private final ReportingService reportingService;
    private final ISecurity security;

    public ReportController(ReportingService reportingService, ISecurity security) {
        this.reportingService = reportingService;
        this.security = security;
    }

    @GetMapping("/donations")
    public String getDonationsReportPage(Model model)
    {
        return "/views/reports/donationsReport";
    }

    @GetMapping("/donationsReportResult")
    public String getDonationsReportResultPage(Model model)
    {
        return "/views/reports/donationsReportResult";
    }

    @PostMapping("/donations")
    public String generateDonationsReport(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Report<Donation> report = reportingService.generateReportDonations(startDate, endDate, security.getCurrentUserId());
            //System.out.println(report);
            //model.addAttribute("report", report);
            redirectAttributes.addFlashAttribute("report", report);
            redirectAttributes.addFlashAttribute("toast", "Donations report generated successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
            return "redirect:donationsReportResult";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to generate donations report.");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/dashboard";
        }
    }

    //donor
    @GetMapping("/donors")
    public String getDonorReportPage(Model model)
    {
        return "/views/reports/donorReport";
    }

    @GetMapping("/donorReportResult")
    public String getDonorReportResultPage(Model model)
    {
        return "/views/reports/donorReportResult";
    }

    @PostMapping("/donors")
    public String generateDonorsReport(
            @RequestParam("userId") Long userId,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Report<Donation> report = reportingService.generateReportForDonor(userId);
            redirectAttributes.addFlashAttribute("report", report);
            redirectAttributes.addFlashAttribute("toast", "Donor report generated successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
            return "redirect:donorReportResult";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to generate donor report.");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/dashboard";
        }
    }


    //notifications
    @GetMapping("/notifications")
    public String getNotificationsReportPage(Model model)
    {
        return "/views/reports/notificationsReport";
    }

    @GetMapping("/notificationsReportResult")
    public String getNotificationsReportResultPage(Model model)
    {
        return "/views/reports/notificationsReportResult";
    }

    @PostMapping("/notifications")
    public String generateNotificationsReport(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Report<Notification> report = reportingService.generateReportNotifications(startDate, endDate, security.getCurrentUserId());
            redirectAttributes.addFlashAttribute("report", report);
            redirectAttributes.addFlashAttribute("toast", "Notifications report generated successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
            return "redirect:notificationsReportResult";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to generate notifications report.");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/dashboard";
        }
    }

    //volunteers
    @GetMapping("/volunteers")
    public String getVolunteersReportPage(Model model)
    {
        return "/views/reports/volunteerReport";
    }

    @GetMapping("/volunteerReportResult")
    public String getVolunteersReportResultPage(Model model)
    {
        return "/views/reports/volunteerReportResult";
    }

    @PostMapping("/volunteers")
    public String generateVolunteersReport(
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Report<Volunteer> report = reportingService.generateReportVolunteer(security.getCurrentUserId());
            redirectAttributes.addFlashAttribute("report", report);
            redirectAttributes.addFlashAttribute("toast", "Volunteers report generated successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
            return "redirect:volunteerReportResult";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to generate volunteers report.");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/dashboard";
        }
    }

    //resources
    @GetMapping("/resources")
    public String getResourcesReportPage(Model model)
    {
        return "/views/reports/resourcesReport";
    }

    @GetMapping("/resourcesReportResult")
    public String getResourcesReportResultPage(Model model)
    {
        return "/views/reports/resourcesReportResult";
    }

    @PostMapping("/resources")
    public String generateResourcesReport(
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Report<AllocatedResource> report = reportingService.generateReportResources(security.getCurrentUserId());
            redirectAttributes.addFlashAttribute("report", report);
            redirectAttributes.addFlashAttribute("toast", "Volunteers report generated successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
            return "redirect:resourcesReportResult";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to generate volunteers report.");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/dashboard";
        }
    }


}