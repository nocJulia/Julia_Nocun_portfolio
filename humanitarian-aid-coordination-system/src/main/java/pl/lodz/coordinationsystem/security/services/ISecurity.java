package pl.lodz.coordinationsystem.security.services;

import pl.lodz.coordinationsystem.security.model.Role;
import pl.lodz.coordinationsystem.security.model.User;

import java.util.List;
import java.util.Optional;

public interface ISecurity {
    List<User> getUsersByRole(String roleName);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    Optional<Role> getRoleByName(String roleName);
    boolean isUserAdmin(String email);
    Long createAdmin(String firstName, String lastName, String email, String password,
                     String department, Integer permissionLevel);
    void updateUserRole(Long userId, String newRoleName);
    void deactivateUser(Long userId);
    void activateUser(Long userId);
    void deactivateCurrentUser(String email);
    void changePassword(String email, String currentPassword, String newPassword, String confirmNewPassword);
    void updateUserProfile(String currentUserEmail, User updatedUser);
    void updateUserPassword(Long userId, String newPassword);
    Long getCurrentUserId();
}
