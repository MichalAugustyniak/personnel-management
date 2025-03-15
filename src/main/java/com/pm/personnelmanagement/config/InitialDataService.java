package com.pm.personnelmanagement.config;

import com.pm.personnelmanagement.user.constant.DefaultRoleNames;
import com.pm.personnelmanagement.user.model.*;
import com.pm.personnelmanagement.user.repository.RoleRepository;
import com.pm.personnelmanagement.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class InitialDataService {
    private final AppConfigPropertyRepository appConfigPropertyRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialDataService(AppConfigPropertyRepository appConfigPropertyRepository, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.appConfigPropertyRepository = appConfigPropertyRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void insertInitialData() {
        AppConfigProperty appConfigProperty = new AppConfigProperty();
        appConfigProperty.setPropertyName(AppConfigProperties.IS_CONFIGURED);
        appConfigProperty.setPropertyValue(String.valueOf(true));
        appConfigPropertyRepository.save(appConfigProperty);
        AppConfigProperty logoAppConfigProperty = new AppConfigProperty();
        logoAppConfigProperty.setPropertyName(AppConfigProperties.LOGO_PATH);
        logoAppConfigProperty.setPropertyValue("");
        appConfigPropertyRepository.save(logoAppConfigProperty);

        User user = new User();
        user.setActive(true);
        user.setUuid(UUID.randomUUID());
        user.setLastLoginAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setBirthday(LocalDate.now());
        user.setSex(Sex.MALE);
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin"));

        Role role = new Role();
        role.setName(DefaultRoleNames.ADMIN);
        role.setUuid(UUID.randomUUID());
        user.setRole(role);

        Address address = new Address();
        address.setUuid(UUID.randomUUID());
        address.setStreetNumber("-1");
        address.setStreetName("-1");
        address.setCity("-1");
        address.setPostalCode("-1");
        address.setCountryIsoCode("-1");

        user.setAddress(address);

        userRepository.save(user);

        List<Role> roles = new LinkedList<>();
        Role managerRole = new Role();
        managerRole.setUuid(UUID.randomUUID());
        managerRole.setName(DefaultRoleNames.MANAGER);
        roles.add(managerRole);
        Role employeeRole = new Role();
        employeeRole.setUuid(UUID.randomUUID());
        employeeRole.setName(DefaultRoleNames.EMPLOYEE);
        roles.add(employeeRole);
        Role hrRole = new Role();
        hrRole.setUuid(UUID.randomUUID());
        hrRole.setName(DefaultRoleNames.HR);
        roles.add(hrRole);
        Role accountantRole = new Role();
        accountantRole.setUuid(UUID.randomUUID());
        accountantRole.setName(DefaultRoleNames.ACCOUNTANT);
        roles.add(accountantRole);
        roleRepository.saveAll(roles);
    }
}
