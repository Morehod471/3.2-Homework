package ru.hogwarts.school.dto;

import lombok.Getter;

@Getter
public class StudentDTO {

    private long id;
    private String name;
    private int age;
    private long facultyId;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setFacultyId(long facultyId) {
        this.facultyId = facultyId;
    }
}
