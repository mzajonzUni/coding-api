package pl.zajonz.coding.student;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import pl.zajonz.coding.student.model.Student;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepository {

    private static final List<Student> dummyStudentDb = new ArrayList<>();

    public List<Student> findAll() {
        return dummyStudentDb;
    }

    @PostConstruct
    private static void init(){
        Student student1 = Student.builder()
                .id(1)
                .firstName("Mateusz")
                .lastName("Zajonz")
                .language("Java")
                .build();
        Student student2 = Student.builder()
                .id(2)
                .firstName("Andrzej")
                .lastName("Duda")
                .language("Angielski")
                .build();
        dummyStudentDb.add(student1);
        dummyStudentDb.add(student2);
    }

}
