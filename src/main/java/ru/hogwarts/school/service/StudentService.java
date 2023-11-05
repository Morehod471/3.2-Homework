package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final Map<Long, Student> storage = new HashMap<>();
    private long counter = 0;

    public Student add(Student student) {
        student.setId(counter);
        storage.put(counter, student);
        counter++;
        return student;
    }

    public Student get(long id) {
        return storage.get(id);
    }

    public void remove(long id) {
        storage.remove(id);
    }

    public Student update(Student student) {
        if (storage.containsKey(student.getId())) {
            storage.put(student.getId(), student);
            return student;
        }
        return null;
    }

    public Collection<Student> filterByAge(int age) {
        return storage.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
}
