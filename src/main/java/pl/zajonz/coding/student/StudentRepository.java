package pl.zajonz.coding.student;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.zajonz.coding.student.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Integer> {
    List<Student> findAllByTeacher_IdAndDeletedFalse(int id);

    List<Student> findAllByDeletedFalse();
}
