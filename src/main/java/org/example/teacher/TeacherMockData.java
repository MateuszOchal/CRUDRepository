package org.example.teacher;

import lombok.RequiredArgsConstructor;
import org.example.student.Student;
import org.example.student.StudentMockData;
import org.example.student.StudentService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class TeacherMockData {
    private final TeacherRepository repository;
    private final StudentService studentService;
    private final StudentMockData studentMockData;


    private List<String> sampleNames = List.of("Bob", "Carol", "Jane", "John", "Michael",
            "Scotty", "Barbara", "Claudia", "Veronica", "Tom");
    private List<String> sampleSurnames = List.of("Doe", "Smith", "Marx", "Dunk", "Roberts",
            "Nolan", "Ross", "Jordan", "Pippin", "Clooney");

    private List<String> subjects = List.of("Engineering", "Biology", "Sociology", "Computer Science",
            "Acting", "Journalism", "Computer Sciences");

    int numberOfEntries = 40;

    @PostConstruct
    void initializeTeachers() {
        Random random = new Random();
        List<Student> students = studentService.getAllStudents();
        List<Student> studentsOfThisTeacher = new ArrayList<>();
        int size = sampleNames.size();
        int studentListSize = students.size();
        int numberOfStudentsOfGivenTeacher = random.nextInt(studentListSize);

        for (int i = 0; i < numberOfEntries; i++) {
            students = studentService.getAllStudents();
            for (int j = 0; j < numberOfStudentsOfGivenTeacher; j++) {
                Student randomStudent = students.get(random.nextInt(students.size()));
                studentsOfThisTeacher.add(randomStudent);
                students.remove(randomStudent);
            }
            String name = sampleNames.get(random.nextInt(size));
            String surname = sampleSurnames.get(random.nextInt(size));
            int age = random.nextInt(40) + 25;
            String email = (name.charAt(0) + surname + "@gmail.com").toLowerCase();
            String subject = subjects.get(random.nextInt(size - 3));
            Teacher teacher = new Teacher(name, surname, age, email, subject, studentsOfThisTeacher);
            repository.save(teacher);
            studentsOfThisTeacher.clear();
        }
        studentMockData.giveTeachersToStudents();
    }
}
