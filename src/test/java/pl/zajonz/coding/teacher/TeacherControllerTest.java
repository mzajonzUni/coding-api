package pl.zajonz.coding.teacher;

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
import pl.zajonz.coding.teacher.model.Teacher;
import pl.zajonz.coding.teacher.model.command.CreateTeacherCommand;
import pl.zajonz.coding.teacher.model.command.UpdateTeacherCommand;
import pl.zajonz.coding.teacher.model.command.UpdateTeacherLanguageCommand;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

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

//        List<Teacher> teachers = List.of(teacher);
//        when(teacherRepository.findAllByDeletedFalse()).thenReturn(teachers);

        //when //then
        mockMvc.perform(get("/api/v1/teachers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$", hasSize(teachers.size())))
                .andExpect(jsonPath("$.[0]", notNullValue()))
                .andExpect(jsonPath("$.[0].id", equalTo(1)))
                .andExpect(jsonPath("$.[0].id").value(1));
        // TODO: 09.03.2023 w razie potrzeby, jeśli jest takie wymaganie, to możemy testować trochę głębiej - wszystkie dane nauczyciela itd.
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

//        when(teacherRepositoryMock.findById(anyInt())).thenReturn(Optional.of(teacher));

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
    @Transactional
    void testFindByIdNotFound() throws Exception {
        //given
//        when(teacherRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());

        //when //then
        mockMvc.perform(get("/api/v1/teachers/0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Teacher with id=0 has not been found")));
    }

    @Test
    @Transactional
    void testSave() throws Exception {
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
    @Transactional
    void testDelete() throws Exception {
        //given
        Teacher teacher = Teacher.builder()
                .firstName("Testa")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        teacherRepository.save(teacher);

        //when //then
        mockMvc.perform(delete("/api/v1/teachers/" + teacher.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
        Optional<Teacher> deletedTeacher = teacherRepository.findById(teacher.getId());
        deletedTeacher.ifPresent(value -> assertTrue(value.isDeleted()));
    }

    @Test
    @Transactional
    void testDelete_IncorrectTeacher() throws Exception {
        //given
        teacherRepository.deleteById(1);
        //when //then
        mockMvc.perform(delete("/api/v1/teachers/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message",
                        equalTo("No class pl.zajonz.coding.teacher.model.Teacher entity with id 1 exists")));
    }


    @Test
    @Transactional
    void testUpdate() throws Exception {
        //given
        Teacher teacherToUpdate = Teacher.builder()
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
        MvcResult result = mockMvc.perform(put("/api/v1/teachers/" + teacherToUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(teacherToUpdate.getId())))
                .andExpect(jsonPath("$.firstName", equalTo("Test")))
                .andExpect(jsonPath("$.lastName", equalTo("Testowy")))
                .andReturn();

        Set<String> actualSet = new HashSet<>(JsonPath.read(result.getResponse().getContentAsString(),
                "$.languages"));
        Set<String> expectedSet = command.getLanguages().stream()
                .map(Objects::toString)
                .collect(Collectors.toSet());

        assertEquals(expectedSet, actualSet);
    }

    @Test
    @Transactional
    void testUpdate_IncorrectTeacher() throws Exception {
        //given
        teacherRepository.deleteById(1);
        UpdateTeacherCommand command = new UpdateTeacherCommand();
        command.setFirstName("Test");
        command.setLastName("Testowy");
        command.setLanguages(Set.of(Language.JS));

        //when //then
        mockMvc.perform(patch("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message",
                        equalTo("Teacher with id=1 has not been found")));
    }

    @Test
    @Transactional
    void testUpdateLanguages() throws Exception {
        //given
        Teacher teacherToUpdate = Teacher.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(Language.JAVA))
                .build();
        teacherRepository.save(teacherToUpdate);
        UpdateTeacherLanguageCommand command = new UpdateTeacherLanguageCommand();
        command.setLanguages(Set.of(Language.JS, Language.KOBOL));

        //when //then
        MvcResult result = mockMvc.perform(patch("/api/v1/teachers/" + teacherToUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(teacherToUpdate.getId())))
                .andExpect(jsonPath("$.firstName", equalTo("Test")))
                .andExpect(jsonPath("$.lastName", equalTo("Testowy")))
                .andReturn();

        Set<String> actualSet = new HashSet<>(JsonPath.read(result.getResponse().getContentAsString(),
                "$.languages"));
        Set<String> expectedSet = command.getLanguages().stream()
                .map(Objects::toString)
                .collect(Collectors.toSet());

        assertEquals(expectedSet, actualSet);
    }

    @Test
    @Transactional
    void testUpdateLanguages_IncorrectTeacher() throws Exception {
        //given
        teacherRepository.deleteById(1);
        UpdateTeacherLanguageCommand command = new UpdateTeacherLanguageCommand();
        command.setLanguages(Set.of(Language.JS, Language.KOBOL));

        //when //then
        mockMvc.perform(patch("/api/v1/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message",
                        equalTo("Teacher with id=1 has not been found")));
    }
}