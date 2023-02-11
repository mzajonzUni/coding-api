package pl.zajonz.coding.lesson;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import pl.zajonz.coding.lesson.model.Lesson;
import pl.zajonz.coding.student.StudentRepository;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.teacher.model.Teacher;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LessonRepository {

    private static final List<Lesson> dummyLessonDb = new ArrayList<>();

    public List<Lesson> findAll(){
        return dummyLessonDb;
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

        Student student1 = Student.builder()
                .id(1)
                .firstName("Mateusz")
                .lastName("Zajonz")
                .language("Java")
                .build();
        Student student2 = Student.builder()
                .id(1)
                .firstName("Andrzej")
                .lastName("Duda")
                .language("Angielski")
                .build();

        Lesson lesson1 = Lesson.builder()
                .id(1)
                .student(student1)
                .teacher(teacher1)
                .build();
        Lesson lesson2 = Lesson.builder()
                .id(2)
                .student(student2)
                .teacher(teacher2)
                .build();
        dummyLessonDb.add(lesson1);
        dummyLessonDb.add(lesson2);
    }

}
