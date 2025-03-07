package pl.lodz.coordinationsystem.security.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
public class AdminEntity extends BaseUserEntity {
    @Column(name = "department")
    private String department;

    @Column(name = "permission_level")
    private Integer permissionLevel;

    public AdminEntity() {}

    public AdminEntity(Long id, String firstName, String lastName, String email, String password, Boolean active,
                       LocalDateTime createdAt, RoleEntity role, String department, Integer permissionLevel) {
        super(id, firstName, lastName, email, password, active, createdAt, role);
        this.department = department;
        this.permissionLevel = permissionLevel;
    }

    public AdminEntity(String firstName, String lastName, String email) {
        super(null, firstName, lastName, email, null, true, LocalDateTime.now(), null);
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getPermissionLevel() { return permissionLevel; }
    public void setPermissionLevel(Integer permissionLevel) { this.permissionLevel = permissionLevel; }
}
