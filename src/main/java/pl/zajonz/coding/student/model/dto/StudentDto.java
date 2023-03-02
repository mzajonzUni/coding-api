package pl.zajonz.coding.student.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.student.model.Student;

@Getter
@Builder
public class StudentDto {

    private int id;
    private String firstName;
    private String lastName;
    private Language language;

    public static StudentDto fromEntity(Student student) {
        return StudentDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .language(student.getLanguage())
                .build();
    }

}
