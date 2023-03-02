package pl.zajonz.coding.student;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajonz.coding.student.model.command.CreateStudentCommand;
import pl.zajonz.coding.student.model.command.UpdateStudentCommand;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.student.model.dto.StudentDto;
import pl.zajonz.coding.teacher.TeacherRepository;
import pl.zajonz.coding.teacher.model.Teacher;

import java.text.MessageFormat;
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

    public Student save(CreateStudentCommand command) {
        Student toSave = command.toEntity();
        toSave.setTeacher(findTeacherId(command.getTeacherId()));

        if (!toSave.getTeacher().getLanguages().contains(toSave.getLanguage())){
            throw new IllegalArgumentException("Wrong teacher");
        }

        return studentRepository.save(toSave);
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

    public Teacher findTeacherId(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Teacher with id={0} has not been found", id)));
    }

    public Student findById(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Student with id={0} has not been found", id)));
    }

    public StudentDto update(UpdateStudentCommand command, int id) {
        Student student = findById(id);
        student.setFirstName(command.getFirstName());
        student.setLastName(command.getLastName());
        studentRepository.save(student);
        return StudentDto.fromEntity(student);
    }
}
