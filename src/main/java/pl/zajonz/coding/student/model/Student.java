package pl.zajonz.coding.student.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.lesson.model.Lesson;
import pl.zajonz.coding.teacher.model.Teacher;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@SQLDelete(sql = "UPDATE coding_db.student SET deleted = true WHERE id=?")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Language language;

    @OneToMany(mappedBy = "student")
    private Set<Lesson> lessons;
    @ManyToOne
    private Teacher teacher;
    private boolean deleted = Boolean.FALSE;
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
