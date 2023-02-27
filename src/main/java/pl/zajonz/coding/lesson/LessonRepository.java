package pl.zajonz.coding.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.zajonz.coding.lesson.model.Lesson;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson,Integer> {

    List<Lesson> findAllByDeletedFalse();
    boolean existsByTeacherIdAndTermBetween(int teacherId, LocalDateTime from, LocalDateTime to);

}
