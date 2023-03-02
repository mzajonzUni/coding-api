package pl.zajonz.coding.lesson.model.command;

import lombok.Data;
import pl.zajonz.coding.lesson.model.Lesson;
import java.time.LocalDateTime;

@Data
public class CreateLessonCommand {
    private int teacherId;
    private int studentId;
    private LocalDateTime term;

    public Lesson toEntity() {
        return Lesson.builder()
                .term(term)
                .build();
    }
}
