package pl.zajonz.coding.student.model.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.student.model.Student;

@Data
public class UpdateStudentCommand {

    @NotBlank(message = "first name cannot be blank")
    @Pattern(regexp = "[A-Z][a-z]{1,20}", message = "first name has to match the pattern")
    private String firstName;
    @NotBlank(message = "last name cannot be blank")
    @Pattern(regexp = "[A-Z][a-z]{1,20}", message = "last name has to match the pattern")
    private String lastName;
    @NotEmpty
    private Language language;

    public Student toEntity() {
        return Student.builder()
                .firstName(firstName)
                .lastName(lastName)
                .language(language)
                .build();
    }
}
