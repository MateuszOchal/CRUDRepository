package org.example.teacher;

import lombok.RequiredArgsConstructor;
import org.example.student.Student;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class TeacherService {

    private final TeacherRepository repository;



    public List<Teacher> getAllTeachers() {
        return repository.findAll();
    }

    @Transactional
    public void removeAtId(Integer id) {
        Teacher teacher = repository.findById(id).orElseThrow();
        teacher.getStudents().forEach(s -> s.getTeachers().clear());
        repository.deleteById(id);
    }

    public Teacher addTeacher(Teacher teacher) {
        return repository.save(teacher);
    }

    public Teacher modify(int index, Teacher teacher) {
        List<Teacher> teachers = repository.findAll();
        teachers.set(index, teacher);
        repository.saveAll(teachers);
        return teachers.get(index);
    }

    public Teacher removeStudentById(int teacherId,int studentId) {
        Teacher teacher=repository.findById(teacherId).orElseThrow();
        List<Student>students = teacher.getStudents();
        List<Student>newStudentList =new ArrayList<>();
        for (Student student:students) {
            if(!student.getId().equals(studentId)){
             newStudentList.add(student);
            }
        }
        teacher.setStudents(newStudentList);
        return repository.save(teacher);
    }

    List<Teacher> findTeacherByNameAndSurname(String name, String surname){
        return repository.findTeacherByNameAndSurname(name, surname);
    }

}









