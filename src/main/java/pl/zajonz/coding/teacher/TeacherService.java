package pl.zajonz.coding.teacher;

import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.teacher.model.Teacher;
import pl.zajonz.coding.teacher.model.command.UpdateTeacherCommand;
import pl.zajonz.coding.teacher.model.command.UpdateTeacherLanguageCommand;
import pl.zajonz.coding.teacher.model.dto.TeacherDto;

import java.util.List;

public interface TeacherService {

    /**
     * Method returns list of {@link Teacher} elements that have deleted status false
     *
     * @return list of active teachers
     */
    List<Teacher> findAllByDeletedFalse();

    Teacher save(Teacher teacher);

    List<Teacher> findAllByLanguagesContainingAndDeletedFalse(Language language);

    void deleteById(int id);

    Teacher findById(int id);

    Teacher update(int id, Teacher teacher);

    Teacher updateLanguages(UpdateTeacherLanguageCommand command, int id);
}
