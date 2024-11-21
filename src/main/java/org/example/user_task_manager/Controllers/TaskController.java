package org.example.user_task_manager.Controllers;

import org.example.user_task_manager.Entities.Task;
import org.example.user_task_manager.Service.TaskService;
import org.example.user_task_manager.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Displays a paginated list of tasks for the logged-in user.
     *
     * @param model          the model to pass data to the view
     * @param authentication the authentication object to get the logged-in user
     * @param page           the current page number (default is 0)
     * @param size           the number of tasks per page (default is 5)
     * @return the Thymeleaf template for the tasks list
     */
    @GetMapping("/tasks")
    public String listTasks(
            Model model,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String title // Optional search parameter
    ) {
        String username = authentication.getName();
        Page<Task> taskPage;

        if (title != null && !title.isEmpty()) {
            taskPage = taskService.searchTasksByUsernameAndTitle(username, title, page, size);
        } else {
            taskPage = taskService.getTasksByUsername(username, page, size);
        }

        model.addAttribute("taskPage", taskPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("searchTitle", title); // Retain search term in UI
        return "tasks";
    }



    /**
     * Shows the form to create a new task.
     *
     * @param model the model to pass data to the view
     * @return the Thymeleaf template for creating a new task
     */
    @GetMapping("/tasks/new")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "create_task";
    }

    /**
     * Handles the creation of a new task for the logged-in user.
     *
     * @param task           the task to be created
     * @param authentication the authentication object to get the logged-in user
     * @return a redirect to the tasks list
     */
    @PostMapping("/tasks")
    public String createTask(Task task, Authentication authentication) {
        String username = authentication.getName();
        task.setUser(userRepository.findByUsername(username));
        taskService.saveTask(task);
        return "redirect:/tasks";
    }

    /**
     * Handles the deletion of a task by ID.
     *
     * @param id the ID of the task to be deleted
     * @return a redirect to the tasks list
     */
    @GetMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return "redirect:/tasks";
    }
}
