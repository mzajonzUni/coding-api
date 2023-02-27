package pl.zajonz.coding.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.teacher.TeacherRepository;
import pl.zajonz.coding.teacher.model.Teacher;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public List<Student> findAllByDeletedFalse() {
        return studentRepository.findAllByDeletedFalse();
    }

    public void save(Student student) {
        if (!student.getTeacher().getLanguages().contains(student.getLanguage())){
            throw new IllegalArgumentException("Wrong teacher");
        }
        studentRepository.save(student);
    }

    public void deleteById(int id) {
        studentRepository.deleteById(id);
    }

    public List<Student> findAllByTeacher_IdAndDeletedFalse(int id) {
        return studentRepository.findAllByTeacher_IdAndDeletedFalse(id);
    }

    public Student findById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No such student with Id " + id));
    }

    public void updateStudent(Integer teacherId, Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("No such student with Id " + studentId));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new NoSuchElementException("No such teacher with Id " + teacherId));
        if (!teacher.getLanguages().contains(student.getLanguage())){
            throw new IllegalArgumentException("Wrong teacher");
        }
        student.setTeacher(teacher);
        studentRepository.save(student);
    }
}
