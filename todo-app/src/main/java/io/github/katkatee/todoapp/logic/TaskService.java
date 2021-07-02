package io.github.katkatee.todoapp.logic;

import io.github.katkatee.todoapp.model.Task;
import io.github.katkatee.todoapp.model.TaskRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
  public static final Logger logger = LoggerFactory.getLogger(TaskService.class);

  private final TaskRepository repository;

  public TaskService(TaskRepository repository) {
    this.repository = repository;
  }

  public CompletableFuture<List<Task>> findAllAsync() {
    logger.info("Async!");
    return CompletableFuture.supplyAsync(repository::findAll);
  }
}
