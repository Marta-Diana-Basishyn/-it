package com.softserve.itacademy.service;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.repository.ToDoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService {
    private final ToDoRepository todoRepository;

    public ToDoService(ToDoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public ToDo create(ToDo todo) {
        if (todo == null){
            throw new RuntimeException("ToDo cannot be null");
        }
            return todoRepository.save(todo);
    }

    public ToDo readById(long id) {
        return todoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ToDo with id " + id + " not found"));
    }

    public ToDo update(ToDo todo) {
        if (todo == null){
            throw new RuntimeException("ToDo cannot be null");
        }
            readById(todo.getId());
            return todoRepository.save(todo);
    }

    public void delete(long id) {
        ToDo todo = readById(id);
        todoRepository.delete(todo);
    }

    public List<ToDo> getAll() {
        return todoRepository.findAll();
    }

    public List<ToDo> getByUserId(long userId) {
        return todoRepository.getByUserId(userId);
    }
}
