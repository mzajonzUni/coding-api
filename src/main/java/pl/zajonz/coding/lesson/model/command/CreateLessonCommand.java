package pl.zajonz.coding.lesson.model.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.zajonz.coding.lesson.model.Lesson;

import java.time.LocalDateTime;

@Data
public class CreateLessonCommand {

    @Min(value = 1, message = "student id must be equal to or greater than 1")
    private int studentId;

    @Min(value = 1, message = "teacher id must be equal to or greater than 1")
    private int teacherId;
    @NotNull(message = "term cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime term;

    public Lesson toEntity() {
        return Lesson.builder()
                .term(term)
                .build();
    }
}
