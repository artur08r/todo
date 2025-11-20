package org.example.domain;

import org.example.bd.Repository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class Service {
    private final Repository repository;

    public Service(Repository repository) {
        this.repository = repository;
    }

    public Task add(String name, String description, LocalDate deadLine){
        return repository.createTask(name, description, deadLine);
    }

    public Task getTask(Long id){
        return repository.getTaskById(id);
    }

    public List<Task> getAllTasks(){
        return repository.getAllTasks();
    }

    public void updateTask(Task taskToUpdate){
        repository.updateTask(taskToUpdate);
    }

    public void deleteTask(Long id){
        repository.deleteTask(id);
    }

    public List<Task> filterByStatus(TaskStatus status){
        return repository.filterByStatus(status);
    }

    public List<Task> sortByStatus(){
        return repository.sortByStatus();
    }

    public List<Task> sortByDeadLine(){
        return repository.sortByDeadLine();
    }
}
