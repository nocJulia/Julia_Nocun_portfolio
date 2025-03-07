package pl.lodz.coordinationsystem.communication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.coordinationsystem.communication.entity.MessageStatusEntity;
import pl.lodz.coordinationsystem.communication.model.Message;
import pl.lodz.coordinationsystem.communication.model.MessageStatus;
import pl.lodz.coordinationsystem.communication.service.IActors;
import pl.lodz.coordinationsystem.security.model.Role;
import pl.lodz.coordinationsystem.security.model.User;
import pl.lodz.coordinationsystem.security.services.ISecurity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private final IActors actorsService;
    private final ISecurity securityService;

    public MessageController(IActors actorsService, ISecurity securityService) {
        this.actorsService = actorsService;
        this.securityService = securityService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String showMessagesMenu() {
        return "views/messages/messagesMenu";
    }

    @GetMapping("/new")
    public String showMessageForm(Model model) {
        Message message = new Message();
        model.addAttribute("message", message);
        return "views/messages/newMessage";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/save")
    public String saveMessage(@ModelAttribute Message message, RedirectAttributes redirectAttributes) {
        Logger logger = LoggerFactory.getLogger(MessageController.class);
        try {
            Optional<Role> roleOptional = securityService.getRoleByName(message.getRole().getRoleName());
            if (roleOptional.isPresent()) {
                message.setRole(roleOptional.get());
                message.setDate(LocalDateTime.now());
                actorsService.addMessageForRole(message);
                redirectAttributes.addFlashAttribute("toast", "Message sent successfully.");
                redirectAttributes.addFlashAttribute("toastType", "success");
            } else {
                redirectAttributes.addFlashAttribute("toast", "Role not found.");
                redirectAttributes.addFlashAttribute("toastType", "error");
            }
        } catch (Exception e) {
            logger.error("Failed to send message", e);
            redirectAttributes.addFlashAttribute("toast", "Failed to send message.");
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/messages/new";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public String showUserMessages(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = securityService.getUserByEmail(currentPrincipalName);

        if (user.isPresent()) {
            List<Message> messages = actorsService.readMessagesForUser(user.get().getId());

            // Pobieramy statusy wiadomości
            Map<Long, MessageStatus> messageStatuses = actorsService.readMessageStatusesForUser(user.get().getId())
                    .stream()
                    .collect(Collectors.toMap(ms -> ms.getMessage().getId(), MessageStatusEntity::getStatus));

            // Sortowanie wiadomości
            List<Message> sortedMessages = messages.stream()
                    .sorted((m1, m2) -> {
                        // Jeśli jedna z wiadomości jest UNREAD, a druga nie, ta z UNREAD idzie na górę
                        if (messageStatuses.get(m1.getId()) == MessageStatus.UNREAD && messageStatuses.get(m2.getId()) != MessageStatus.UNREAD) {
                            return -1;
                        } else if (messageStatuses.get(m1.getId()) != MessageStatus.UNREAD && messageStatuses.get(m2.getId()) == MessageStatus.UNREAD) {
                            return 1;
                        } else {
                            // Jeśli obie mają ten sam status, sortujemy po dacie, najnowsze na górze
                            return m2.getDate().compareTo(m1.getDate());
                        }
                    })
                    .collect(Collectors.toList());

            // Dodajemy posortowane wiadomości do modelu
            model.addAttribute("messages", sortedMessages);
            model.addAttribute("messageStatuses", messageStatuses);
            return "views/messages/userMessages";
        } else {
            model.addAttribute("error", "User not found.");
            return "error";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/status/update")
    public String updateMessageStatus(@RequestParam Long messageId, @RequestParam MessageStatus status, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = securityService.getUserByEmail(currentPrincipalName);

        if (user.isPresent()) {
            try {
                actorsService.updateMessageStatus(messageId, user.get().getId(), status);
                redirectAttributes.addFlashAttribute("toast", "Message status updated successfully.");
                redirectAttributes.addFlashAttribute("toastType", "success");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("toast", "Failed to update message status.");
                redirectAttributes.addFlashAttribute("toastType", "error");
            }
        } else {
            redirectAttributes.addFlashAttribute("toast", "User not found.");
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/messages/user";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{messageId}")
    @ResponseBody
    public Message getMessageContent(@PathVariable Long messageId) {
        return actorsService.getMessageById(messageId);
    }
}