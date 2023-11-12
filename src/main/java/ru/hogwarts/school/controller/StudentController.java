package ru.hogwarts.school.controller;

import lombok.Getter;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService service;
    @Getter
    private final FacultyService facultyService;

    public StudentController(StudentService service, FacultyService facultyService) {
        this.service = service;
        this.facultyService = facultyService;
    }

    @PostMapping
    public Student add(@RequestBody Student student) {
        return service.add(student);
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable long id) {
        return service.get(id);
    }

    @PutMapping
    public Student update(@RequestBody Student student) {
        return service.update(student);
    }

    @DeleteMapping("/{id}")
    public Student remove(@PathVariable long id) {
        return service.remove(id);
    }

    @GetMapping("/findByAge")
    public Collection<Student> findByAge(@RequestParam int age) {
        return service.findByAge(age);
    }

    @GetMapping("/findByAgeBetween")
    public Collection<Student> findByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return service.findByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{studentId}/faculty")
    public Faculty facultyByStudent(@PathVariable long studentId) {
        return service.get(studentId).getFaculty();
    }

    @GetMapping("/countAll")
    public Integer countAllStudents() {
        return service.countAllStudents();
    }

    @GetMapping("/getAverageAge")
    public Integer getAverageAge() {
        return service.getAverageAge();
    }

    @GetMapping("/getLastStudents")
    public List<Student> getLastStudents() {
        return service.getLastStudents();
    }


}
