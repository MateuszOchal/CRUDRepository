package org.example.student;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.teacher.Teacher;
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
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Integer id;
    @NotNull(message = "Name cannot be null")
    @Length(min = 2, max = 20, message = "name must be between 2 and 20 chars")
    private String name;
    @NotNull(message = "Surname cannot be null")
    @Length(min = 2, max = 20, message = "surname must be between 2 and 20 chars")
    private String surname;
    @Min(18)
    @Max(100)
    private int age;
    @Email
    private String email;
    private String fieldOfStudy;
    @JsonIgnore
    @ManyToMany(mappedBy = "students")
    private List<Teacher> teachers;

    public Student(String name, String surname, int age, String email, String fieldOfStudy) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.fieldOfStudy = fieldOfStudy;
    }

    public Student(String name, String surname, int age, String email, String fieldOfStudy, List<Teacher> teachers) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.fieldOfStudy = fieldOfStudy;
        this.teachers = teachers;
    }
}