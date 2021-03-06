package io.github.katkatee.todoapp.controller;

import io.github.katkatee.todoapp.TaskConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class InfoController {
    private DataSourceProperties dataSource;
    private TaskConfigurationProperties prop;

    InfoController(DataSourceProperties dataSource, TaskConfigurationProperties prop) {
        this.dataSource = dataSource;
        this.prop = prop;
    }

    @GetMapping("/info/url")
    String url() {
        return dataSource.getUrl();
    }

    @GetMapping("/info/prop")
    Boolean myProp() {
        return prop.getTemplate().isAllowMultipleTasks();
    }
}
