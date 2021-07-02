package io.github.katkatee.todoapp.controller;

import io.github.katkatee.todoapp.logic.TaskService;
import io.github.katkatee.todoapp.model.Task;
import io.github.katkatee.todoapp.model.TaskRepository;
import java.net.URI;
import java.util.List;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    private final TaskService service;


    TaskController(TaskRepository repository, TaskService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping(params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        logger.info("Task id:" + id + " was updated.");
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    repository.save(task);
                });
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> getById(@PathVariable int id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTask(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(
            repository.findByDone(state)
        );
    }

    @PostMapping
    ResponseEntity<Task> create(@RequestBody @Valid Task toCreate) {
        logger.info("Created new task");
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        logger.info("Task id:" + id + " filed 'done' was updated.");
        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }



}
