package pl.zajonz.coding.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.teacher.TeacherRepository;
import pl.zajonz.coding.teacher.model.Teacher;

import java.util.List;
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

}
