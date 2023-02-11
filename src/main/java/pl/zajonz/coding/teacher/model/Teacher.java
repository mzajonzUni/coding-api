package pl.zajonz.coding.teacher.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Teacher {

    private int id;
    private String firstName;
    private String lastName;

    public String toString() {
        return firstName + " " + lastName;
    }
}
