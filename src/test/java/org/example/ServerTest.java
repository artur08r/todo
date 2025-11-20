package org.example;

import org.example.bd.Repository;
import org.example.domain.Service;
import org.example.domain.Task;
import org.example.domain.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServerTest {

    @Mock
    Repository repository;

    @InjectMocks
    Service service;

    @Test
    void addTest(){
        String name = "Задача 1";
        String description = "Описание 1";
        LocalDate deadline = LocalDate.parse("2020-12-12");
        Task task = new Task(1L, name,description, deadline, TaskStatus.CREATED);

        when(repository.createTask(anyString(),anyString(),any(LocalDate.class))).thenReturn(task);

        Task result = service.add(name,description, deadline);

        assertEquals(name, result.name());
        verify(repository, times(1)).createTask(anyString(),anyString(), any(LocalDate.class));

    }

    @Test
    void editTest(){
        String name = "Задача 1";
        String description = "Описание 1";
        LocalDate deadline = LocalDate.parse("2020-12-12");
        Task task = new Task(1L, name,description, deadline, TaskStatus.CREATED);

        service.updateTask(task);

        verify(repository, times(1)).updateTask(eq(task));
    }

    @Test
    void deleteTest(){
        service.deleteTask(2L);
        verify(repository, times(1)).deleteTask(anyLong());

    }
    @Test
    void filterTest(){
        Task t1 = new Task(1L, "A", "desc", LocalDate.now(), TaskStatus.CREATED);
        Task t2 = new Task(2L, "B", "desc", LocalDate.now(), TaskStatus.DONE);
        Task t3 = new Task(3L, "C", "desc", LocalDate.now(), TaskStatus.CREATED);

        List<Task> mockedTasks = List.of(t1, t3);

        when(repository.filterByStatus(TaskStatus.CREATED)).thenReturn(mockedTasks);

        // when
        List<Task> result = service.filterByStatus(TaskStatus.CREATED);

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(t1));
        assertTrue(result.contains(t3));
        assertFalse(result.contains(t2));

        verify(repository, times(1)).filterByStatus(TaskStatus.CREATED);
    }

    @Test
    void sortByDeadLineTest() {
        // Подготовка тестовых задач
        Task t1 = new Task(1L, "A", "desc", LocalDate.of(2025, 12, 10), TaskStatus.CREATED);
        Task t2 = new Task(2L, "B", "desc", LocalDate.of(2025, 11, 5), TaskStatus.DONE);
        Task t3 = new Task(3L, "C", "desc", LocalDate.of(2025, 12, 1), TaskStatus.IN_PROGRESS);

        List<Task> sortedByDeadline = List.of(t2, t3, t1);

        // Мокаем вызов репозитория
        when(repository.sortByDeadLine()).thenReturn(sortedByDeadline);

        // Вызов сервиса
        List<Task> result = service.sortByDeadLine();

        // Проверка
        assertEquals(sortedByDeadline, result);
        verify(repository, times(1)).sortByDeadLine();
    }

    @Test
    void sortByStatusTest() {
        // Подготовка тестовых задач
        Task t1 = new Task(1L, "A", "desc", LocalDate.of(2025, 12, 10), TaskStatus.CREATED);
        Task t2 = new Task(2L, "B", "desc", LocalDate.of(2025, 11, 5), TaskStatus.DONE);
        Task t3 = new Task(3L, "C", "desc", LocalDate.of(2025, 12, 1), TaskStatus.IN_PROGRESS);

        List<Task> sortedByStatus = List.of(t1, t3, t2); // порядок CREATED, IN_PROGRESS, DONE

        // Мокаем вызов репозитория
        when(repository.sortByStatus()).thenReturn(sortedByStatus);

        // Вызов сервиса
        List<Task> result = service.sortByStatus();

        // Проверка
        assertEquals(sortedByStatus, result);
        verify(repository, times(1)).sortByStatus();
    }
}
