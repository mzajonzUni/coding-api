package pl.zajonz.coding.lesson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.lesson.model.Lesson;
import pl.zajonz.coding.lesson.model.command.CreateLessonCommand;
import pl.zajonz.coding.lesson.model.command.UpdateLessonTermCommand;
import pl.zajonz.coding.student.StudentRepository;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.teacher.TeacherRepository;
import pl.zajonz.coding.teacher.model.Teacher;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void testFindAll() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        teacherRepository.save(teacher);
        Student student = Student.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        studentRepository.save(student);
        Lesson lesson = Lesson.builder()
                .id(1)
                .student(student)
                .teacher(teacher)
                .term(LocalDateTime.now())
                .build();
        lessonRepository.save(lesson);

//        List<Lesson> lessons = List.of(lesson);
//
//        when(lessonRepository.findAllByDeletedFalse()).thenReturn(lessons);

        //when //then
        mockMvc.perform(get("/api/v1/lessons"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]", notNullValue()))
                .andExpect(jsonPath("$.[0].id", equalTo(1)));
    }

    @Test
    @Transactional
    void testFindById() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        teacherRepository.save(teacher);
        Student student = Student.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        studentRepository.save(student);
        Lesson lesson = Lesson.builder()
                .student(student)
                .teacher(teacher)
                .term(LocalDateTime.now())
                .build();
        lessonRepository.save(lesson);

//        when(lessonRepository.findById(1)).thenReturn(Optional.of(lesson));


        //when //then
        mockMvc.perform(get("/api/v1/lessons/" + lesson.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(lesson.getId())))
                .andExpect(jsonPath("$.teacher", notNullValue()))
                .andExpect(jsonPath("$.student", notNullValue()));
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        //given
//        when(lessonRepository.findById(1)).thenReturn(Optional.empty());

        //when //then
        mockMvc.perform(get("/api/v1/lessons/0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Lesson with id=0 has not been found")));
    }

    @Test
    @Transactional
    void testCreate() throws Exception {
        //given
        CreateLessonCommand command = new CreateLessonCommand();
        command.setTeacherId(1);
        command.setStudentId(1);
        command.setTerm(LocalDateTime.now().plusDays(20));

        //when //then

        mockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.term").exists());
    }

    @Test
    @Transactional
    void testDeleteLesson() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        teacherRepository.save(teacher);
        Student student = Student.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        studentRepository.save(student);
        Lesson lesson = Lesson.builder()
                .student(student)
                .teacher(teacher)
                .term(LocalDateTime.now().plusDays(10))
                .build();
        lessonRepository.save(lesson);

//        when(lessonRepository.findById(anyInt())).thenReturn(Optional.of(lesson));

        //when //then
        mockMvc.perform(delete("/api/v1/lessons/" + lesson.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
        Optional<Lesson> deletedLesson = lessonRepository.findById(teacher.getId());
        deletedLesson.ifPresent(value -> assertTrue(value.isDeleted()));
    }

    @Test
    @Transactional
    void testDeleteLesson_IncorrectId() throws Exception {
        //given
//        when(lessonRepository.findById(anyInt())).thenReturn(Optional.empty());

        //when //then
        mockMvc.perform(delete("/api/v1/lessons/0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message",
                        equalTo("Lesson with id=0 has not been found")));
    }

    @Test
    @Transactional
    void testDeleteLesson_TermInPast() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        teacherRepository.save(teacher);
        Student student = Student.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        studentRepository.save(student);
        Lesson lesson = Lesson.builder()
                .id(1)
                .student(student)
                .teacher(teacher)
                .term(LocalDateTime.now().minusDays(10))
                .build();
        lessonRepository.save(lesson);

//        when(lessonRepository.findById(anyInt())).thenReturn(Optional.of(lesson));

        //when //then
        mockMvc.perform(delete("/api/v1/lessons/1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message",
                        equalTo("The term is in the past")));
    }

    @Test
    @Transactional
    void testUpdateTerm() throws Exception{
        //given
        Student student = Student.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        studentRepository.save(student);
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        teacherRepository.save(teacher);
        Lesson lesson = Lesson.builder()
                .id(1)
                .student(student)
                .teacher(teacher)
                .term(LocalDateTime.now().plusDays(1))
                .build();
        lessonRepository.save(lesson);
        UpdateLessonTermCommand command = new UpdateLessonTermCommand();
        command.setTerm(LocalDateTime.now().plusDays(100));

        //when //then
        MvcResult result = mockMvc.perform(patch("/api/v1/lessons/" + lesson.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(lesson.getId())))
                .andReturn();

        String term = JsonPath.read(result.getResponse().getContentAsString(),"$.term");
        LocalDateTime termReturned = LocalDateTime.parse(term);
        assertEquals(command.getTerm(),termReturned);
    }
}