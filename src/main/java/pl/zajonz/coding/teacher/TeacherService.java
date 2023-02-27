package pl.zajonz.coding.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.teacher.model.Teacher;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public List<Teacher> findAllByDeletedFalse() {
        return teacherRepository.findAllByDeletedFalse();
    }

    public void save(Teacher teacher){
        teacherRepository.save(teacher);
    }

    public List<Teacher> findAllByLanguagesContainingAndDeletedFalse(Language language) {
        return teacherRepository.findAllByLanguagesContainingAndDeletedFalse(language);
    }

    public void deleteById(int id){
        teacherRepository.deleteById(id);
    }
}
