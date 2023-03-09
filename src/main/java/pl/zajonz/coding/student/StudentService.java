package pl.zajonz.coding.student;

import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.student.model.command.CreateStudentCommand;
import pl.zajonz.coding.student.model.command.UpdateStudentCommand;
import pl.zajonz.coding.student.model.dto.StudentDto;
import pl.zajonz.coding.teacher.model.Teacher;

import java.util.List;

public interface StudentService {
    List<Student> findAllByDeletedFalse();

    Student save(CreateStudentCommand command);

    void deleteById(int id);

    List<Student> findAllByTeacher_IdAndDeletedFalse(int id);

    void updateStudent(Integer teacherId, Integer studentId);

    Teacher findTeacherId(int id);

    Student findById(int id);

    StudentDto update(UpdateStudentCommand command, int id);
}
