package com.pm.personnelmanagement.task.model;

import com.pm.personnelmanagement.taskevent.model.TaskEvent;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer color;

    @ElementCollection
    @CollectionTable(
            name = "task_user",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name = "user_id")
    private Set<String> users = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TaskEvent taskEvent;
}
