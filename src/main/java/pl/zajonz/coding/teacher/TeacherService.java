package pl.zajonz.coding.teacher;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.teacher.model.Teacher;
import pl.zajonz.coding.teacher.model.command.UpdateTeacherCommand;
import pl.zajonz.coding.teacher.model.command.UpdateTeacherLanguageCommand;
import pl.zajonz.coding.teacher.model.dto.TeacherDto;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public List<Teacher> findAllByDeletedFalse() {
        return teacherRepository.findAllByDeletedFalse();
    }

    public Teacher save(Teacher teacher){
        return teacherRepository.save(teacher);
    }

    public List<Teacher> findAllByLanguagesContainingAndDeletedFalse(Language language) {
        return teacherRepository.findAllByLanguagesContainingAndDeletedFalse(language);
    }

    public void deleteById(int id){
        teacherRepository.deleteById(id);
    }

    public Teacher findById(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Teacher with id={0} has not been found", id)));
    }

    public TeacherDto update(UpdateTeacherCommand command, int id) {
        Teacher teacher = findById(id);
        teacher.setFirstName(command.getFirstName());
        teacher.setLastName(command.getLastName());
        teacher.setLanguages(command.getLanguages());
        teacherRepository.save(teacher);
        return TeacherDto.fromEntity(teacher);
    }

    public TeacherDto updateLanguages(UpdateTeacherLanguageCommand command, int id) {
        Teacher teacher = findById(id);
        teacher.setLanguages(command.getLanguages());
        teacherRepository.save(teacher);
        return TeacherDto.fromEntity(teacher);
    }
}
