package org.example.user_task_manager.Repositories;

import org.example.user_task_manager.Entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.user.username = :username AND LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Task> findByUserUsernameAndTitleContaining(
            @Param("username") String username,
            @Param("title") String title,
            Pageable pageable
    );

    @Query("SELECT t FROM Task t WHERE t.user.username = :username")
    Page<Task> findByUserUsername(@Param("username") String username, Pageable pageable);



}
