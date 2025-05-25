package com.pm.personnelmanagement.config;

import com.pm.personnelmanagement.user.constant.DefaultRoleNames;
import com.pm.personnelmanagement.user.service.UserService;
import com.pm.personnelmanagement.user.util.UserUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    @Value("${app.client.url}")
    private String clientUrl;

    @Bean
    public UserDetailsService userService(UserUtils userUtils) {
        return new UserDetailsServiceImpl(userUtils);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(c -> corsConfigurationSource())
                .authorizeHttpRequests(authorize -> {
            authorize.requestMatchers(HttpMethod.POST, "/api/users").hasRole(DefaultRoleNames.ADMIN)
                    .requestMatchers(HttpMethod.PATCH, "/api/users/**").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/users/**").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/users").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/addresses").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/addresses/").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/addresses").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.HR)
                    .requestMatchers(HttpMethod.GET, "/api/tasks").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/tasks/**").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/tasks").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.MANAGER)
                    .requestMatchers(HttpMethod.POST, "/api/tasks/**").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.MANAGER)
                    .requestMatchers(HttpMethod.PATCH, "/api/tasks").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.MANAGER)
                    .requestMatchers(HttpMethod.PATCH, "/api/tasks/**").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.MANAGER)
                    .requestMatchers(HttpMethod.DELETE, "/api/tasks").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.MANAGER)
                    .requestMatchers(HttpMethod.DELETE, "/api/tasks/**").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.MANAGER)
                    .requestMatchers(HttpMethod.GET, "/api/shift-types", "/api/shift-types/**").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/shift-types", "/api/shift-types/**").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.HR)
                    .requestMatchers(HttpMethod.PATCH, "/api/shift-types", "/api/shift-types/**").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.HR)
                    .requestMatchers(HttpMethod.DELETE, "/api/shift-types", "/api/shift-types/**").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.HR)
                    .requestMatchers(HttpMethod.GET, "/api/schedules", "/api/schedules/**").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/schedules", "/api/schedules/**").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.HR)
                    .requestMatchers(HttpMethod.PATCH, "/api/schedules", "/api/schedules/**").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.HR)
                    .requestMatchers(HttpMethod.GET, "/api/attendance-statuses", "/api/attendance-statuses/**").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/attendance-statuses", "/api/attendance-statuses/**").hasAnyRole(DefaultRoleNames.ADMIN)
                    .requestMatchers(HttpMethod.PATCH, "/api/attendance-statuses", "/api/attendance-statuses/**").hasAnyRole(DefaultRoleNames.ADMIN)
                    .requestMatchers(HttpMethod.DELETE, "/api/attendance-statuses", "/api/attendance-statuses/**").hasAnyRole(DefaultRoleNames.ADMIN)
                    .requestMatchers(HttpMethod.GET, "/api/attendances", "/api/attendances/**").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/attendances", "/api/attendances/**").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.MANAGER)
                    .requestMatchers(HttpMethod.PATCH, "/api/attendances", "/api/attendances/**").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.MANAGER)
                    .requestMatchers(HttpMethod.DELETE, "/api/attendances", "/api/attendances/**").hasAnyRole(DefaultRoleNames.ADMIN, DefaultRoleNames.MANAGER)
                    .requestMatchers(HttpMethod.POST, "/api/config/**").hasRole(DefaultRoleNames.ADMIN)
                    .requestMatchers(HttpMethod.GET, "/uploads/**", "/api/config/logo").permitAll()
                    .requestMatchers(HttpMethod.POST, "/login").permitAll()
                    .requestMatchers("/session").permitAll()
                    .anyRequest().authenticated();
        });
        http.sessionManagement(session -> {
            session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        });
        http.authenticationManager(authenticationManager)
                .httpBasic(basic -> {
                    basic.securityContextRepository(securityContextRepository);
                });
        http.securityContext(security -> {
            security.securityContextRepository(securityContextRepository);
        });
        return http.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Collections.singletonList(clientUrl));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider daoAuthenticationProvider) {
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(clientUrl)
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
