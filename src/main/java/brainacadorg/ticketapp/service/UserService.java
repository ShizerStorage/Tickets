package brainacadorg.ticketapp.service;

import brainacadorg.ticketapp.model.Role;
import brainacadorg.ticketapp.model.User;
import brainacadorg.ticketapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final UserRepository userRepository = null;
    private final PasswordEncoder passwordEncoder;

    public static User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));
    }

    public User registerUser(String email, String password) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }


}