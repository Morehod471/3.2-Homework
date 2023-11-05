package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final Map<Long, Faculty> storage = new HashMap<>();
    private long counter = 0;

    public Faculty add(Faculty faculty) {
        faculty.setId(counter);
        storage.put(counter, faculty);
        counter++;
        return faculty;
    }

    public Faculty get(long id) {
        return storage.get(id);
    }

    public void remove(long id) {
        storage.remove(id);
    }

    public Faculty update(Faculty faculty) {
        if (storage.containsKey(faculty.getId())) {
            storage.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }

    public Collection<Faculty> filterByColor(String color) {
        return storage.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
