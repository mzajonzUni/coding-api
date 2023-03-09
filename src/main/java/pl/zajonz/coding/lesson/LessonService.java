package pl.zajonz.coding.lesson;

import pl.zajonz.coding.lesson.model.Lesson;
import pl.zajonz.coding.lesson.model.command.CreateLessonCommand;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.teacher.model.Teacher;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonService {
    List<Lesson> findAllByDeletedFalse();

    Lesson save(CreateLessonCommand command);

    Teacher findTeacherId(int id);

    Student findStudentId(int id);

    void deleteById(int id);

    Lesson updateTerm(int lessonId, Lesson lesson);

    Lesson findById(int id);

    boolean checkDate(LocalDateTime date, int teacherId);
}
