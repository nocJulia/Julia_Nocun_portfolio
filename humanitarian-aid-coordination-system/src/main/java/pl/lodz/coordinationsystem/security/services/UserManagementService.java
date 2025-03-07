//package pl.lodz.coordinationsystem.security.services;
//
//import pl.lodz.coordinationsystem.security.entity.BaseUserEntity;
//import pl.lodz.coordinationsystem.security.entity.RoleEntity;
//import pl.lodz.coordinationsystem.security.repositories.BaseUserRepository;
//import pl.lodz.coordinationsystem.security.repositories.RoleRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//public class UserManagementService {
//
//    private final BaseUserRepository baseUserRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public UserManagementService(
//            BaseUserRepository baseUserRepository,
//            RoleRepository roleRepository,
//            PasswordEncoder passwordEncoder
//    ) {
//        this.baseUserRepository = baseUserRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Transactional
//    public BaseUserEntity registerUser(BaseUserEntity user, String roleName) {
//        if (baseUserRepository.findByEmail(user.getEmail()).isPresent()) {
//            throw new RuntimeException("Użytkownik o podanym adresie email już istnieje");
//        }
//
//        RoleEntity role = roleRepository.findByRoleName(roleName);
//        if (role == null) {
//            throw new RuntimeException("Nie znaleziono roli: " + roleName);
//        }
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        user.setRole(role);
//
//        user.setActive(true);
//
//        return baseUserRepository.save(user);
//    }
//
//    @Transactional
//    public void deactivateUser(String email) {
//        Optional<BaseUserEntity> userOptional = baseUserRepository.findByEmail(email);
//        if (userOptional.isPresent()) {
//            userOptional.get().setActive(false);
//            baseUserRepository.save(userOptional.get());
//        }
//    }
//
//    @Transactional
//    public void changeUserRole(String email, String newRoleName) {
//        Optional<BaseUserEntity> userOptional = baseUserRepository.findByEmail(email);
//        RoleEntity newRole = roleRepository.findByRoleName(newRoleName);
//
//        if (userOptional.isEmpty()) {
//            throw new RuntimeException("Nie znaleziono użytkownika");
//        }
//
//        if (newRole == null) {
//            throw new RuntimeException("Nie znaleziono roli");
//        }
//
//        userOptional.get().setRole(newRole);
//        baseUserRepository.save(userOptional.get());
//    }
//}
