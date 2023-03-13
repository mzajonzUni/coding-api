package pl.zajonz.coding.teacher.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.SQLDelete;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.lesson.model.Lesson;
import pl.zajonz.coding.student.model.Student;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@SQLDelete(sql = "UPDATE teacher SET deleted = true WHERE id=?")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;

    private boolean deleted = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "teacher_language", joinColumns = @JoinColumn(name = "teacher_id"))
    @Column(name = "language")
    private Set<Language> languages;

    @OneToMany(mappedBy = "teacher")
    private Set<Lesson> lessons;

    @OneToMany(mappedBy = "teacher")
    private Set<Student> students;

    public Teacher(int id, String firstName, String lastName, boolean deleted) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.deleted = deleted;
    }

    public String toString() {
        return firstName + " " + lastName;
    }
}
