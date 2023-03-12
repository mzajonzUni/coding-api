package pl.zajonz.coding.lesson;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajonz.coding.common.exception.InvalidDateException;
import pl.zajonz.coding.lesson.model.Lesson;
import pl.zajonz.coding.lesson.model.command.CreateLessonCommand;
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
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;

    @Override
    public List<Lesson> findAllByDeletedFalse() {
        return lessonRepository.findAllByDeletedFalse();
    }

    @Override
    public Lesson save(CreateLessonCommand command) {
        Lesson toSave = command.toEntity();
        if (!checkDate(toSave.getTerm(), command.getTeacherId())) {
            throw new InvalidDateException("Invalid date " + toSave.getTerm());
        }
        toSave.setTeacher(findTeacherId(command.getTeacherId()));
        toSave.setStudent(findStudentId(command.getStudentId()));
        return lessonRepository.save(toSave);
    }

    @Override
    public Teacher findTeacherId(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Teacher with id={0} has not been found", id)));
    }

    @Override
    public Student findStudentId(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Student with id={0} has not been found", id)));
    }

    @Override
    public void deleteById(int id) {
        LocalDateTime date = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Lesson with id={0} has not been found", id)))
                .getTerm();
        if (date.isBefore(LocalDateTime.now())) {
            throw new InvalidDateException("The term is in the past");
        }
        lessonRepository.deleteById(id);
    }

    @Override
    public Lesson updateTerm(int lessonId, Lesson lesson) {
        Lesson editLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Lesson with id={0} has not been found", lessonId)));
        if (!checkDate(lesson.getTerm(), editLesson.getTeacher().getId())) {
            throw new InvalidDateException("Invalid date " + lesson.getTerm());
        }
        editLesson.setTerm(lesson.getTerm());
        return lessonRepository.save(editLesson);
    }

    @Override
    public Lesson findById(int id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Lesson with id={0} has not been found", id)));
    }

    @Override
    public boolean checkDate(LocalDateTime date, int teacherId) {
        return !date.isBefore(LocalDateTime.now()) &&
                !lessonRepository.existsByTeacherIdAndTermBetween(
                        teacherId, date.minusMinutes(59), date.plusMinutes(59));
    }
}
