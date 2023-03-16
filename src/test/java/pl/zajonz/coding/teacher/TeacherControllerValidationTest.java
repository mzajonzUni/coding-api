package pl.zajonz.coding.teacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.common.exception.ValidationErrorMessage;
import pl.zajonz.coding.teacher.model.command.CreateTeacherCommand;
import pl.zajonz.coding.teacher.model.command.UpdateTeacherCommand;
import pl.zajonz.coding.teacher.model.command.UpdateTeacherLanguageCommand;

import java.util.List;
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
public class TeacherControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testSave_ValidationNotBlank() throws Exception {
        //given
        CreateTeacherCommand command = new CreateTeacherCommand();
        command.setLastName("");
        command.setFirstName("");
        command.setLanguages(Set.of(Language.JAVA));

        //when //then
        String validationErrors = mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Validation errors")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ValidationErrorMessage validationErrorMessage = objectMapper.readValue(validationErrors, ValidationErrorMessage.class);
        List<String> validationErrorList = validationErrorMessage.getViolations().stream()
                .map(ValidationErrorMessage.FieldConstraintViolation::getMessage)
                .toList();
        assertTrue(validationErrorList.contains("first name cannot be blank"));
        assertTrue(validationErrorList.contains("last name cannot be blank"));
    }

    @Test
    void testSave_ValidationPattern() throws Exception {
        //given
        CreateTeacherCommand command = new CreateTeacherCommand();
        command.setLastName("testowy");
        command.setFirstName("test");
        command.setLanguages(Set.of(Language.JAVA));

        //when //then
        String validationErrors = mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Validation errors")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ValidationErrorMessage validationErrorMessage = objectMapper.readValue(validationErrors,
                ValidationErrorMessage.class);
        List<String> validationErrorList = validationErrorMessage.getViolations().stream()
                .map(ValidationErrorMessage.FieldConstraintViolation::getMessage)
                .toList();
        assertTrue(validationErrorList.contains("first name has to match the pattern"));
        assertTrue(validationErrorList.contains("last name has to match the pattern"));
    }

    @Test
    void testSave_ValidationNotEmpty() throws Exception {
        //given
        CreateTeacherCommand command = new CreateTeacherCommand();
        command.setLastName("Testowy");
        command.setFirstName("Test");
        command.setLanguages(Set.of());

        //when //then
        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Validation errors")))
                .andExpect(jsonPath("$.violations[0].message",
                        equalTo("language list cannot be empty")));
    }

    @Test
    void testUpdate_ValidationNotBlank() throws Exception {
        //given
        UpdateTeacherCommand command = new UpdateTeacherCommand();
        command.setLastName("");
        command.setFirstName("");
        command.setLanguages(Set.of(Language.JAVA));

        //when //then
        String validationErrors = mockMvc.perform(put("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Validation errors")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ValidationErrorMessage validationErrorMessage = objectMapper.readValue(validationErrors, ValidationErrorMessage.class);
        List<String> validationErrorList = validationErrorMessage.getViolations().stream()
                .map(ValidationErrorMessage.FieldConstraintViolation::getMessage)
                .toList();
        assertTrue(validationErrorList.contains("first name cannot be blank"));
        assertTrue(validationErrorList.contains("last name cannot be blank"));
    }

    @Test
    void testUpdate_ValidationPattern() throws Exception {
        //given
        UpdateTeacherCommand command = new UpdateTeacherCommand();
        command.setLastName("testowy");
        command.setFirstName("test");
        command.setLanguages(Set.of(Language.JAVA));

        //when //then
        String validationErrors = mockMvc.perform(put("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Validation errors")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ValidationErrorMessage validationErrorMessage = objectMapper.readValue(validationErrors,
                ValidationErrorMessage.class);
        List<String> validationErrorList = validationErrorMessage.getViolations().stream()
                .map(ValidationErrorMessage.FieldConstraintViolation::getMessage)
                .toList();
        assertTrue(validationErrorList.contains("first name has to match the pattern"));
        assertTrue(validationErrorList.contains("last name has to match the pattern"));
    }

    @Test
    void testUpdate_ValidationNotEmpty() throws Exception {
        //given
        UpdateTeacherCommand command = new UpdateTeacherCommand();
        command.setLastName("Testowy");
        command.setFirstName("Test");
        command.setLanguages(Set.of());

        //when //then
        mockMvc.perform(put("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Validation errors")))
                .andExpect(jsonPath("$.violations[0].message",
                        equalTo("language list cannot be empty")));
    }

    @Test
    void testUpdateLanguage_ValidationNotEmpty() throws Exception {
        //given
        UpdateTeacherLanguageCommand command = new UpdateTeacherLanguageCommand();
        command.setLanguages(Set.of());

        //when //then
        mockMvc.perform(put("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Validation errors")))
                .andExpect(jsonPath("$.violations[0].message",
                        equalTo("language list cannot be empty")));
    }

}
