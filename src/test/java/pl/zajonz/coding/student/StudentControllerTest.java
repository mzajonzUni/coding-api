package pl.zajonz.coding.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.student.model.command.CreateStudentCommand;
import pl.zajonz.coding.student.model.command.UpdateStudentCommand;
import pl.zajonz.coding.teacher.TeacherRepository;
import pl.zajonz.coding.teacher.model.Teacher;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFindAll() throws Exception {
        //given
        Student student = Student.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();
        studentRepository.save(student);

//        List<Student> students = List.of(student);
//        when(studentRepository.findAllByDeletedFalse()).thenReturn(students);

        //when //then
        mockMvc.perform(get("/api/v1/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$", hasSize(students.size())))
                .andExpect(jsonPath("$.[0]", notNullValue()))
                .andExpect(jsonPath("$.[0].id", equalTo(1)))
                .andExpect(jsonPath("$.[0].lastName", equalTo("Testowy")));
    }

    @Test
    void testFindById() throws Exception {
        //given
        Student student = Student.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();

        studentRepository.save(student);

//        when(studentRepository.findById(1)).thenReturn(Optional.of(student));


        //when //then
        mockMvc.perform(get("/api/v1/students/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.firstName", equalTo("Test")));
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        //given
//        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        //when //then
        mockMvc.perform(get("/api/v1/students/0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Student with id=0 has not been found")));
    }

    @Test
    @Transactional
    void testCreate() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .firstName("Testa")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        teacherRepository.save(teacher);
        CreateStudentCommand command = new CreateStudentCommand();
        command.setTeacherId(teacher.getId());
        command.setLastName("Testowy");
        command.setFirstName("Test");
        command.setLanguage(Language.JAVA);

        //when //then

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", equalTo("Test")))
                .andExpect(jsonPath("$.lastName", equalTo("Testowy")));
    }

    @Test
    @Transactional
    void testUpdate() throws Exception {
        //given
        Student student = Student.builder()
                .firstName("First")
                .lastName("Last")
                .language(Language.JS)
                .build();
        studentRepository.save(student);
        UpdateStudentCommand command = new UpdateStudentCommand();
        command.setLastName("Testowy");
        command.setFirstName("Test");

        //when //then
        mockMvc.perform(put("/api/v1/students/" + student.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(student.getId())))
                .andExpect(jsonPath("$.firstName", equalTo("Test")))
                .andExpect(jsonPath("$.lastName", equalTo("Testowy")));
    }

    @Test
    @Transactional
    void testUpdate_IncorrectStudent() throws Exception {
        //given
        studentRepository.deleteById(1);
        UpdateStudentCommand command = new UpdateStudentCommand();
        command.setLastName("Testowy");
        command.setFirstName("Test");

        //when //then
        mockMvc.perform(put("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message",
                        equalTo("Student with id=1 has not been found")));
    }

    @Test
    @Transactional
    void testDelete() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .firstName("FirstTest")
                .lastName("LastTest")
                .languages(Set.of(Language.JAVA, Language.KOBOL))
                .build();
        Student student = Student.builder()
                .firstName("Test")
                .lastName("Testowy")
                .teacher(teacher)
                .language(Language.JAVA)
                .build();
        studentRepository.save(student);

        //when //then
        mockMvc.perform(delete("/api/v1/students/" + student.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
        Optional<Student> deletedStudent = studentRepository.findById(teacher.getId());
        deletedStudent.ifPresent(value -> assertTrue(value.isDeleted()));
    }

    @Test
    @Transactional
    void testDelete_IncorrectStudent() throws Exception {
        //given
        studentRepository.deleteById(1);
        //when //then
        mockMvc.perform(delete("/api/v1/students/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message",
                        equalTo("No class pl.zajonz.coding.student.model.Student entity with id 1 exists")));
    }
}