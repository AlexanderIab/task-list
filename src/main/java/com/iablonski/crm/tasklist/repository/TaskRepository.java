package com.iablonski.crm.tasklist.repository;

import com.iablonski.crm.tasklist.domain.task.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskRepository {
    Optional<Task> findById(Long id);
    List<Task> findAllByUserId(Long userId);
    void assignToUserById(@Param("taskId") Long taskId, @Param("userId") Long userId);
    void update(Task task);
    void create(Task task);
    void delete(Long id);
}
