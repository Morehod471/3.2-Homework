package ru.hogwarts.school.dto;

import lombok.Getter;

@Getter
public class FacultyDTO {

    private long id;
    private String name;
    private String color;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }
}