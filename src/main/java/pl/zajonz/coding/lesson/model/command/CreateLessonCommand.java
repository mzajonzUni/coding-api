package pl.zajonz.coding.lesson.model.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import pl.zajonz.coding.lesson.model.Lesson;
import java.time.LocalDateTime;

@Data
public class CreateLessonCommand {
    @Positive(message = "teacher id must be positive")
    private int teacherId;

    @Positive(message = "student id must be positive")
    private int studentId;

    @NotNull(message = "term cannot be empty")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Future(message = "term must be in the future")
    private LocalDateTime term;

    public Lesson toEntity() {
        return Lesson.builder()
                .term(term)
                .build();
    }
}
