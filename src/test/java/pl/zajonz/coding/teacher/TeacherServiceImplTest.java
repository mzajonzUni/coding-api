package pl.zajonz.coding.teacher;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.teacher.model.Teacher;
import pl.zajonz.coding.teacher.model.command.UpdateTeacherLanguageCommand;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //zaprzęga do pracy Mockito (@Mock oraz @InjectMocks)
class TeacherServiceImplTest {

    @InjectMocks
    private TeacherServiceImpl teacherServiceImpl;

    @Mock
    private TeacherRepository teacherRepository;

    //poniższa metoda robi praktycznie to samo co adnotacje @Mock oraz @InjectMocks
//    @BeforeEach
//    void init() {
//        teacherRepository = mock(TeacherRepository.class);
//        teacherService = new TeacherService(teacherRepository);
//    }

    @Test
    void testFindAllByDeletedFalse_ResultsInTeacherListBeingReturned() {
        //given
        Teacher teacher = Teacher.builder()
                .firstName("Test")
                //...
                .build();
        List<Teacher> teachersFromRepo = List.of(teacher);

        when(teacherRepository.findAllByDeletedFalse()).thenReturn(teachersFromRepo);

        //when
        List<Teacher> returned = teacherServiceImpl.findAllByDeletedFalse();

        //then
        assertEquals(teachersFromRepo, returned);
    }
    
    @Test
    void testSave_ResultsInTeacherBeingReturned() {
        //given
        Teacher teacher = Teacher.builder()
                .firstName("Teacher")
                .lastName("LastTeacher")
                .build();

        when(teacherRepository.save(teacher)).thenReturn(teacher);

        //when
        Teacher returned = teacherServiceImpl.save(teacher);

        //then
        assertEquals(teacher, returned);
    }

    @Test
    void testFindAllByLanguagesContainingAndDeletedFalse_ResultsInTeacherListBeingReturned() {
        //given
        Language language = Language.JAVA;
        Teacher teacher = Teacher.builder()
                .firstName("Test")
                .languages(Set.of(language))
                .build();
        List<Teacher> teachersFromRepo = List.of(teacher);

        when(teacherRepository.findAllByLanguagesContainingAndDeletedFalse(language)).thenReturn(teachersFromRepo);

        //when
        List<Teacher> returned = teacherServiceImpl.findAllByLanguagesContainingAndDeletedFalse(language);

        //then
        assertEquals(teachersFromRepo, returned);
    }

    @Test
    void testDeleteById_ResultsInTeacherBeingDeleted() {
        //given
        int teacherId = 1;

        //when
        teacherServiceImpl.deleteById(teacherId);

        //then
        verify(teacherRepository).deleteById(teacherId);
    }

    @Test
    void testFindById_CorrectId_ResultsInTeacherBeingReturned() {
        int teacherId = 1;
        Teacher teacher = Teacher.builder()
                .firstName("TestTeacher")
                .build();
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        //then
        Teacher returned = teacherServiceImpl.findById(teacherId);

        //when
        assertEquals(teacher, returned);
    }

    @Test
    void testFindById_IncorrectId_ResultsInEntityNotFoundException() {
        //given
        int teacherId = 1;
        String exceptionMsg = "Teacher with id=1 has not been found";
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());
        //then //when
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> teacherServiceImpl.findById(teacherId));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testUpdate_CorrectValues_ResultsInTeacherBeingUpdated(){
        //given
        Teacher teacherToUpdate = Teacher.builder()
                .firstName("Teacher")
                .lastName("LastTeacher")
                .build();
        Teacher teacherUpdated = Teacher.builder()
                .firstName("Tea")
                .lastName("Last")
                .build();

        when(teacherRepository.findById(anyInt())).thenReturn(Optional.of(teacherToUpdate));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacherUpdated);

        //when
        Teacher returned = teacherServiceImpl.update(1,teacherUpdated);

        //then
        assertEquals(teacherUpdated.getFirstName(),returned.getFirstName());
        assertEquals(teacherUpdated,returned);
    }

    @Test
    void testUpdate_IncorrectValues_ResultsInEntityNotFoundException(){
        //given
        Teacher teacher = Teacher.builder()
                .firstName("Tea")
                .lastName("Last")
                .build();
        String exceptionMsg = "Teacher with id=1 has not been found";

        when(teacherRepository.findById(anyInt())).thenReturn(Optional.empty());
        //when //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> teacherServiceImpl.update(1, teacher));

        assertEquals(exceptionMsg,exception.getMessage());
    }

    @Test
    void testUpdateLanguages_CorrectValues_ResultsInTeacherBeingUpdated(){
        //given
        UpdateTeacherLanguageCommand command = new UpdateTeacherLanguageCommand();
        command.setLanguages(Set.of(Language.KOBOL,Language.JS));
        Teacher teacherToUpdate = Teacher.builder()
                .firstName("Teacher")
                .lastName("LastTeacher")
                .build();
        Teacher teacherUpdated = Teacher.builder()
                .firstName("Tea")
                .lastName("Last")
                .languages(command.getLanguages())
                .build();

        when(teacherRepository.findById(anyInt())).thenReturn(Optional.of(teacherToUpdate));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacherUpdated);
        //when
        Teacher returned = teacherServiceImpl.updateLanguages(command, 1);
        //then
        assertEquals(teacherUpdated,returned);
        assertEquals(command.getLanguages(),returned.getLanguages());
    }

    @Test
    void testUpdateLanguages_IncorrectTeacher_ResultsInEntityNotFoundException(){
        //given
        UpdateTeacherLanguageCommand command = new UpdateTeacherLanguageCommand();
        command.setLanguages(Set.of(Language.KOBOL,Language.JS));
        String exceptionMsg = "Teacher with id=1 has not been found";

        when(teacherRepository.findById(anyInt())).thenReturn(Optional.empty());
        //when //then

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> teacherServiceImpl.updateLanguages(command,1));

        assertEquals(exceptionMsg,exception.getMessage());
    }
}