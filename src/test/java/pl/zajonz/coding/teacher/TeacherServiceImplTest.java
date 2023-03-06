package pl.zajonz.coding.teacher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.teacher.model.Teacher;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testSave_ResultsInTeacherBeingSaved() {
        //given
        Teacher teacher = Teacher.builder()
                .firstName("Teacher")
                .lastName("LastTeacher")
                .build();

        //when
        teacherServiceImpl.save(teacher);

        //then
        verify(teacherRepository).save(teacher);
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
    void testDeleteById_ResultsInTeacherBeingSaved() {
        //given
        int teacherId = 1;

        //when
        teacherServiceImpl.deleteById(teacherId);

        //then
        verify(teacherRepository).deleteById(teacherId);
    }

}