package pl.zajonz.coding.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.common.exception.ValidationErrorMessage;
import pl.zajonz.coding.student.model.command.CreateStudentCommand;
import pl.zajonz.coding.student.model.command.UpdateStudentCommand;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSave_ValidationNotBlank() throws Exception {
        //given
        CreateStudentCommand command = new CreateStudentCommand();
        command.setLastName("");
        command.setFirstName("");
        command.setLanguage(Language.JAVA);
        command.setTeacherId(1);

        //when //then
        String validationErrors = mockMvc.perform(post("/api/v1/students")
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
        CreateStudentCommand command = new CreateStudentCommand();
        command.setLastName("testowy");
        command.setFirstName("test");
        command.setLanguage(Language.JAVA);
        command.setTeacherId(1);

        //when //then
        String validationErrors = mockMvc.perform(post("/api/v1/students")
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
    void testSave_ValidationNotNull() throws Exception {
        //given
        CreateStudentCommand command = new CreateStudentCommand();
        command.setLastName("Testowy");
        command.setFirstName("Test");
        command.setLanguage(null);
        command.setTeacherId(1);

        //when //then
        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Validation errors")))
                .andExpect(jsonPath("$.violations[0].message",
                        equalTo("language cannot be null")));
    }

    @Test
    void testSave_ValidationMin() throws Exception {
        //given
        CreateStudentCommand command = new CreateStudentCommand();
        command.setLastName("Testowy");
        command.setFirstName("Test");
        command.setLanguage(Language.JAVA);
        command.setTeacherId(0);

        //when //then
        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Validation errors")))
                .andExpect(jsonPath("$.violations[0].message",
                        equalTo("teacher id must be equal to or greater than 1")));
    }

    @Test
    void testUpdate_ValidationNotBlank() throws Exception {
        //given
        UpdateStudentCommand command = new UpdateStudentCommand();
        command.setLastName("");
        command.setFirstName("");

        //when //then
        String validationErrors = mockMvc.perform(post("/api/v1/students")
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
        UpdateStudentCommand command = new UpdateStudentCommand();
        command.setLastName("testowy");
        command.setFirstName("test");

        //when //then
        String validationErrors = mockMvc.perform(put("/api/v1/students/1")
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
}
