package com.pm.personnelmanagement.user.service;

import com.pm.personnelmanagement.task.model.Task;
import com.pm.personnelmanagement.user.model.*;
import com.pm.personnelmanagement.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void saveUser() {
    }

    @Test
    public void createTask() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) throw new RuntimeException();
        User user = users.getFirst();
        Task task = new Task();
        task.getUsers().add(user);
        task.setName("test_task");
        task.setCreatedBy(user);
        task.setDescription("test_desc");
        task.setStartDateTime(LocalDateTime.now());
        task.setEndDateTime(LocalDateTime.now());
        task.setUuid(UUID.randomUUID());
    }
}