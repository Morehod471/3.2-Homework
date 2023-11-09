package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception  {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void testPostFaculty() {
        Faculty f = faculty("Gryffindor", "red");
        Faculty testFaculty = restTemplate.postForObject("http://localhost:" + port + "/faculty", f, Faculty.class);
        Assertions.assertThat(testFaculty.getColor()).isEqualTo("red");
        Assertions.assertThat(testFaculty.getName()).isEqualTo("Gryffindor");
    }

    @Test
    public void testGetFaculty() {
        Faculty f = faculty("Gryffindor", "red");
        Faculty saved = restTemplate.postForObject("/faculty", f, Faculty.class);
        Faculty testFaculty = restTemplate.getForObject("/faculty/" + saved.getId(), Faculty.class);
        Assertions.assertThat(testFaculty.getName()).isEqualTo("Gryffindor");
        Assertions.assertThat(testFaculty.getColor()).isEqualTo("red");
    }

    @Test
    public void testPutFaculty() {
        Faculty f = faculty("Gryffindor", "red");
        Faculty saved = restTemplate.postForObject("/faculty", f, Faculty.class);

        saved.setName("Malory");
        ResponseEntity<Faculty> facultyEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.PUT,
                new HttpEntity<>(saved),
                Faculty.class);

        Assertions.assertThat(facultyEntity.getBody().getName()).isEqualTo("Malory");
        Assertions.assertThat(facultyEntity.getBody().getColor()).isEqualTo("red");
    }

    @Test
    public void testDeleteFaculty() {
        Faculty f = faculty("Gryffindor", "red");
        Faculty saved = restTemplate.postForObject("/faculty", f, Faculty.class);

        ResponseEntity<Faculty> facultyEntity = restTemplate.exchange(
                "/faculty/" + saved.getId(),
                HttpMethod.DELETE,
                null,
                Faculty.class);

        Assertions.assertThat(facultyEntity.getBody().getName()).isEqualTo("Gryffindor");
        Assertions.assertThat(facultyEntity.getBody().getColor()).isEqualTo("red");
    }

    @Test
    void testFindByColor() {
        Faculty f1 = restTemplate.postForObject("/faculty", faculty("test1", "red"), Faculty.class);
        Faculty f2 = restTemplate.postForObject("/faculty", faculty("test2", "blue"), Faculty.class);
        Faculty f3 = restTemplate.postForObject("/faculty", faculty("test3", "yellow"), Faculty.class);
        Faculty f4 = restTemplate.postForObject("/faculty", faculty("test4", "green"), Faculty.class);

        var result = restTemplate.exchange("/faculty/findByColor?color=green",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {});

        Collection<Faculty> faculties = result.getBody();

        Assertions.assertThat(faculties).isNotNull();
        Assertions.assertThat(faculties.size()).isEqualTo(1);
        Assertions.assertThat(faculties).containsExactly(f4);
    }

    private static Faculty faculty(String name, String color) {
        var f = new Faculty();
        f.setName(name);
        f.setColor(color);
        return f;
    }
}

