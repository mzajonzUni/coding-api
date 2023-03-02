package pl.zajonz.coding.student.model.command;

import lombok.Data;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.student.model.Student;

@Data
public class CreateStudentCommand {

    private String firstName;
    private String lastName;
    private Language language;

    private int teacherId;

    public Student toEntity() {
        return Student.builder()
                .firstName(firstName)
                .lastName(lastName)
                .language(language)
                .build();
    }
}
