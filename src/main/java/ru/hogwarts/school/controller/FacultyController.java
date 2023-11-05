package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @PostMapping
    public Faculty add(@RequestBody Faculty faculty) {
        return service.add(faculty);
    }

    @GetMapping("/{id}")
    public Faculty get(@PathVariable long id) {
        return service.get(id);
    }

    @PutMapping
    public Faculty update(@RequestBody Faculty faculty) {
        return service.update(faculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Collection<Faculty>> remove(@PathVariable long id) {
        service.remove(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findByColor")
    public Collection<Faculty> findByColor(@RequestParam String color) {
        return service.findByColor(color);
    }

    @GetMapping("/findByColorOrName")
    public ResponseEntity<Collection<Faculty>> findByColorOrName(@RequestParam (required = false) String color,
                                            @RequestParam(required = false) String name) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(service.findByColor(color));
        }
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(service.findByName(name));
        }
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{facultyId}/students")
    public Collection<Student> findByFaculty(@PathVariable long facultyId) {
        return service.get(facultyId).getStudents();
    }
}
