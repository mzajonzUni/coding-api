package pl.zajonz.coding.lesson.model;

import lombok.*;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.teacher.model.Teacher;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Lesson {

    private int id;
    private Student student;
    private Teacher teacher;
    private LocalDateTime term;

}
