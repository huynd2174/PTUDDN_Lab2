package com.example.ptuddn_2.securingweb;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User userRequest) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("❌ Username already exists!");
        }

        // Nếu không có role nào được gửi, mặc định là USER
        Set<Role> userRoles = new HashSet<>();
        if (userRequest.getRoles() == null || userRequest.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("❌ Role 'USER' not found in database."));
            userRoles.add(userRole);
        } else {
            for (Role role : userRequest.getRoles()) {
                Role foundRole = roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new RuntimeException("❌ Role '" + role.getName() + "' not found in database."));
                userRoles.add(foundRole);
            }
        }

        // Tạo user mới
        User newUser = new User();
        newUser.setUsername(userRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword())); // Mã hóa password
        newUser.setRoles(userRoles);

        // Lưu vào database
        userRepository.save(newUser);
        return ResponseEntity.ok("✅ User registered successfully!");
    }
}
