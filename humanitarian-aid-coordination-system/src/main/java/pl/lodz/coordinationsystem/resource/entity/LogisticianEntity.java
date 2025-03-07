package pl.lodz.coordinationsystem.resource.entity;

import jakarta.persistence.*;
import pl.lodz.coordinationsystem.maps.entity.LocationEntity;
import pl.lodz.coordinationsystem.security.entity.BaseUserEntity;
import pl.lodz.coordinationsystem.security.entity.RoleEntity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("LOGISTICIAN")
@Table(name = "logisticians")
public class LogisticianEntity extends BaseUserEntity {
    public LogisticianEntity(Long id, String firstName, String lastName, String email, String password,
                             Boolean active, LocalDateTime createdAt, RoleEntity role, LocationEntity location) {
        super(id, firstName, lastName, email, password, active, createdAt, role);
        this.location = location;
    }
    @OneToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public LogisticianEntity() {

    }
}
