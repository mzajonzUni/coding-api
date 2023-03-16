package pl.zajonz.coding.lesson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajonz.coding.common.exception.ValidationErrorMessage;
import pl.zajonz.coding.lesson.model.command.CreateLessonCommand;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LessonControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSave_ValidationMin() throws Exception {
        //given
        CreateLessonCommand command = new CreateLessonCommand();
        command.setStudentId(0);
        command.setTeacherId(0);
        command.setTerm(LocalDateTime.now().plusDays(1));

        //when //then
        String validationErrors = mockMvc.perform(post("/api/v1/lessons")
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
        assertTrue(validationErrorList.contains("student id must be equal to or greater than 1"));
        assertTrue(validationErrorList.contains("teacher id must be equal to or greater than 1"));
    }

    @Test
    void testSave_ValidationNotNull() throws Exception {
        //given
        CreateLessonCommand command = new CreateLessonCommand();
        command.setStudentId(1);
        command.setTeacherId(1);
        command.setTerm(null);

        //when //then
        mockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Validation errors")))
                .andExpect(jsonPath("$.violations[0].message", equalTo("term cannot be null")));
    }

    @Test
    void testUpdateTerm_ValidationNotNull() throws Exception {
        //given
        CreateLessonCommand command = new CreateLessonCommand();
        command.setStudentId(1);
        command.setTeacherId(1);
        command.setTerm(null);

        //when //then
        mockMvc.perform(patch("/api/v1/lessons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Validation errors")))
                .andExpect(jsonPath("$.violations[0].message", equalTo("term cannot be null")));
    }

}
