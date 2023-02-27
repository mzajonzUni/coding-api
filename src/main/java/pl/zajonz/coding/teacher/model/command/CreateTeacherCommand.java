package pl.zajonz.coding.teacher.model.command;

import lombok.Data;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.teacher.model.Teacher;

import java.util.Set;

@Data
public class CreateTeacherCommand {

    private String firstName;
    private String lastName;
    private Set<Language> languages;

    public Teacher toEntity() {
        return Teacher.builder()
                .firstName(firstName)
                .lastName(lastName)
                .languages(languages)
                .build();
    }
}
