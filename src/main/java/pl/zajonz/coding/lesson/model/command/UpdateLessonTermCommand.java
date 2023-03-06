package pl.zajonz.coding.lesson.model.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateLessonTermCommand {

    @NotNull(message = "term cannot be empty")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Future(message = "term must be in the future")
    private LocalDateTime term;
}
