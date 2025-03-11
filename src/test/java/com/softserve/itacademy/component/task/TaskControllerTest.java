package com.softserve.itacademy.component.task;


import com.softserve.itacademy.controller.TaskController;
import com.softserve.itacademy.dto.TaskDto;
import com.softserve.itacademy.dto.TaskTransformer;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.TaskPriority;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.service.StateService;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {TaskController.class})
@ContextConfiguration(classes = {TaskController.class, TaskTransformer.class})
class TaskControllerTest {

    @MockBean
    private TaskService taskService;
    @MockBean
    private ToDoService todoService;
    @MockBean
    private StateService stateService;

    private final TaskTransformer taskTransformer = new TaskTransformer();

    @Autowired
    private MockMvc mvc;

    @Test
    void TestReturnTaskCreationForm() throws Exception {
        // Мокаємо отримання ToDo
        when(todoService.readById(1L)).thenReturn(new ToDo());

        // Виконуємо GET-запит
        mvc.perform(get("/tasks/create/todos/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("task", "todo", "priorities"));
    }
    @Test
    void TestCreateTaskSuccessfully() throws Exception {
        // Створюємо TaskDto для передачі у запит
        TaskDto taskDto = new TaskDto(0L, "New Task", TaskPriority.HIGH.name(), 1L, 1L);

        // Мокаємо виклик сервісу
        when(taskService.create(any(TaskDto.class))).thenReturn(taskDto);

        // Виконуємо POST-запит
        mvc.perform(post("/tasks/create/todos/1")
                        .flashAttr("task", taskDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/1/tasks"));
    }
    @Test
    void TestUpdateTaskSuccessfully() throws Exception {
        // Створюємо TaskDto для оновлення
        TaskDto taskDto = new TaskDto(1L, "Updated Task", TaskPriority.MEDIUM.name(), 1L, 2L);

        // Мокаємо виклик сервісу
        when(taskService.update(any(TaskDto.class))).thenReturn(taskDto);

        // Виконуємо POST-запит
        mvc.perform(post("/tasks/1/update/todos/1")
                        .flashAttr("task", taskDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/1/tasks"));
    }
    @Test
    void TestDeleteTask() throws Exception {
        // Мокаємо видалення завдання
        doNothing().when(taskService).delete(1L);

        // Виконуємо GET-запит
        mvc.perform(get("/tasks/1/delete/todos/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/todos/1/tasks"));
    }


}
