package pl.lodz.coordinationsystem.security.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.coordinationsystem.donation.model.Donor;
import pl.lodz.coordinationsystem.security.model.Role;
import pl.lodz.coordinationsystem.security.model.User;
import pl.lodz.coordinationsystem.security.services.UserService;
import pl.lodz.coordinationsystem.volunteer.model.Volunteer;
import pl.lodz.coordinationsystem.donation.service.IDonations;
import pl.lodz.coordinationsystem.volunteer.service.IVolunteer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class SecurityController {

    private final UserService userService;
    private final IDonations donationService;
    private final IVolunteer volunteerService;

    public SecurityController(UserService userService,
                              IDonations donationService,
                              IVolunteer volunteerService) {
        this.userService = userService;
        this.donationService = donationService;
        this.volunteerService = volunteerService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "views/security/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "views/security/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, @RequestParam String roles, RedirectAttributes redirectAttributes) {
        try {
            switch (roles) {
                case "DONOR" -> {
                    Donor donor = new Donor();
                    donor.setFirstName(user.getFirstName());
                    donor.setLastName(user.getLastName());
                    donor.setEmail(user.getEmail());
                    donor.setPassword(user.getPassword());
                    donationService.createNewDonor(donor);
                }
                case "VOLUNTEER" -> {
                    Volunteer volunteer = new Volunteer();
                    volunteer.setFirstName(user.getFirstName());
                    volunteer.setLastName(user.getLastName());
                    volunteer.setEmail(user.getEmail());
                    volunteer.setPassword(user.getPassword());
                    Long id = volunteerService.createVolunteer(volunteer);
                    return "redirect:/locations/newVolunteer?volunteerId=" + id;
                }
                case "ADMIN" -> {
                    userService.createAdmin(
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            user.getPassword(),
                            "IT",
                            1
                    );
                }
                default -> throw new IllegalArgumentException("Unsupported role: " + roles);
            }

            redirectAttributes.addFlashAttribute("toast", "Registration successful!");
            redirectAttributes.addFlashAttribute("toastType", "success");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Registration failed: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/register";
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "views/security/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "views/security/reset-password";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional = userService.getUserByEmail(authentication.getName());

        if (userOptional.isEmpty()) {
            return "redirect:/login";
        }

        model.addAttribute("user", userOptional.get());
        return "views/security/profile";
    }

    @GetMapping("/dashboard/users")
    public String showUserManagement(Model model) {
        // Create a new mutable ArrayList
        List<User> users = new ArrayList<>();

        // Add users from different roles
        users.addAll(userService.getUsersByRole("ADMIN"));
        users.addAll(userService.getUsersByRole("VOLUNTEER"));
        users.addAll(userService.getUsersByRole("DONOR"));
        users.addAll(userService.getUsersByRole("COORDINATOR"));
        users.addAll(userService.getUsersByRole("LOGISTICIAN"));
        users.addAll(userService.getUsersByRole("AUTHORITY"));

        List<Role> availableRoles = List.of(
                userService.getRoleByName("ADMIN").orElseThrow(),
                userService.getRoleByName("VOLUNTEER").orElseThrow(),
                userService.getRoleByName("DONOR").orElseThrow(),
                userService.getRoleByName("COORDINATOR").orElseThrow(),
                userService.getRoleByName("LOGISTICIAN").orElseThrow(),
                userService.getRoleByName("AUTHORITY").orElseThrow()
        );

        model.addAttribute("users", users);
        model.addAttribute("availableRoles", availableRoles);
        return "views/security/users";
    }

    @PostMapping("/dashboard/users/{id}/update")
    public String updateUser(@PathVariable Long id, @RequestParam String role, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserRole(id, role);
            redirectAttributes.addFlashAttribute("toast", "User role updated successfully!");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to update user role: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/dashboard/users";
    }

    @PostMapping("/dashboard/users/{id}/deactivate")
    public String deactivateUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deactivateUser(id);
            redirectAttributes.addFlashAttribute("toast", "User deactivated successfully!");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to deactivate user: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/dashboard/users";
    }

    @PostMapping("/dashboard/users/{id}/activate")
    public String activateUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.activateUser(id);
            redirectAttributes.addFlashAttribute("toast", "User activated successfully!");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to activate user: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/dashboard/users";
    }

    @PostMapping("/profile/deactivate")
    public String deactivateCurrentUser(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            // Deactivate user
            userService.deactivateCurrentUser(authentication.getName());

            // Perform logout
            SecurityContextHolder.clearContext();
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // Add success message
            redirectAttributes.addFlashAttribute("toast", "Account deactivated successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to deactivate account: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/profile";
        }
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam String email,
                                       @RequestParam String firstName,
                                       @RequestParam String lastName,
                                       @RequestParam(required = false) String newPassword,
                                       @RequestParam(required = false) String confirmPassword,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        try {
            Optional<User> userOptional = userService.getUserByEmail(email);
            if (userOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No account found with this email address.");
                return "redirect:/forgot-password";
            }

            User user = userOptional.get();

            // Check if first name and last name match
            if (!user.getFirstName().equals(firstName) || !user.getLastName().equals(lastName)) {
                redirectAttributes.addFlashAttribute("error", "The provided information does not match our records.");
                return "redirect:/forgot-password";
            }

            // If new password is being set
            if (newPassword != null && !newPassword.isEmpty()) {
                if (!newPassword.equals(confirmPassword)) {
                    redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
                    redirectAttributes.addFlashAttribute("success", true);
                    redirectAttributes.addFlashAttribute("email", email);
                    redirectAttributes.addFlashAttribute("firstName", firstName);
                    redirectAttributes.addFlashAttribute("lastName", lastName);
                    return "redirect:/forgot-password";
                }

                try {
                    userService.updateUserPassword(user.getId(), newPassword);
                    redirectAttributes.addFlashAttribute("toast", "Password has been updated successfully.");
                    redirectAttributes.addFlashAttribute("toastType", "success");
                    return "redirect:/login";
                } catch (IllegalArgumentException e) {
                    redirectAttributes.addFlashAttribute("error", e.getMessage());
                    redirectAttributes.addFlashAttribute("success", true);
                    redirectAttributes.addFlashAttribute("email", email);
                    redirectAttributes.addFlashAttribute("firstName", firstName);
                    redirectAttributes.addFlashAttribute("lastName", lastName);
                    return "redirect:/forgot-password";
                }
            }

            // If just validating user info
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("firstName", firstName);
            redirectAttributes.addFlashAttribute("lastName", lastName);
            return "redirect:/forgot-password";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred. Please try again.");
            return "redirect:/forgot-password";
        }
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            userService.changePassword(authentication.getName(), currentPassword, newPassword, confirmNewPassword);
            redirectAttributes.addFlashAttribute("toast", "Password changed successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to change password: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            userService.updateUserProfile(authentication.getName(), user);
            redirectAttributes.addFlashAttribute("toast", "Profile updated successfully.");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", "Failed to update profile: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/profile";
    }
}
