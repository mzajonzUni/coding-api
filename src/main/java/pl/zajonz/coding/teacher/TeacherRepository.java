package pl.zajonz.coding.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.teacher.model.Teacher;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    List<Teacher> findAllByDeletedFalse();
    List<Teacher> findAllByLanguagesContainingAndDeletedFalse(Language language);
}
