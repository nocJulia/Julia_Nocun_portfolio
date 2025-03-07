package pl.lodz.coordinationsystem.security.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.coordinationsystem.security.model.Role;
import pl.lodz.coordinationsystem.security.model.User;
import pl.lodz.coordinationsystem.security.repositories.BaseUserRepository;
import pl.lodz.coordinationsystem.security.repositories.RoleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ISecurityTest {

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ISecurity securityService;

    private User testUser;
    private Role adminRole;

    @BeforeEach
    public void setUp() {
        adminRole = securityService.getRoleByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException("Admin role not found"));

        testUser = new User(
                "Test",
                "Admin",
                "test.admin@example.com",
                "password123",
                adminRole,
                true,
                LocalDateTime.now()
        );
    }

    @Test
    public void getUsersByRole() {
        Long adminId = securityService.createAdmin(
                testUser.getFirstName(),
                testUser.getLastName(),
                testUser.getEmail(),
                testUser.getPassword(),
                "IT",
                1
        );
        assertNotNull(adminId);

        List<User> adminUsers = securityService.getUsersByRole("ADMIN");

        assertFalse(adminUsers.isEmpty());
        assertEquals(1, adminUsers.size());
        User foundUser = adminUsers.get(0);
        assertEquals(testUser.getEmail(), foundUser.getEmail());
        assertEquals("ADMIN", foundUser.getRole().getRoleName());
    }

    @Test
    public void getUserById_ExistingUser() {
        Long adminId = securityService.createAdmin(
                testUser.getFirstName(),
                testUser.getLastName(),
                testUser.getEmail(),
                testUser.getPassword(),
                "IT",
                1
        );

        Optional<User> result = securityService.getUserById(adminId);

        assertTrue(result.isPresent());
        assertEquals(testUser.getEmail(), result.get().getEmail());
    }

    @Test
    public void getUserById_NonExistingUser() {
        Optional<User> result = securityService.getUserById(999999L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getUserByEmail_ExistingUser() {
        securityService.createAdmin(
                testUser.getFirstName(),
                testUser.getLastName(),
                testUser.getEmail(),
                testUser.getPassword(),
                "IT",
                1
        );

        Optional<User> result = securityService.getUserByEmail(testUser.getEmail());

        assertTrue(result.isPresent());
        assertEquals(testUser.getEmail(), result.get().getEmail());
    }

    @Test
    public void getUserByEmail_NonExistingUser() {
        Optional<User> result = securityService.getUserByEmail("nonexistent@example.com");
        assertTrue(result.isEmpty());
    }

    @Test
    public void isUserAdmin_True() {
        securityService.createAdmin(
                testUser.getFirstName(),
                testUser.getLastName(),
                testUser.getEmail(),
                testUser.getPassword(),
                "IT",
                1
        );

        boolean isAdmin = securityService.isUserAdmin(testUser.getEmail());
        assertTrue(isAdmin);
    }

    @Test
    public void getRoleByName_ExistingRole() {
        Optional<Role> result = securityService.getRoleByName("ADMIN");

        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getRoleName());
    }

    @Test
    public void getRoleByName_NonExistingRole() {
        Optional<Role> result = securityService.getRoleByName("NON_EXISTENT_ROLE");
        assertTrue(result.isEmpty());
    }

    @Test
    public void createAdmin_Success() {
        Long adminId = securityService.createAdmin(
                testUser.getFirstName(),
                testUser.getLastName(),
                testUser.getEmail(),
                testUser.getPassword(),
                "IT",
                1
        );

        assertNotNull(adminId);
        Optional<User> createdAdmin = securityService.getUserById(adminId);
        assertTrue(createdAdmin.isPresent());
        assertEquals(testUser.getEmail(), createdAdmin.get().getEmail());
        assertEquals("ADMIN", createdAdmin.get().getRole().getRoleName());
    }

    @Test
    public void createAdmin_DuplicateEmail() {
        securityService.createAdmin(
                testUser.getFirstName(),
                testUser.getLastName(),
                testUser.getEmail(),
                testUser.getPassword(),
                "IT",
                1
        );

        assertThrows(IllegalArgumentException.class, () ->
                securityService.createAdmin(
                        "Another",
                        "Admin",
                        testUser.getEmail(), // Same email
                        "different_password",
                        "HR",
                        2
                )
        );
    }
}
