package pl.zajonz.coding.student.model.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.student.model.Student;

@Data
public class CreateStudentCommand {

    @NotBlank(message = "first name cannot be blank")
    @Pattern(regexp = "[A-Z][a-z]{1,20}", message = "first name has to match the pattern")
    private String firstName;

    @NotBlank(message = "last name cannot be blank")
    @Pattern(regexp = "[A-Z][a-z]{1,20}", message = "last name has to match the pattern")
    private String lastName;

    @NotEmpty(message = "language list cannot be empty")
    private Language language;

    @NotNull(message = "teacher id cannot be empty")
    private Integer teacherId;

    public Student toEntity() {
        return Student.builder()
                .firstName(firstName)
                .lastName(lastName)
                .language(language)
                .build();
    }
}
