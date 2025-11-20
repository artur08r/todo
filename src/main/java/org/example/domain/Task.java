package org.example.domain;

import java.time.LocalDate;

public record Task(
    Long id,
    String name,
    String description,
    LocalDate deadLine,
    TaskStatus status
) {
    @Override
    public String toString() {
        return String.format("ID %d | Название : %s | Описание : %s | Дедлайн : %s | Статус : %S \n",
                id, name, description, deadLine,status);
    }

}
