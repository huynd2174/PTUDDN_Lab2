package com.example.ptuddn_2.securingweb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ✅ Nếu bạn muốn cho phép logout bằng GET
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/auth/register").permitAll() // Cho phép trang chủ và trang đăng ký
                        .requestMatchers("/admin/**").hasAuthority("ADMIN") // Quản lý quyền cho admin
                        .requestMatchers("/user/**").hasAuthority("USER") // Quản lý quyền cho user
                        .anyRequest().authenticated() // Các trang còn lại cần đăng nhập
                )
                .formLogin(login -> login
                        .loginPage("/login") // Chỉ định trang login
                        .defaultSuccessUrl("/home", true) // Điều hướng về trang home sau khi đăng nhập
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Định nghĩa URL logout
                        .logoutSuccessUrl("/login?logout") // Chuyển hướng sau khi logout
                        .invalidateHttpSession(true) // Hủy session khi logout
                        .deleteCookies("JSESSIONID") // Xóa cookie đăng nhập
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Mã hóa mật khẩu với BCrypt
    }
}
