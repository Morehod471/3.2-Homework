package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception  {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testPostStudent() {
        Student s = student("James", 16);
        Student testStudent = restTemplate.postForObject("http://localhost:" + port + "/student", s, Student.class);
        Assertions.assertThat(testStudent.getAge()).isEqualTo(16);
        Assertions.assertThat(testStudent.getName()).isEqualTo("James");
        Assertions.assertThat(testStudent.getId()).isEqualTo(1L);
    }

    @Test
    public void testGetStudent() {
        Student s = student("Harry", 15);
        Student saved = restTemplate.postForObject("/student", s, Student.class);
        Student testStudent = restTemplate.getForObject("/student/" + saved.getId(), Student.class);
        Assertions.assertThat(testStudent.getName()).isEqualTo("Harry");
        Assertions.assertThat(testStudent.getAge()).isEqualTo(15);
    }

    @Test
    public void testPutStudent() {
        Student s = student("Venik", 20);
        Student saved = restTemplate.postForObject("/student", s, Student.class);

        saved.setName("Malory");
        ResponseEntity<Student> studentEntity = restTemplate.exchange(
                "http://localhost:" + port + "/student",
                HttpMethod.PUT,
                new HttpEntity<>(saved),
                Student.class);

        Assertions.assertThat(studentEntity.getBody().getName()).isEqualTo("Malory");
        Assertions.assertThat(studentEntity.getBody().getAge()).isEqualTo(20);
    }

    @Test
    public void testDeleteStudent() {
        Student s = student("Venik", 20);
        Student saved = restTemplate.postForObject("/student", s, Student.class);

        ResponseEntity<Student> studentEntity = restTemplate.exchange(
                "/student/" + saved.getId(),
                HttpMethod.DELETE,
                null,
                Student.class);

        Assertions.assertThat(studentEntity.getBody().getName()).isEqualTo("Venik");
        Assertions.assertThat(studentEntity.getBody().getAge()).isEqualTo(20);
    }

    @Test
    void testFindByAge() {
        Student s1 = restTemplate.postForObject("/student", student("test1", 16), Student.class);
        Student s2 = restTemplate.postForObject("/student", student("test2", 17), Student.class);
        Student s3 = restTemplate.postForObject("/student", student("test3", 18), Student.class);
        Student s4 = restTemplate.postForObject("/student", student("test4", 17), Student.class);

        var result = restTemplate.exchange("/student/findByAge?age=17",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {});

        Collection<Student> students = result.getBody();

        Assertions.assertThat(students).isNotNull();
        Assertions.assertThat(students.size()).isEqualTo(2);
        Assertions.assertThat(students).containsExactly(s2, s4);
    }

    @Test
    void testFacultyByStudent() {
        var savedFaculty = restTemplate.postForObject("/faculty", faculty("Griffindor", "red"), Faculty.class);
        var s = student("Ron", 19);
        s.setFaculty(savedFaculty);
        var savedStudent = restTemplate.postForObject("/student", s, Student.class);


        var result = restTemplate.getForObject("/student/" + savedStudent.getId() + "/faculty", Faculty.class);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getName()).isEqualTo("Griffindor");
        Assertions.assertThat(result.getColor()).isEqualTo("red");
    }

    @Test
    void testFindByAgeBetween() {
        Student s1 = restTemplate.postForObject("/student", student("test1", 16), Student.class);
        Student s2 = restTemplate.postForObject("/student", student("test2", 17), Student.class);
        Student s3 = restTemplate.postForObject("/student", student("test3", 18), Student.class);
        Student s4 = restTemplate.postForObject("/student", student("test4", 17), Student.class);

        var result = restTemplate.exchange("/student/findByAgeBetween?minAge=16&maxAge=17",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {});

        Collection<Student> students = result.getBody();

        Assertions.assertThat(students).isNotNull();
        Assertions.assertThat(students.size()).isEqualTo(3);
        Assertions.assertThat(students).containsExactly(s1, s2, s4);
    }

    private static Student student(String name, int age) {
        Student s = new Student();
        s.setName(name);
        s.setAge(age);
        return s;
    }

    private static Faculty faculty(String name, String color) {
        var f = new Faculty();
        f.setName(name);
        f.setColor(color);
        return f;
    }
}
