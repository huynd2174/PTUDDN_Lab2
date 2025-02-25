package com.example.ptuddn_2.securingweb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // ✅ Đảm bảo vai trò ADMIN & USER được tạo
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("USER"));
            roleRepository.save(new Role("ADMIN"));
        }

        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
        Role userRole = roleRepository.findByName("USER").orElseThrow();

        // ✅ Danh sách user cần thêm
        String[][] users = {
                {"admin", "admin123", "ADMIN"},
                {"user1", "user123", "USER"},
                {"user2", "password456", "USER"},
                {"user3", "securepass", "USER"},
                {"manager", "manager123", "ADMIN"},
                {"staff", "staff123", "USER"}
        };

        // ✅ Lặp qua danh sách để thêm user
        for (String[] userData : users) {
            String username = userData[0];
            String password = userData[1];
            String roleName = userData[2];

            Optional<User> existingUser = userRepository.findByUsername(username);
            if (existingUser.isEmpty()) {
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(passwordEncoder.encode(password));
                newUser.setRoles(Set.of(roleName.equals("ADMIN") ? adminRole : userRole));

                userRepository.save(newUser);
            }
        }
    }
}
