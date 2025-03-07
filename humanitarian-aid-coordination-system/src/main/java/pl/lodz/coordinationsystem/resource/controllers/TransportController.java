package pl.lodz.coordinationsystem.resource.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lodz.coordinationsystem.resource.entity.TransportEntity;
import pl.lodz.coordinationsystem.resource.service.TransportService;
import pl.lodz.coordinationsystem.resource.service.VehicleService;

import java.util.List;

@Controller
public class TransportController {

    private final TransportService transportService;
    private final VehicleService vehicleService;

    public TransportController(TransportService transportService, VehicleService vehicleService) {
        this.transportService = transportService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/dashboard/transport")
    public String getTransport(Model model) {
        List<TransportEntity> allTransports = transportService.getAllTransportsRunning();
        model.addAttribute("allTransports", allTransports);
        return "views/resources/transports";
    }
    @PostMapping("/dashboard/transports/finish")

    public String finishTransport(@RequestParam("transportId") Long transportId) {
        System.out.println(transportId);
        transportService.finishTransport(transportId);
        TransportEntity byId = transportService.findById(transportId);
        vehicleService.setAvailable(byId.getVehicle().getId(), true);
        return "views/resources/success";
    }

}
