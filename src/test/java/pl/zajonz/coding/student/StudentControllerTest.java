package pl.zajonz.coding.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.student.model.Student;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void findAll() throws Exception {
        //given
        Student student = Student.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();

        List<Student> students = List.of(student);

        when(studentRepository.findAllByDeletedFalse()).thenReturn(students);

        //when //then
        mockMvc.perform(get("/api/v1/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(students.size())))
                .andExpect(jsonPath("$.[0]", notNullValue()))
                .andExpect(jsonPath("$.[0].id", equalTo(1)))
                .andExpect(jsonPath("$.[0].lastName", equalTo("Testowy")));
    }

    @Test
    void findById() throws Exception {
        //given
        Student student = Student.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .build();

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));


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

        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        //when //then
        mockMvc.perform(get("/api/v1/students/100"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Student with id=100 has not been found")));
    }

    @Test
    void testCreate() {
    }

    @Test
    void testUpdate() {
    }

    @Test
    void testDelete() throws Exception {
        //given

        //when //then
        mockMvc.perform(delete("/api/v1/students/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        Optional<Student> deletedStudent = studentRepository.findById(1);
        assertThat(deletedStudent).isEmpty();

    }
}