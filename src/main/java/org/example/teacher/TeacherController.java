package org.example.teacher;

import lombok.RequiredArgsConstructor;
import org.example.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherRepository repository;
    private final TeacherService teacherService;

    @GetMapping
    List<Teacher> getAllTeachers(@RequestParam Optional<String> name,
                                 @RequestParam Optional<String> surname,
                                 @RequestParam Optional<Integer> page,
                                 @RequestParam Optional<String> sortBy) {
        int pageSize = 5;
        Page<Teacher> teacherPage =
                repository.findByNameAndSurname(name.orElse(""),
                        surname.orElse(""),
                        PageRequest.of(
                                page.orElse(0), pageSize,
                                Sort.Direction.ASC, sortBy.orElse("id")));
        return teacherPage.getContent().stream()
                .filter(t -> t.getStudents().removeAll(t.getStudents()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{name}/{surname}/{number}")
    List<Student> studentsOfGivenTeacher(@PathVariable(name = "name") String name, @PathVariable(name = "surname") String surname, @PathVariable(name = "number") int number) {
        List<Teacher> teachers = teacherService.findTeacherByNameAndSurname(name, surname);
        return teachers.get(number).getStudents();
    }


    @PostMapping("/newteacher")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Teacher> createTeacher(@Validated @RequestBody Teacher teacher) {
        Teacher savedTeacher = teacherService.addTeacher(teacher);
        return new ResponseEntity<>(savedTeacher, HttpStatus.CREATED);
    }

    @PutMapping("/updateteacher/{index}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Teacher> modifyTeacher(@PathVariable int index, @Validated @RequestBody Teacher teacher) {
        Teacher modifiedTeacher = teacherService.modify(index, teacher);
        return new ResponseEntity<>(modifiedTeacher, HttpStatus.CREATED);
    }

    @PutMapping("/newstudent/{teacherid}/{studentid}")
    public ResponseEntity<Teacher> giveTeacherToStudent(@PathVariable(name = "teacherid") Integer teacherid,
                                                        @PathVariable(name = "studentid") Integer studentid) {
        List<Teacher> teachers = repository.findAll();
        List<Student> students = new ArrayList<>();
        for (Teacher eachTeacher : teachers) {
            students.addAll(eachTeacher.getStudents());
        }
        Set<Student> studentSet = new HashSet<>();
        studentSet.addAll(students);
        students.clear();
        students.addAll(studentSet);
        Teacher teacherBeingChanged = repository.getById(teacherid);
        for (Student student : students) {
            if (student.getId().equals(studentid)) {
                teacherBeingChanged.getStudents().add(student);
                repository.save(teacherBeingChanged);
            }
        }
        return new ResponseEntity<>(teacherBeingChanged, HttpStatus.CREATED);
    }

    @PutMapping("/remove/student/{teacherid}/{studentid}")
    public ResponseEntity<Teacher> removeStudentFromTeacher(@PathVariable(name = "teacherid") int teacherId,
                                                            @PathVariable(name = "studentid") int studentId) {
        Teacher t = teacherService.removeStudentById(teacherId, studentId);
        return new ResponseEntity<>(t, HttpStatus.NO_CONTENT);
    }

    @Transactional
    @DeleteMapping("/deleteteacher/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteAtIndex(@PathVariable Integer id) {
        teacherService.removeAtId(id);
    }
}


