package pl.zajonz.coding.student.model;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Student {

    private int id;
    private String firstName;
    private String lastName;
    private String language;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
