package pl.zajonz.coding.lesson.model.command;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.zajonz.coding.lesson.model.Lesson;

import java.time.LocalDateTime;

@Data
public class UpdateLessonTermCommand {

    @NotNull(message = "term cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime term;

    public Lesson toEntity(){
        return Lesson.builder()
                .term(term)
                .build();
    }

}
