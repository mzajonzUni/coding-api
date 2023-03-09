package pl.zajonz.coding.teacher;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.teacher.model.Teacher;
import pl.zajonz.coding.teacher.model.command.UpdateTeacherLanguageCommand;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    @Override
    public List<Teacher> findAllByDeletedFalse() {
        return teacherRepository.findAllByDeletedFalse();
    }

    @Override
    public Teacher save(Teacher teacher){
        return teacherRepository.save(teacher);
    }

    @Override
    public List<Teacher> findAllByLanguagesContainingAndDeletedFalse(Language language) {
        return teacherRepository.findAllByLanguagesContainingAndDeletedFalse(language);
    }

    @Override
    public void deleteById(int id){
        teacherRepository.deleteById(id);
    }

    @Override
    public Teacher findById(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Teacher with id={0} has not been found", id)));
    }

    @Override
    public Teacher update(int id, Teacher teacher) {
        Teacher teacherToUpdate = findById(id);
        teacherToUpdate.setFirstName(teacher.getFirstName());
        teacherToUpdate.setLastName(teacher.getLastName());
        teacherToUpdate.setLanguages(teacher.getLanguages());
        return teacherRepository.save(teacherToUpdate);
    }

    @Override
    public Teacher updateLanguages(UpdateTeacherLanguageCommand command, int id) {
        Teacher teacher = findById(id);
        teacher.setLanguages(command.getLanguages());
        return teacherRepository.save(teacher);
    }
}
