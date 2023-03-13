package pl.zajonz.coding.teacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerDBTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ObjectMapper objectMapper;

    Teacher teacher;

    @BeforeEach
    void init() {
        teacher = new Teacher(1, "Test", "Test", false);
        teacherRepository.save(teacher);
    }

    @AfterEach
    void after() {
        teacherRepository.deleteById(1);
    }

    @Test
    void testFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/teachers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0]", notNullValue()))
                .andExpect(jsonPath("$.[0].id", equalTo(1)))
                .andExpect(jsonPath("$.[0].id").value(1));
    }

    @Test
    void testFindById() throws Exception {
        mockMvc.perform(get("/api/v1/teachers/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.firstName", equalTo("Test")));
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/teachers/100"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Teacher with id=100 has not been found")));
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/api/v1/teachers/1"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());

        assertEquals(teacherRepository.findById(1).get().isDeleted(), true);
    }

    @Test
    void testSave() throws Exception {
        //given
        CreateTeacherCommand command = new CreateTeacherCommand();
        command.setFirstName("Test");
        command.setLastName("Testowy");
        command.setLanguages(Set.of(Language.JAVA));

        //when //then
        String responseJson = mockMvc.perform(
                        post("/api/v1/teachers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(command))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName", equalTo("Test")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Teacher response = objectMapper.readValue(responseJson, Teacher.class);
        Teacher teaacher = teacherRepository.findById(response.getId()).get();

        assertEquals(response.getFirstName(), teaacher.getFirstName());
    }

    @Test
    void testUpdate() throws Exception {
        //given
        UpdateTeacherCommand command = new UpdateTeacherCommand();
        command.setFirstName("Testaaaaa");
        command.setLastName("Testowyyyyy");
        command.setLanguages(Set.of(Language.PYTHON));

        //when //then
        String responseJson = mockMvc.perform(
                        put("/api/v1/teachers/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(command))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName", equalTo("Testaaaaa")))
                .andExpect(jsonPath("$.lastName", equalTo("Testowyyyyy")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Teacher response = objectMapper.readValue(responseJson, Teacher.class);
        Teacher teaacher = teacherRepository.findById(response.getId()).get();

        assertEquals(response.getFirstName(), teaacher.getFirstName());
        assertEquals(response.getLastName(), teaacher.getLastName());
    }

    @Test
    void testUpdateShouldThrowException() throws Exception {
        //given
        UpdateTeacherCommand command = new UpdateTeacherCommand();
        command.setFirstName("Test");
        command.setLastName("Testowy");
        command.setLanguages(Set.of(Language.JAVA));

        //when //then
        mockMvc.perform(
                        put("/api/v1/teachers/100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(command))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Teacher with id=100 has not been found")));
    }

    @Test
    void testPatch() throws Exception {
        //given
        UpdateTeacherLanguageCommand command = new UpdateTeacherLanguageCommand();
        command.setLanguages(Set.of(Language.PYTHON));

        //when //then
        String responseJson = mockMvc.perform(
                        patch("/api/v1/teachers/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(command))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.languages", contains(Language.PYTHON.toString())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Teacher response = objectMapper.readValue(responseJson, Teacher.class);
        Teacher teaacher = teacherRepository.findById(response.getId()).get();

        assertEquals(response.getLanguages(), teaacher.getLanguages());
        assertEquals(response.getLanguages(), teaacher.getLanguages());
    }
}