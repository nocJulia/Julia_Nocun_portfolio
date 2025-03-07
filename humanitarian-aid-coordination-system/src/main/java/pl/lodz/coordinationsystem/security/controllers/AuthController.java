package pl.lodz.coordinationsystem.security.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "views/dashboard";
    }
}
