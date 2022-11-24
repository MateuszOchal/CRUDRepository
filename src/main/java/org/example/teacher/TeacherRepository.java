package org.example.teacher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query("select t from Teacher t where t.name like %:name% and t.surname like %:surname%")
    Page<Teacher> findByNameAndSurname(String name, String surname, Pageable pageable);
    List<Teacher> findTeacherByNameAndSurname(String name, String surname);

}
