package com.iablonski.crm.tasklist.domain.user;

import com.iablonski.crm.tasklist.domain.task.Task;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users", schema = "demo")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String username;
    private String password;
    @Transient
    private String passwordConfirmation;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles;
    @CollectionTable(name = "user_task")
    @OneToMany
    @JoinColumn(name = "task_id")
    private List<Task> tasks;
}