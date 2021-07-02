package io.github.katkatee.todoapp.logic;

import io.github.katkatee.todoapp.TaskConfigurationProperties;
import io.github.katkatee.todoapp.model.ProjectRepository;
import io.github.katkatee.todoapp.model.TaskGroupRepository;
import io.github.katkatee.todoapp.model.TaskRepository;
import org.springframework.context.annotation.Bean;

public class LogicConfiguration {

    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskConfigurationProperties config,
            final  TaskGroupService service) {
        return new ProjectService(repository, taskGroupRepository, config, service);
    }

    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository repository,
            final TaskRepository taskRepository) {
        return new TaskGroupService(repository, taskRepository);
    }
}
