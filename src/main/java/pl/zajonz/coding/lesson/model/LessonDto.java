package pl.zajonz.coding.lesson.model;

import lombok.Builder;
import lombok.Getter;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.student.model.StudentDto;

import java.time.LocalDateTime;

@Getter
@Builder
public class LessonDto {

    private int id;
    private LocalDateTime term;

    public static LessonDto fromEntity(Lesson Lesson) {
        return LessonDto.builder()
                .id(Lesson.getId())
                .term(Lesson.getTerm())
                .build();
    }

}
