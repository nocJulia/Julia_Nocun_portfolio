package pl.lodz.coordinationsystem.security.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.coordinationsystem.donation.entity.DonorEntity;
import pl.lodz.coordinationsystem.security.entity.AdminEntity;
import pl.lodz.coordinationsystem.security.entity.BaseUserEntity;
import pl.lodz.coordinationsystem.security.entity.RoleEntity;
import pl.lodz.coordinationsystem.security.model.Role;
import pl.lodz.coordinationsystem.security.model.User;
import pl.lodz.coordinationsystem.security.repositories.BaseUserRepository;
import pl.lodz.coordinationsystem.security.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import pl.lodz.coordinationsystem.volunteer.entity.VolunteerEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements ISecurity {
    private final BaseUserRepository baseUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(BaseUserRepository baseUserRepository,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.baseUserRepository = baseUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getUsersByRole(String roleName) {
        RoleEntity role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            throw new IllegalArgumentException("Role not found: " + roleName);
        }
        return baseUserRepository.findByRole(role)
                .stream()
                .map(UserMapper::toModel)
                .toList();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        Optional<BaseUserEntity> userOptional = baseUserRepository.findById(id);
        return userOptional.map(UserMapper::toModel);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<BaseUserEntity> userOptional = baseUserRepository.findByEmail(email);
        return userOptional.map(UserMapper::toModel);
    }

    @Override
    public boolean isUserAdmin(String email) {
        Optional<User> userOptional = getUserByEmail(email);
        return userOptional.isPresent() && "ADMIN".equals(userOptional.get().getRole().getRoleName());
    }

    @Override
    public Optional<Role> getRoleByName(String roleName) {
        return Optional.ofNullable(roleRepository.findByRoleName(roleName))
                .map(RoleMapper::toModel);
    }

    @Override
    @Transactional
    public Long createAdmin(String firstName, String lastName, String email, String password,
                            String department, Integer permissionLevel) {
        if (baseUserRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Admin with email " + email + " already exists");
        }

        Role adminRole = getRoleByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException("Admin role not found"));

        AdminEntity admin = new AdminEntity(
                null,
                firstName,
                lastName,
                email,
                passwordEncoder.encode(password),
                true,
                LocalDateTime.now(),
                RoleMapper.toEntity(adminRole),
                department,
                permissionLevel
        );

        return baseUserRepository.save(admin).getId();
    }

    @Override
    @Transactional
    public void updateUserRole(Long userId, String newRoleName) {
        BaseUserEntity user = baseUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        RoleEntity newRole = roleRepository.findByRoleName(newRoleName);
        if (newRole == null) {
            throw new IllegalArgumentException("Role not found: " + newRoleName);
        }

        user.setRole(newRole);
        baseUserRepository.save(user);
    }

    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        BaseUserEntity user = baseUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.setActive(false);
        baseUserRepository.save(user);
    }

    @Override
    @Transactional
    public void activateUser(Long userId) {
        BaseUserEntity user = baseUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        user.setActive(true);
        baseUserRepository.save(user);
    }

    @Override
    @Transactional
    public void deactivateCurrentUser(String email) {
        BaseUserEntity user = baseUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        user.setActive(false);
        baseUserRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new IllegalArgumentException("New passwords don't match");
        }

        if (!newPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and include at least one letter, one number, and one special character");
        }

        BaseUserEntity user = baseUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        baseUserRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserProfile(String currentUserEmail, User updatedUser) {
        BaseUserEntity user = baseUserRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Validate input
        if (updatedUser.getFirstName() == null || updatedUser.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (updatedUser.getLastName() == null || updatedUser.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }

        // Update fields
        user.setFirstName(updatedUser.getFirstName().trim());
        user.setLastName(updatedUser.getLastName().trim());

        // Only update email if it's provided and different from current
        String newEmail = updatedUser.getEmail();
        if (newEmail != null && !newEmail.trim().isEmpty() && !currentUserEmail.equals(newEmail.trim())) {
            if (baseUserRepository.findByEmail(newEmail.trim()).isPresent()) {
                throw new IllegalArgumentException("Email already in use");
            }
            user.setEmail(newEmail.trim());
        }

        baseUserRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserPassword(Long userId, String newPassword) {
        BaseUserEntity user = baseUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Validate new password
        if (!newPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and include at least one letter, one number, and one special character");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        baseUserRepository.save(user);
    }

    @Override
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            return baseUserRepository.findByEmail(principal.getUsername()).orElseThrow().getId();
        }
        return null;
    }
}
