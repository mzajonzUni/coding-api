package pl.zajonz.coding.lesson;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajonz.coding.common.InvalidDateException;
import pl.zajonz.coding.lesson.model.Lesson;
import pl.zajonz.coding.lesson.model.command.CreateLessonCommand;
import pl.zajonz.coding.lesson.model.command.UpdateLessonTermCommand;
import pl.zajonz.coding.student.StudentRepository;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.teacher.TeacherRepository;
import pl.zajonz.coding.teacher.model.Teacher;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;

    public List<Lesson> findAllByDeletedFalse() {
        return lessonRepository.findAllByDeletedFalse();
    }

    public Lesson save(CreateLessonCommand command) {
        Lesson toSave = command.toEntity();
        toSave.setTeacher(findTeacherId(command.getTeacherId()));
        toSave.setStudent(findStudentId(command.getTeacherId()));
        lessonRepository.save(toSave);
        return toSave;
    }

    public Teacher findTeacherId(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Teacher with id={0} has not been found", id)));
    }

    public Student findStudentId(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Student with id={0} has not been found", id)));
    }

    public void deleteById(int id) {
        LocalDateTime date = lessonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No such lesson with Id" + id))
                .getTerm();
        if (date.isBefore(LocalDateTime.now())) {
            throw new InvalidDateException("The term is in the past");
        }
        lessonRepository.deleteById(id);
    }

    public void updateLesson(UpdateLessonTermCommand command, int lessonId) {
        Lesson editLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException("No such lesson with Id" + lessonId));
        if (!checkDate(command.getTerm(), editLesson.getTeacher().getId())) {
            throw new InvalidDateException("Invalid date " + command.getTerm());
        }
        editLesson.setTerm(command.getTerm());
        lessonRepository.save(editLesson);
    }

    public Lesson findById(int id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No such lesson with Id" + id));
    }

    public boolean checkDate(LocalDateTime date, int teacherId) {
        return !date.isBefore(LocalDateTime.now()) &&
                !lessonRepository.existsByTeacherIdAndTermBetween(
                        teacherId, date.minusMinutes(59), date.plusMinutes(59));
    }

}
