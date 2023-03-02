package pl.zajonz.coding.lesson.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.zajonz.coding.lesson.model.Lesson;

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
