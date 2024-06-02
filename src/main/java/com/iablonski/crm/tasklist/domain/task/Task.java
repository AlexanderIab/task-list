package com.iablonski.crm.tasklist.domain.task;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "tasks", schema = "demo")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private LocalDateTime expirationTime;

    @Column(name = "image")
    @CollectionTable(name = "tasks_images")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> images;
}