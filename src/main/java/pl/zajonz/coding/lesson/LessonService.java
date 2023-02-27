package pl.zajonz.coding.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajonz.coding.common.InvalidDateException;
import pl.zajonz.coding.lesson.model.Lesson;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    public List<Lesson> findAllByDeletedFalse() {
        return lessonRepository.findAllByDeletedFalse();
    }

    public void save(Lesson lesson) {
        LocalDateTime date = lesson.getTerm();
        if (!checkDate(date, lesson.getTeacher().getId())) {
            throw new InvalidDateException("Invalid date " + date);
        }
        lessonRepository.save(lesson);
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

    public void updateLesson(LocalDateTime date, int lessonId) {
        Lesson editLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException("No such lesson with Id" + lessonId));
        if (!checkDate(date, editLesson.getTeacher().getId())) {
            throw new InvalidDateException("Invalid date " + date);
        }
        editLesson.setTerm(date);
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
