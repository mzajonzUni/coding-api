package pl.zajonz.coding.teacher;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import pl.zajonz.coding.teacher.model.Teacher;

import java.util.ArrayList;
import java.util.List;

@Repository     //adnotacja do usunięcia w momencie przejścia na interface
public class TeacherRepository {

    private static final List<Teacher> dummyTeacherDb = new ArrayList<>();

    public List<Teacher> findAll() {
        return dummyTeacherDb;
    }

    @PostConstruct
    private static void init() {
        Teacher teacher1 = Teacher.builder()
                .id(1)
                .firstName("Jan")
                .lastName("Kowalski")
                .build();
        Teacher teacher2 = Teacher.builder()
                .id(2)
                .firstName("Janek")
                .lastName("Kowal")
                .build();
        dummyTeacherDb.add(teacher1);
        dummyTeacherDb.add(teacher2);
    }
}
