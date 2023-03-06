package pl.zajonz.coding.teacher.model.command;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.teacher.model.Teacher;

import java.util.Set;

@Data
public class UpdateTeacherLanguageCommand {

    @NotEmpty(message = "language list cannot be empty")
    private Set<Language> languages;

    public Teacher toEntity() {
        return Teacher.builder()
                .languages(languages)
                .build();
    }
}
