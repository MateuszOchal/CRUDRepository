package org.example.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("select s from Student s where s.name like %:name% and s.surname like %:surname%")
    Page<Student> findByNameAndSurname( String name, String surname,  Pageable pageable);
    List<Student> findStudentByNameAndSurname(String name, String surname);
}
