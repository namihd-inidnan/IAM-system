package com.example.iam.config;

import com.example.iam.repository.UserRepository;
import com.example.iam.security.CustomAccessDeniedHandler;
import com.example.iam.security.JwtAuthenticationFilter;
import com.example.iam.security.JwtUtil;
import com.example.iam.service.AuditService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            UserRepository userRepository,
            AuditService auditService,
            JwtUtil jwtUtil
    ) throws Exception {

        http
                // 🔥 ENABLE CORS (IMPORTANT)
                .cors(cors -> {})

                .csrf(csrf -> csrf.disable())

                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(new CustomAccessDeniedHandler(auditService))
                )

                .addFilterBefore(
                        new JwtAuthenticationFilter(userRepository, jwtUtil),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}