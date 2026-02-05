package com.attendance.tracker.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    // ===============================
    // 🔐 SECURITY FILTER CHAIN
    // ===============================
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authenticationProvider(authenticationProvider())

                .authorizeHttpRequests(auth -> auth

                        // ✅ Allow login page + static files
                        .requestMatchers(
                                "/login.html",
                                "/login.css",
                                "/login.js"
                        ).permitAll()
                        .requestMatchers("/api/me").authenticated()
                        // ✅ Allow dashboards (after login)
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/teacher/**").hasRole("TEACHER")

                        // ✅ API Role Protection
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/attendance/**").hasRole("TEACHER")

                        .anyRequest().authenticated()
                )

                // ✅ FORM LOGIN
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")

                        .successHandler((request, response, authentication) -> {

                            String loginType = request.getParameter("loginType");

                            String role = authentication.getAuthorities()
                                    .iterator().next().getAuthority();

                            // 🔥 Role validation
                            if ("ADMIN".equals(loginType) && !role.equals("ROLE_ADMIN")) {
                                response.sendRedirect("/login.html?error=admin");
                                return;
                            }

                            if ("TEACHER".equals(loginType) && !role.equals("ROLE_TEACHER")) {
                                response.sendRedirect("/login.html?error=teacher");
                                return;
                            }

                            // 🔥 Redirect dashboards
                            if (role.equals("ROLE_ADMIN")) {
                                response.sendRedirect("/admin/dashboard.html");
                            } else {
                                response.sendRedirect("/teacher/dashboard.html");
                            }
                        })

                        .failureUrl("/login.html?error=invalid")
                        .permitAll()
                )

                // ✅ Disable Spring popup login
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    // ===============================
    // 🔐 AUTH PROVIDER
    // ===============================
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    // ===============================
    // 🔐 AUTH MANAGER
    // ===============================
    @Bean
    public AuthenticationManager authenticationManager(
            DaoAuthenticationProvider provider) {

        return new ProviderManager(provider);
    }

    // ===============================
    // 🔐 PASSWORD ENCODER
    // ===============================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
