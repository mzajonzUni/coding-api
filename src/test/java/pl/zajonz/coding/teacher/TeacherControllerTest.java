package pl.zajonz.coding.teacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.teacher.model.Teacher;
import pl.zajonz.coding.teacher.model.command.CreateTeacherCommand;
import pl.zajonz.coding.teacher.model.command.UpdateTeacherCommand;
import pl.zajonz.coding.teacher.model.command.UpdateTeacherLanguageCommand;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
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
        teacherRepository.save(teacher);

        List<Teacher> teachers = List.of(teacher);

        //when //then
        mockMvc.perform(get("/api/v1/teachers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(teachers.size())))
                .andExpect(jsonPath("$.[0]", notNullValue()))
//                .andExpect(jsonPath("$.[0].id", equalTo(1)))
                .andExpect(jsonPath("$.[0].id").value(1));
        // TODO: 09.03.2023 w razie potrzeby, jeśli jest takie wymaganie, to możemy testować trochę głębiej - wszystkie dane nauczyciela itd.
    }

    @Test
    void testFindById_CorrectValue() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        teacherRepository.save(teacher);

        //when //then
        mockMvc.perform(get("/api/v1/teachers/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(teacher.getId())))
                .andExpect(jsonPath("$.firstName", equalTo("Test")));
        //lepiej sprawdzić wszystkie dane
    }

    @Test
    void testFindById_IncorrectTeacher_ShouldThrowException() throws Exception {
        //given

        //when //then
        mockMvc.perform(get("/api/v1/teachers/100"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Teacher with id=100 has not been found")));
    }

    @Test
    void testSave_CorrectValues() throws Exception {
        //given
        CreateTeacherCommand command = new CreateTeacherCommand();
        command.setLastName("Testowy");
        command.setFirstName("Test");
        command.setLanguages(Set.of(Language.JAVA));

        //when //then
        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", equalTo("Test")))
                .andExpect(jsonPath("$.lastName", equalTo("Testowy")));
    }

    @Test
    void testDelete_CorrectTeacher() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("Testa")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        teacherRepository.save(teacher);

        //when //then
        mockMvc.perform(delete("/api/v1/teachers/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        Optional<Teacher> deletedTeacher = teacherRepository.findById(1);
        deletedTeacher.ifPresent(value -> assertTrue(value.isDeleted()));
    }

    @Test
    void testDelete_IncorrectTeacher_ShouldThrowException() throws Exception {
        //given

        //when //then
        mockMvc.perform(delete("/api/v1/teachers/100"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message",
                        equalTo("No class pl.zajonz.coding.teacher.model.Teacher entity with id 100 exists")));
    }


    @Test
    void testUpdate_CorrectValues() throws Exception {
        //given
        Teacher teacherToUpdate = Teacher.builder()
                .id(1)
                .firstName("FirstTest")
                .lastName("LastTest")
                .languages(Set.of(Language.JAVA))
                .build();
        teacherRepository.save(teacherToUpdate);
        UpdateTeacherCommand command = new UpdateTeacherCommand();
        command.setFirstName("Test");
        command.setLastName("Testowy");
        command.setLanguages(Set.of(Language.JS));

        //when //then
        String responseJson = mockMvc.perform(put("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.firstName", equalTo("Test")))
                .andExpect(jsonPath("$.lastName", equalTo("Testowy")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Teacher response = objectMapper.readValue(responseJson, Teacher.class);
        assertEquals(command.getLanguages(),response.getLanguages());
    }

    @Test
    void testUpdate_IncorrectTeacher_ShouldThrowEntityNotFoundException() throws Exception {
        //given
        UpdateTeacherCommand command = new UpdateTeacherCommand();
        command.setFirstName("Test");
        command.setLastName("Testowy");
        command.setLanguages(Set.of(Language.JS));

        //when //then
        mockMvc.perform(patch("/api/v1/teachers/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message",
                        equalTo("Teacher with id=100 has not been found")));
    }

    @Test
    void testUpdateLanguages_CorrectValues() throws Exception {
        //given
        Teacher teacherToUpdate = Teacher.builder()
                .id(1)
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        teacherRepository.save(teacherToUpdate);
        UpdateTeacherLanguageCommand command = new UpdateTeacherLanguageCommand();
        command.setLanguages(Set.of( Language.KOBOL, Language.JS));

        //when //then
        String responseJson = mockMvc.perform(patch("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.firstName", equalTo("Test")))
                .andExpect(jsonPath("$.lastName", equalTo("Testowy")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Teacher response = objectMapper.readValue(responseJson, Teacher.class);
        assertEquals(command.getLanguages(),response.getLanguages());
    }

    @Test
    void testUpdateLanguages_IncorrectTeacher_ShouldThrowEntityNotFoundException() throws Exception {
        //given
        UpdateTeacherLanguageCommand command = new UpdateTeacherLanguageCommand();
        command.setLanguages(Set.of(Language.JS, Language.KOBOL));

        //when //then
        mockMvc.perform(patch("/api/v1/teachers/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message",
                        equalTo("Teacher with id=100 has not been found")));
    }
}