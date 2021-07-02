package io.github.katkatee.todoapp.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import io.github.katkatee.todoapp.model.Task;
import io.github.katkatee.todoapp.model.TaskRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TaskController.class)
public class TaskControllerLightIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository repo;

    @Test
    void httpGet_returnsGivenTask() throws Exception {
        //given

        when(repo.findById(anyInt()))
                .thenReturn(Optional.of(new Task("foo", LocalDateTime.now())));

        //when + then
        mockMvc.perform(get("/tasks/123"))
                .andDo(print())
                .andExpect(content().string(containsString("\"description\":\"foo\"")));
    }

}
