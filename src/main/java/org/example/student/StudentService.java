package org.example.student;

import lombok.RequiredArgsConstructor;
import org.example.teacher.Teacher;
import org.example.teacher.TeacherService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service

public class StudentService {

    private final StudentRepository repository;

    private final TeacherService teacherService;

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @Transactional
    public void removeAtId(Integer id) {
        Student student = repository.findById(id).orElseThrow();
        student.getTeachers().forEach(s -> s.getStudents().clear());
        repository.deleteById(id);
    }

    public Student addStudent(Student student) {
        return repository.save(student);
    }

    public Student modify(int index, Student student) {
        List<Student> students = repository.findAll();
        students.set(index, student);
        repository.saveAll(students);
        return students.get(index);
    }

    public List<Student> findStudentByNameAndSurname(String name, String surname) {
        return repository.findStudentByNameAndSurname(name, surname);
    }

    public Student removeTeacherById(int studentId,int teacherId) {
        Student student=repository.findById(studentId).orElseThrow();
        List<Teacher>teachers = student.getTeachers();
        List<Teacher>newTeacherList =new ArrayList<>();
        for (Teacher teacher:teachers) {
            if(!teacher.getId().equals(teacherId)){
                newTeacherList.add(teacher);
            }
        }
        teacherService.removeStudentById(teacherId, studentId);
        student.setTeachers(newTeacherList);
        return repository.save(student);
    }
}
