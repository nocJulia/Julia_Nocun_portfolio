package pl.lodz.coordinationsystem.resource.model;

import pl.lodz.coordinationsystem.maps.model.Location;
import pl.lodz.coordinationsystem.security.model.Role;
import pl.lodz.coordinationsystem.security.model.User;

import java.time.LocalDateTime;

public class Logistician extends User {
    public Logistician(Long id, String firstName, String lastName, String email, String password, Role role,
                       Boolean active, LocalDateTime createdAt, Location location) {
        super(id, firstName, lastName, email, password, role, active, createdAt);
        this.location = location;
    }

    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    //TODO Create minimal version of Logistician constructor
}
