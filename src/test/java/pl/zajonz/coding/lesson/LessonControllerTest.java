package pl.zajonz.coding.lesson;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.lesson.model.Lesson;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.teacher.model.Teacher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonRepository lessonRepository;

    @Test
    void findAll() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        Student student = Student.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        Lesson lesson = Lesson.builder()
                .id(1)
                .student(student)
                .teacher(teacher)
                .term(LocalDateTime.now())
                .build();

        List<Lesson> lessons = List.of(lesson);

        when(lessonRepository.findAllByDeletedFalse()).thenReturn(lessons);

        //when //then
        mockMvc.perform(get("/api/v1/lessons"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]", notNullValue()))
                .andExpect(jsonPath("$.[0].id", equalTo(1)))
                .andExpect(jsonPath("$.[0].teacher", notNullValue()))
                .andExpect(jsonPath("$.[0].student", notNullValue()));
    }

    @Test
    void findById() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        Student student = Student.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        Lesson lesson = Lesson.builder()
                .id(1)
                .student(student)
                .teacher(teacher)
                .term(LocalDateTime.now())
                .build();

        when(lessonRepository.findById(1)).thenReturn(Optional.of(lesson));


        //when //then
        mockMvc.perform(get("/api/v1/lessons/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.teacher", notNullValue()))
                .andExpect(jsonPath("$.student", notNullValue()));
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        //given
        when(lessonRepository.findById(1)).thenReturn(Optional.empty());

        //when //then
        mockMvc.perform(get("/api/v1/lessons/100"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Lesson with id=100 has not been found")));
    }

    @Test
    void testCreate() {
    }

    @Test
    void testDeleteLesson() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        Student student = Student.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        Lesson lesson = Lesson.builder()
                .id(1)
                .student(student)
                .teacher(teacher)
                .term(LocalDateTime.now().plusDays(10))
                .build();

        when(lessonRepository.findById(anyInt())).thenReturn(Optional.of(lesson));

        //when //then
        mockMvc.perform(delete("/api/v1/lessons/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
    @Test
    void testDeleteLesson_IncorrectId() throws Exception {
        //given
        when(lessonRepository.findById(anyInt())).thenReturn(Optional.empty());

        //when //then
        mockMvc.perform(delete("/api/v1/lessons/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteLesson_TermInPast() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        Student student = Student.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        Lesson lesson = Lesson.builder()
                .id(1)
                .student(student)
                .teacher(teacher)
                .term(LocalDateTime.now().minusDays(10))
                .build();

        when(lessonRepository.findById(anyInt())).thenReturn(Optional.of(lesson));

        //when //then
        mockMvc.perform(delete("/api/v1/lessons/1"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTerm() {
    }
}