package org.example.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.student.Student;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Integer id;
    @NotNull(message = "Name cannot be null")
    @Length(min = 2, max = 20, message = "name must be between 2 and 20 chars")
    private String name;
    @NotNull(message = "Surname cannot be null")
    @Length(min = 2, max = 20, message = "name must be between 2 and 20 chars")
    private String surname;
    @Min(25)
    @Max(100)
    private int age;
    @Email
    private String email;
    private String subject;
    @ManyToMany
    @JoinTable(
            name = "teacher_student",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students;

    public Teacher(String name, String surname, int age, String email, String subject) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.subject = subject;
    }

    public Teacher(String name, String surname, int age, String email, String subject, List<Student> students) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.subject = subject;
        this.students = students;
    }
}
