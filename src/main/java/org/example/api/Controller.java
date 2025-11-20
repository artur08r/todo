package org.example.api;

import com.sun.jdi.InvalidTypeException;
import org.example.bd.Repository;
import org.example.domain.Service;
import org.example.domain.Task;
import org.example.domain.TaskStatus;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Controller {
    private final Service service;
    private final Scanner scanner;

    public Controller(Service service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void run(String command) {
        switch (command.toLowerCase().trim()) {
            case "add" -> addTask();
            case "list" -> printTasks(service.getAllTasks());
            case "get" -> getTask();
            case "edit" -> editTask();
            case "delete" -> deleteTask();
            case "filter" -> filterTasks();
            case "sort" -> sortTasks();
            default -> System.out.println("Команда не существует");
        }
    }

    // ------------------- Методы для команд -------------------

    private void addTask() {
        System.out.println("Создание новой задачи...");
        String name = inputString("Введите имя задачи: ");
        String desc = inputString("Введите описание задачи: ");
        LocalDate deadline = inputDate("Введите дедлайн задачи (yyyy-MM-dd): ");
        if (deadline == null) return;

        Task task = service.add(name, desc, deadline);
        System.out.println(task);
    }

    private void getTask() {
        Long id = inputLong("Введите id задачи: ");
        if (id == null) return;

        try {
            Task task = service.getTask(id);
            System.out.println(task);
        } catch (Exception e) {
            System.out.println("Задача с таким id не найдена");
        }
    }

    private void deleteTask() {
        Long id = inputLong("Введите id задачи для удаления: ");
        if (id == null) return;

        try {
            service.deleteTask(id);
            System.out.println("Задача удалена");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении задачи");
        }
    }

    private void filterTasks() {
        String statusStr = inputString("Введите статус для фильтрации (CREATED / IN_PROGRESS / DONE): ");
        try {
            TaskStatus status = TaskStatus.valueOf(statusStr.toUpperCase());
            printTasks(service.filterByStatus(status));
        } catch (Exception e) {
            System.out.println("Неизвестный статус");
        }
    }

    private void sortTasks() {
        System.out.println("Для сортировки по сроку введите 1, по статусу - 2");
        int option = inputInt("");
        if (option == 1) printTasks(service.sortByDeadLine());
        else if (option == 2) printTasks(service.sortByStatus());
        else System.out.println("Неизвестная команда");
    }

    private void editTask() {
        Long id = inputLong("Введите ID задачи для редактирования: ");
        if (id == null) return;

        Task task;
        try {
            task = service.getTask(id);
        } catch (Exception e) {
            System.out.println("Задача не найдена");
            return;
        }

        String name = task.name();
        String desc = task.description();
        LocalDate deadline = task.deadLine();
        TaskStatus status = task.status();

        while (true) {
            System.out.println("""
                Что хотите изменить?
                1 - название
                2 - описание
                3 - дедлайн
                4 - статус
                0 - завершить редактирование
                """);

            int option = inputInt("");
            if (option == -1) continue;

            switch (option) {
                case 0 -> {
                    Task updated = new Task(id, name, desc, deadline, status);
                    service.updateTask(updated);
                    System.out.println("Редактирование завершено: " + updated);
                    return;
                }
                case 1 -> name = inputString("Введите новое название: ");
                case 2 -> desc = inputString("Введите новое описание: ");
                case 3 -> {
                    LocalDate newDeadline = inputDate("Введите новый дедлайн (yyyy-MM-dd): ");
                    if (newDeadline != null) deadline = newDeadline;
                }
                case 4 -> {
                    String statusStr = inputString("Введите статус (CREATED / IN_PROGRESS / DONE): ");
                    try {
                        status = TaskStatus.valueOf(statusStr.toUpperCase());
                    } catch (Exception e) {
                        System.out.println("Неверный статус");
                    }
                }
                default -> System.out.println("Нет такой опции");
            }
        }
    }

    // ------------------- Вспомогательные методы -------------------

    private void printTasks(List<Task> tasks) {
        if (tasks.isEmpty()) System.out.println("Список задач пуст");
        else tasks.forEach(System.out::println);
    }

    private String inputString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private Long inputLong(String prompt) {
        System.out.print(prompt);
        try {
            Long value = scanner.nextLong();
            scanner.nextLine(); // очищаем буфер
            return value;
        } catch (Exception e) {
            System.out.println("Неверный формат числа");
            scanner.nextLine();
            return null;
        }
    }

    private int inputInt(String prompt) {
        System.out.print(prompt);
        try {
            int value = scanner.nextInt();
            scanner.nextLine();
            return value;
        } catch (Exception e) {
            System.out.println("Неверная опция");
            scanner.nextLine();
            return -1;
        }
    }

    private LocalDate inputDate(String prompt) {
        System.out.print(prompt);
        try {
            return LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат даты");
            return null;
        }
    }
}
