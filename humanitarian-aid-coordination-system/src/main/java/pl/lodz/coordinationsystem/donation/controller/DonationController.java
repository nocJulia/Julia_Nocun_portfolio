package pl.lodz.coordinationsystem.donation.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.coordinationsystem.donation.model.Donation;
import pl.lodz.coordinationsystem.donation.model.DonationStatus;
import pl.lodz.coordinationsystem.donation.model.Donor;
import pl.lodz.coordinationsystem.donation.service.DonationService;
import pl.lodz.coordinationsystem.security.model.User;
import pl.lodz.coordinationsystem.security.services.ISecurity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class DonationController {

    private final DonationService donationService;
    private final ISecurity securityService;

    public DonationController(DonationService donationService, ISecurity securityService) {
        this.donationService = donationService;
        this.securityService = securityService;
    }

//    @GetMapping("/donors/new")
//    public String showDonorForm(Model model) {
//        Donor donor = new Donor();
//        model.addAttribute("donor", donor);
//        return "views/donations/newDonor";
//    }

//    @PostMapping("/donors/save")
//    public String saveDonor(@ModelAttribute Donor donor, RedirectAttributes redirectAttributes) {
//        try {
//            donationService.createNewDonor(donor);
//            redirectAttributes.addFlashAttribute("toast", "Donor saved successfully.");
//            redirectAttributes.addFlashAttribute("toastType", "success");
//            return "redirect:/";
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("toast", "Failed to create a donor.");
//            redirectAttributes.addFlashAttribute("toastType", "error");
//            return "redirect:/donors/new";
//        }
//    }

    @GetMapping("/dashboard/donors")
    public String showDonorsDashboard(Model model) {
        List<Donor> donors = donationService.findAllDonors();
        model.addAttribute("donors", donors);
        return "views/donations/donors";
    }

    @GetMapping("/dashboard/donors/details/{id}")
    public String getDonorDetails(@PathVariable("id") Long id, Model model) {
        Donor donor = donationService.findDonorById(id).orElseThrow(() -> new IllegalArgumentException("Donor not found with id: " + id));
        model.addAttribute("donor", donor);
        return "views/donations/donorDetails";
    }

    @PostMapping("/dashboard/donors/{id}/update")
    public String updateDonorDetails(@PathVariable("id") Long id, @ModelAttribute Donor updatedDonor, RedirectAttributes redirectAttributes) {
        try {
            donationService.updateDonorById(id, updatedDonor);
            redirectAttributes.addFlashAttribute("toast", "Donor details updated successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to update donor details.");
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/dashboard/donors/details/" + id;
    }

    @GetMapping("/dashboard/donorAccountDetails")
    public String showDonorAccountDetails(Model model) {
        // tutaj moze przydalaby sie jakas metoda w ISecurity zeby brac id aktualnego usera ale na razie zostawiam
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = securityService.getUserByEmail(currentPrincipalName);
        Long id = user.map(User::getId).orElseThrow(() -> new IllegalArgumentException("User not found with email: " + currentPrincipalName));
        //
        Donor donor = donationService.findDonorById(id).orElseThrow(() -> new IllegalArgumentException("Donor not found with id: " + id));
        model.addAttribute("donor", donor);
        return "views/donations/donorAccountDetails";
    }

    @PostMapping("/dashboard/donorAccountDetails/update")
    public String updateDonorAccountDetails(@ModelAttribute Donor updatedDonor, RedirectAttributes redirectAttributes) {
        // tutaj moze przydalaby sie jakas metoda w ISecurity zeby brac id aktualnego usera ale na razie zostawiam
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = securityService.getUserByEmail(currentPrincipalName);
        Long id = user.map(User::getId).orElseThrow(() -> new IllegalArgumentException("User not found with email: " + currentPrincipalName));
        //
        try {
            donationService.updateDonorById(id, updatedDonor);
            redirectAttributes.addFlashAttribute("toast", "Donor details updated successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to update donor details.");
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/dashboard/donorAccountDetails";
    }

    @GetMapping("/donations/new")
    public String showDonationForm(Model model) {
        Donation donation = new Donation();
        model.addAttribute("donation", donation);
        return "views/donations/newDonation";
    }

    @PostMapping("/donations/save")
    public String saveDonation(@ModelAttribute Donation donation, RedirectAttributes redirectAttributes) {
        try {
            donation.setDonationDate(LocalDateTime.now());
            donation.setStatus(DonationStatus.PENDING);
            // tutaj moze przydalaby sie jakas metoda w ISecurity zeby brac id aktualnego usera ale na razie zostawiam
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Optional<User> user = securityService.getUserByEmail(currentPrincipalName);
            Long id = user.map(User::getId).orElseThrow(() -> new IllegalArgumentException("User not found with email: " + currentPrincipalName));
            //
            donation.setDonorId(id);
            donationService.createNewDonation(donation);
            redirectAttributes.addFlashAttribute("toast", "Donation saved successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to save donation.");
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/donations/new";
    }

    @GetMapping("/dashboard/donations")
    public String showDonationsDashboard(Model model) {
        List<Donation> donations = donationService.findAllDonations();
        model.addAttribute("donations", donations);
        return "views/donations/donations";
    }

    @GetMapping("/dashboard/donations/details/{id}")
    public String getDonationDetails(@PathVariable("id") Long id, Model model) {
        Donation donation = donationService.findDonationById(id).orElseThrow(() -> new IllegalArgumentException("Donation not found with id: " + id));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        model.addAttribute("formattedDonationDate", donation.getDonationDate().format(formatter));
        model.addAttribute("donation", donation);
        return "views/donations/donationDetails";
    }

    @PostMapping("/dashboard/donations/{id}/updateStatus")
    public String updateDonationStatus(@PathVariable("id") Long id, @RequestParam("newStatus") DonationStatus newStatus, RedirectAttributes redirectAttributes) {
        try {
            donationService.updateDonationStatusById(id, newStatus);
            redirectAttributes.addFlashAttribute("toast", "Donation status updated successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to update donation status.");
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/dashboard/donations/details/" + id;
    }
}
