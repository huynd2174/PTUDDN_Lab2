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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ✅ Nếu bạn muốn cho phép logout bằng GET
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/auth/register").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/user/**").hasAuthority("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler()) // ✅ Điều hướng theo quyền
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // ✅ Định nghĩa URL logout
                        .logoutSuccessUrl("/login?logout") // ✅ Chuyển hướng sau khi logout
                        .invalidateHttpSession(true) // ✅ Hủy session khi logout
                        .deleteCookies("JSESSIONID") // ✅ Xóa cookie đăng nhập
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
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            authentication.getAuthorities().forEach(grantedAuthority -> {
                try {
                    if (grantedAuthority.getAuthority().equals("ADMIN")) {
                        response.sendRedirect("/admin/dashboard"); // ✅ ADMIN vào admin dashboard
                    } else if (grantedAuthority.getAuthority().equals("USER")) {
                        response.sendRedirect("/user/dashboard"); // ✅ USER vào user dashboard
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        };
    }
}
