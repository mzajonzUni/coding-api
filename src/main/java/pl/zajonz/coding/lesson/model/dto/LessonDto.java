package pl.zajonz.coding.lesson.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.zajonz.coding.lesson.model.Lesson;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.student.model.dto.StudentDto;
import pl.zajonz.coding.teacher.model.dto.TeacherDto;

import java.time.LocalDateTime;

@Getter
@Builder
public class LessonDto {

    private int id;
    private TeacherDto teacher;
    private StudentDto student;
    private LocalDateTime term;

    public static LessonDto fromEntity(Lesson Lesson) {
        return LessonDto.builder()
                .id(Lesson.getId())
                .student(StudentDto.fromEntity(Lesson.getStudent()))
                .teacher(TeacherDto.fromEntity(Lesson.getTeacher()))
                .term(Lesson.getTerm())
                .build();
    }

}
