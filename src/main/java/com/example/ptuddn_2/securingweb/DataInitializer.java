package com.example.ptuddn_2.securingweb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
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

        // ✅ Thêm ADMIN nếu chưa tồn tại
        Optional<User> existingAdmin = userRepository.findByUsername("admin");
        if (existingAdmin.isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole)); // Gán vai trò ADMIN cho admin user

            userRepository.save(admin);
        }

        // ✅ Thêm USER nếu chưa tồn tại
        Optional<User> existingUser = userRepository.findByUsername("user1");
        if (existingUser.isEmpty()) {
            User user1 = new User();
            user1.setUsername("user1");
            user1.setPassword(passwordEncoder.encode("user123"));
            user1.setRoles(Set.of(userRole));  // Gán vai trò USER cho user1

            userRepository.save(user1);
        }

        // ✅ Thêm một user có nhiều vai trò (ADMIN & USER)
        Optional<User> existingSuperUser = userRepository.findByUsername("superuser");
        if (existingSuperUser.isEmpty()) {
            User superuser = new User();
            superuser.setUsername("superuser");
            superuser.setPassword(passwordEncoder.encode("super123"));

            // Gán nhiều vai trò cho user (USER & ADMIN)
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);  // ADMIN
            roles.add(userRole);    // USER
            superuser.setRoles(roles);

            userRepository.save(superuser);
        }
    }
}
