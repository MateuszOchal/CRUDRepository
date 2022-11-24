package org.example.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.teacher.Teacher;
import org.example.teacher.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {
    private final StudentRepository repository;
    private final StudentService service;
    private final TeacherService teacherService;

    @GetMapping
    List<Student> getAllStudents(@RequestParam Optional<String> name,
                                 @RequestParam Optional<String> surname,
                                 @RequestParam Optional<Integer> page,
                                 @RequestParam Optional<String> sortBy) {
        int pageSize = 5;
        Page<Student> studentPage = repository.findByNameAndSurname(name.orElse("_"),
                surname.orElse("_"),
                PageRequest.of(
                        page.orElse(0), pageSize,
                        Sort.Direction.ASC, sortBy.orElse("id")));
        return studentPage.getContent();
    }

    @GetMapping("/{name}/{surname}/{number}")
    List<Teacher> teachersOfGivenStudent(@PathVariable(name = "name") String name,
                                         @PathVariable(name = "surname") String surname,
                                         @PathVariable(name = "number") int number) {
        List<Student> students = service.findStudentByNameAndSurname(name, surname);
        return students.get(number).getTeachers();
    }

    @GetMapping("/{id}")
    List<Teacher> teachersOfGivenStudent(@PathVariable(name = "id") Integer id) {
        List<Student> students = repository.findAll();
        Student student = students.get(id);
        return student.getTeachers();
    }

    @PostMapping("/newstudent")
    public ResponseEntity<Student> createStudent(@Validated @RequestBody Student student){
        Student savedStudent = service.addStudent(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    @PutMapping("/newteacher/{studentid}/{teacherid}")
    public ResponseEntity<Student> giveTeacherToStudent(@PathVariable (name = "studentid") Integer studentid,
                                                        @PathVariable (name = "teacherid") Integer teacherid){
        List<Student>students = repository.findAll();
        List<Teacher>teachers = new ArrayList<>();
        for (Student eachStudent: students){
            teachers.addAll(eachStudent.getTeachers());
            }
        Set<Teacher> teacherSet = new HashSet<>();
        teacherSet.addAll(teachers);
        teachers.clear();
        teachers.addAll(teacherSet);
        Student studentBeingChanged = repository.getById(studentid);
        for(Teacher teacher : teachers){
            if(teacher.getId().equals(teacherid)){
                studentBeingChanged.getTeachers().add(teacher);
                repository.save(studentBeingChanged);
            }
        }
        return new ResponseEntity<>(studentBeingChanged, HttpStatus.CREATED);
    }

    @PutMapping("/updatestudent/{index}")
    public ResponseEntity<Student> update(@PathVariable int index, @Validated @RequestBody Student student) {
        Student modifiedStudent = service.modify(index, student);
        return new ResponseEntity<>(modifiedStudent, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/remove/teacher/{studentid}/{teacherid}")
    public ResponseEntity<Student> removeTeacherFromStudent(@PathVariable (name="studentid") int studentId,
                                                            @PathVariable(name="teacherid") int teacherId)
                                                            {

        Student s = service.removeTeacherById(studentId, teacherId);
        return new ResponseEntity<>(s, HttpStatus.NO_CONTENT);}

    @Transactional
    @DeleteMapping("/deletestudent/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteAtIndex(@PathVariable Integer id) {
        service.removeAtId(id);
    }
}