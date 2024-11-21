package org.example.user_task_manager.Service;

import org.example.user_task_manager.Entities.Task;
import org.example.user_task_manager.Repositories.TaskRepository;
import org.example.user_task_manager.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<Task> getTasksByUsername(String username, int page, int size) {
        return taskRepository.findByUserUsername(username, PageRequest.of(page, size));
    }

    public Page<Task> searchTasksByUsernameAndTitle(String username, String title, int page, int size) {
        return taskRepository.findByUserUsernameAndTitleContaining(username, title, PageRequest.of(page, size));
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}

