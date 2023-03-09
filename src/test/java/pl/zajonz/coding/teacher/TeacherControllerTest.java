package pl.zajonz.coding.teacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.teacher.model.Teacher;
import pl.zajonz.coding.teacher.model.command.CreateTeacherCommand;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherRepository teacherRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFindAll() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        List<Teacher> teachers = List.of(teacher);

//        teacherRepository.save(teacher);
        when(teacherRepository.findAllByDeletedFalse()).thenReturn(teachers);

        //when //then
        mockMvc.perform(get("/api/v1/teachers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(teachers.size())))
                .andExpect(jsonPath("$.[0]", notNullValue()))
                .andExpect(jsonPath("$.[0].id", equalTo(1)))
                .andExpect(jsonPath("$.[0].id").value(1));
        // TODO: 09.03.2023 w razie potrzeby, jeśli jest takie wymaganie, to możemy testować trochę głębiej - wszystkie dane nauczyciela itd.
    }

    @Test
    void testFindById() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();

        when(teacherRepository.findById(anyInt())).thenReturn(Optional.of(teacher));

        //when //then
        mockMvc.perform(get("/api/v1/teachers/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.firstName", equalTo("Test")));
        //lepiej sprawdzić wszystkie dane
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        //given

        when(teacherRepository.findById(anyInt())).thenReturn(Optional.empty());

        //when //then
        mockMvc.perform(get("/api/v1/teachers/100"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Teacher with id=100 has not been found")));
    }

    @Test
    void testSave() throws Exception {
        //given
        CreateTeacherCommand command = new CreateTeacherCommand();
        command.setLastName("Testowy");
        command.setFirstName("Test");
        command.setLanguages(Set.of(Language.JAVA));

        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();

        when(teacherRepository.save(teacher)).thenReturn(teacher);

        //when //then
        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.firstName", equalTo("Test")));
    }
}