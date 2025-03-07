package pl.lodz.coordinationsystem.security.services;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import pl.lodz.coordinationsystem.security.entity.BaseUserEntity;
import pl.lodz.coordinationsystem.security.repositories.BaseUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final BaseUserRepository baseUserRepository;

    public CustomUserDetailsService(BaseUserRepository baseUserRepository) {
        this.baseUserRepository = baseUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<BaseUserEntity> userOptional = baseUserRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found");
        }

        BaseUserEntity user = userOptional.get();

        // Check if account is active
        if (!user.getActive()) {
            throw new DisabledException("Account is deactivated");
        }

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().getRoleName())
                .disabled(!user.getActive()) // Set the disabled flag based on active status
                .build();
    }
}
