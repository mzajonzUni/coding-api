package pl.zajonz.coding.student.model.command;

import lombok.Data;
import pl.zajonz.coding.student.model.Student;

@Data
public class UpdateStudentCommand {

    private String firstName;
    private String lastName;

    public Student toEntity() {
        return Student.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}
