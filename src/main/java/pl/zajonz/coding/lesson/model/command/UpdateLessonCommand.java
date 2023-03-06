package pl.zajonz.coding.lesson.model.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.zajonz.coding.lesson.model.Lesson;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.teacher.model.Teacher;

import java.time.LocalDateTime;

@Data
public class UpdateLessonCommand {

    @NotNull(message = "student id cannot be null")
    @Min(value = 1, message = "student id must be equal to or greater than 1")
    private Integer studentId;

    @NotNull(message = "teacher id cannot be null")
    @Min(value = 1, message = "teacher id must be equal to or greater than 1")
    private Integer teacherId;
    @NotNull(message = "term cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime term;

    public Lesson toEntity(){
        return Lesson.builder()
                .term(term)
                .build();
    }

}
