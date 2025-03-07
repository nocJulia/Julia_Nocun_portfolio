package pl.lodz.coordinationsystem.resource.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.lodz.coordinationsystem.donation.model.Donation;
import pl.lodz.coordinationsystem.resource.entity.VehicleEntity;
import pl.lodz.coordinationsystem.resource.model.Vehicle;
import pl.lodz.coordinationsystem.resource.service.VehicleService;

import java.util.List;

@Controller
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService){
        this.vehicleService = vehicleService;
    }
    @GetMapping("/dashboard/vehicles/create")
    public String showCreateForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        return "views/resources/createVehicle";
    }

    @PostMapping("/dashboard/vehicles/create")
    public String createVehicle(@ModelAttribute Vehicle vehicle) {
        vehicleService.createVehicle(vehicle);
        return "views/resources/success";
    }

}
