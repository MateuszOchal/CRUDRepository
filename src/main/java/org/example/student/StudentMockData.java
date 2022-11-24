package org.example.student;

import lombok.RequiredArgsConstructor;
import org.example.teacher.Teacher;
import org.example.teacher.TeacherService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class StudentMockData {

    private final StudentRepository repository;
    private final StudentService service;
    private final TeacherService teacherService;

    private final List<String> sampleNames = List.of("Bob", "Carol", "Jane", "John", "Michael",
            "Scotty", "Barbara", "Claudia", "Veronica", "Tom");
    private final List<String> sampleSurnames = List.of("Doe", "Smith", "Marx", "Dunk", "Roberts",
            "Nolan", "Ross", "Jordan", "Pippin", "Clooney");
    private final List<String> fieldOfStudy = List.of("Engineering", "Biology", "Sociology", "Computer Science",
            "Acting", "Journalism", "Computer Sciences");
    Random random = new Random();

    @PostConstruct
    public void initializeMockData() {
        int size = sampleNames.size();
        int numberOfEntries = 100;
        for (int i = 0; i < numberOfEntries; i++) {
            String name = sampleNames.get(random.nextInt(size));
            String surname = sampleSurnames.get(random.nextInt(size));
            int age = random.nextInt(5) + 18;
            String email = (name.charAt(0) + surname + "@gmail.com").toLowerCase();
            String subject = fieldOfStudy.get(random.nextInt(size - 3));
            Student student = new Student(name, surname, age, email, subject);
            repository.save(student);
        }
    }
    public void giveTeachersToStudents() {
        List<Student> students = service.getAllStudents();
        for (Student student : students) {
            List<Teacher> teachers = teacherService.getAllTeachers();
            List<Teacher> teachersOfSpecificStudent = new ArrayList<>();
            for (int i = 0; i < fieldOfStudy.size(); i++) {
                Teacher teacher = teachers.get(random.nextInt(teachers.size()));
                String subjectThatIsAlreadyAssigned = teacher.getSubject();
                if (teacher.getSubject().contains(subjectThatIsAlreadyAssigned)) {
                    teachers.remove(teacher);
                }
            }
            student.setTeachers(teachersOfSpecificStudent);
            teachersOfSpecificStudent.clear();
        }
    }
}



