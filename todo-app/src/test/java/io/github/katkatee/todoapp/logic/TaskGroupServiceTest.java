package io.github.katkatee.todoapp.logic;

import io.github.katkatee.todoapp.model.TaskGroup;
import io.github.katkatee.todoapp.model.TaskGroupRepository;
import io.github.katkatee.todoapp.model.TaskRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    void toggleGroup_undoneTasks_trowsIllegalStateException() {
        //given
        TaskRepository mockTaskRepository = mockTaskRepository(true);

        var toTest = new TaskGroupService(null, mockTaskRepository);
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("has undone tasks");
    }

    @Test
    void toggleGroup_idNotFound_throwsIllegalArgumentException() {
        TaskRepository mockTaskRepository = mockTaskRepository(false);
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());

        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    void toggleGroup_worksAsExpected() {
        TaskRepository mockTaskRepository = mockTaskRepository(false);
        var group = new TaskGroup();
        var beforeToggle = group.isDone();

        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));

        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
        toTest.toggleGroup(0);

        assertThat(group.isDone()).isNotEqualTo(beforeToggle);


    }

    private TaskRepository mockTaskRepository(boolean result) {
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }


}