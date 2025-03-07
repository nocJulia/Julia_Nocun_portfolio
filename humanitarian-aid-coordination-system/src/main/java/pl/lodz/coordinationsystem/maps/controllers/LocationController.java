package pl.lodz.coordinationsystem.maps.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.coordinationsystem.maps.model.Location;
import pl.lodz.coordinationsystem.maps.model.LocationType;
import pl.lodz.coordinationsystem.maps.services.MapsService;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.notification.service.NotificationService;
import pl.lodz.coordinationsystem.resource.model.AllocatedResource;
import pl.lodz.coordinationsystem.resource.service.ResourcesService;
import pl.lodz.coordinationsystem.security.services.UserService;
import pl.lodz.coordinationsystem.volunteer.model.Volunteer;
import pl.lodz.coordinationsystem.volunteer.service.VolunteerService;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/locations")
public class LocationController {

    private NotificationService notificationService;
    private ResourcesService resourcesService;
    private VolunteerService volunteerService;

    public LocationController(NotificationService notificationService, ResourcesService resourcesService, VolunteerService volunteerService) {
        this.notificationService = notificationService;
        this.resourcesService = resourcesService;
        this.volunteerService = volunteerService;
    }

    @Autowired
    private MapsService locationService;

    @Autowired
    private UserService userService;

    @GetMapping("/getMap")
    public String listLocations(@RequestParam(value = "filter", required = false) String filter,
                                @RequestParam(value = "latitude", required = false) Double latitude,
                                @RequestParam(value = "longitude", required = false) Double longitude,
                                @RequestParam(value = "radius", required = false) Double radius,
                                Model model) {

        Iterable<Location> locations = locationService.getAllLocations();

        Stream<Location> stream = StreamSupport.stream(locations.spliterator(), false);
        if (filter != null && !filter.isEmpty()) {
            stream = stream.filter(location -> filter.equalsIgnoreCase(String.valueOf(location.getLocationType())));
        }

        if (latitude != null && longitude != null && radius != null) {
            stream = stream.filter(location -> {
                double distance = this.calculateDistance(latitude, longitude, location.getLatitude(), location.getLongitude());
                return distance <= radius;
            });
        }

        locations = stream.collect(Collectors.toList());
        model.addAttribute("locations", locations);
        model.addAttribute("filter", filter);
        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);
        model.addAttribute("radius", radius);

        return "views/maps/filteredMap";
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    @GetMapping("/newVolunteer")
    public String createVolunteerLocationForm(@RequestParam("volunteerId") Long volunteerId, Model model) {
        Volunteer volunteer = volunteerService.findVolunteerById(volunteerId).orElseThrow();
        model.addAttribute("modelId", volunteer.getId());
        Location location = volunteer.getLocation();
        if(location == null) {
            location = new Location();
        }
        location.setLocationType(LocationType.VOLUNTEER);
        model.addAttribute("location", location);
        return "views/maps/mapsForm";
    }

    @GetMapping("/newResource")
    public String createResourceLocationForm(@RequestParam("resourceId") Long resourceId, Model model) {
        AllocatedResource resource = resourcesService.getAllocatedResourceById(resourceId).orElseThrow();
        model.addAttribute("modelId", resourceId);
        Location location = new Location();
        location.setLocationType(LocationType.RESOURCE);
        model.addAttribute("location", location);
        return "views/maps/mapsForm";
    }

    @GetMapping("/newNotification")
    public String showLocationsForm(@RequestParam("notificationId") Long notificationId, Model model) {
        Notification notification = notificationService.findNotificationById(notificationId).orElseThrow();
        model.addAttribute("modelId", notification.getId());
        Location location = notification.getLocation();
        if(location == null) {
            location = new Location();
        }
        location.setLocationType(LocationType.NOTIFICATION);
        model.addAttribute("location", location);
        return "views/maps/mapsForm";
    }

    @PostMapping("/save")
    public String saveLocation(@ModelAttribute("location") Location location,
                               @RequestParam("modelId") Long modelId,
                               Model model, RedirectAttributes redirectAttributes) {

        location = locationService.getLocationById(locationService.saveLocation(location)).orElseThrow();

        switch (location.getLocationType()) {
            case LocationType.VOLUNTEER:
                volunteerService.setVolunteerLocation(modelId, location);
                redirectAttributes.addFlashAttribute("toast", "Registration successful!");
                redirectAttributes.addFlashAttribute("toastType", "success");
                break;
            case LocationType.RESOURCE:
                redirectAttributes.addFlashAttribute("toast", "New resource created successful!");
                redirectAttributes.addFlashAttribute("toastType", "success");
                return "redirect:/dashboard";
            case LocationType.NOTIFICATION:
                notificationService.setLocationToNotification(modelId, location.getLatitude(), location.getLongitude());
                redirectAttributes.addFlashAttribute("toast", "Notification created successfully!");
                redirectAttributes.addFlashAttribute("toastType", "success");
                break;
        }
        return "redirect:/";
    }
}
