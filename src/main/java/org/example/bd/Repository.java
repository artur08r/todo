package org.example.bd;

import org.example.domain.Task;
import org.example.domain.TaskStatus;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class Repository {
    private final Map<Long, Task> repository = new HashMap<>();
    private Long id = 1L;

    public Task createTask(String name, String description, LocalDate deadLine){
        Task createdTask = new Task(id, name, description, deadLine, TaskStatus.CREATED);
        repository.put(id, createdTask);
        id++;
        return createdTask;
    }

    public void deleteTask(Long id){
        repository.remove(id);
    }

    public void updateTask(Task task){
        if (!repository.containsKey(task.id()))
            throw new NoSuchElementException("Task does not exist with id " + task.id());
        repository.put(task.id(), task);
    }

    public List<Task> getAllTasks(){
        return new ArrayList<>(repository.values());
    }

    public Task getTaskById(Long id){
        Task task = repository.get(id);
        if (task == null) {
            throw new NoSuchElementException("Task does not exist with id " + id);
        }
        return task;

    }

    public List<Task> filterByStatus(TaskStatus status){
        return repository.values().stream()
                .filter(task -> task.status() == status)
                .toList();
    }

    public List<Task> sortByStatus(){
        return repository.values().stream()
                .sorted(Comparator.comparing(Task::status))
                .toList();
    }

    public List<Task> sortByDeadLine(){
        return repository.values().stream()
                .sorted(Comparator.comparing(Task::deadLine))
                .toList();
    }


}
