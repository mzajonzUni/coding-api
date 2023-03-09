package pl.zajonz.coding.student;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.student.model.command.CreateStudentCommand;
import pl.zajonz.coding.teacher.TeacherRepository;
import pl.zajonz.coding.teacher.model.Teacher;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @InjectMocks
    private StudentServiceImpl studentServiceImpl;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

//    @Captor
//    private ArgumentCaptor<Student> studentArgumentCaptor;

    @Test
    void testFindAllByDeletedFalse_ResultsInTeacherListBeingReturned() {
        //given
        Teacher teacher = Teacher.builder()
                .languages(Set.of(Language.JAVA))
                .build();
        Student student = Student.builder()
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .teacher(teacher)
                .build();
        List<Student> studentsFromRepo = List.of(student);

        when(studentRepository.findAllByDeletedFalse()).thenReturn(studentsFromRepo);

        //when
        List<Student> returned = studentServiceImpl.findAllByDeletedFalse();

        //then
        assertEquals(studentsFromRepo, returned);
    }

    @Test
    void testSave_CorrectLanguage_ResultsInStudentBeingSaved() {
        //given
        Teacher teacher = Teacher.builder()
                .id(1)
                .languages(Set.of(Language.JAVA))
                .build();
        CreateStudentCommand command = new CreateStudentCommand();
        command.setLanguage(Language.JAVA);
        command.setTeacherId(1);
        Student student = Student.builder()
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .teacher(teacher)
                .build();

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(teacherRepository.findById(anyInt())).thenReturn(Optional.of(teacher));

        //when
        Student returned = studentServiceImpl.save(command);

        //then
        assertEquals(student,returned);
    }

    @Test
    void testSave_IncorrectLanguage_ResultsInIllegalArgumentException() {
        //given
        String exceptionMsg = "Wrong teacher 1";
        Teacher teacher = Teacher.builder()
                .id(1)
                .languages(Set.of(Language.PYTHON))
                .build();
        CreateStudentCommand command = new CreateStudentCommand();
        command.setLanguage(Language.JAVA);
        command.setTeacherId(1);

        when(teacherRepository.findById(anyInt())).thenReturn(Optional.of(teacher));

        //when //then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> studentServiceImpl.save(command))
                .withMessage(exceptionMsg);

        verifyNoInteractions(studentRepository);

        //Inne rozwiązanie
        //        IllegalArgumentException exception = assertThrows(
        //                IllegalArgumentException.class,
        //                () -> studentService.save(student));
        //        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testSave_IncorrectTeacher_ResultsInEntityNotFoundException() {
        //given
        String exceptionMsg = "Teacher with id=1 has not been found";
        CreateStudentCommand command = new CreateStudentCommand();
        command.setLanguage(Language.JAVA);
        command.setTeacherId(1);

        when(teacherRepository.findById(anyInt())).thenReturn(Optional.empty());

        //when //then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> studentServiceImpl.save(command))
                .withMessage(exceptionMsg);

        verifyNoInteractions(studentRepository);

    }

    @Test
    void testDeleteById_ResultsInStudentListBeingDeleted() {
        //given
        int studentId = 1;

        //when
        studentServiceImpl.deleteById(studentId);

        //then
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void testFindAllByTeacher_IdAndDeletedFalse_ResultsInStudentListBeingReturned() {
        int teacherId = 1;
        Teacher teacher = Teacher.builder()
                .languages(Set.of(Language.JAVA))
                .build();
        Student student = Student.builder()
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .teacher(teacher)
                .build();
        List<Student> studentsFromRepo = List.of(student);

        when(studentServiceImpl.findAllByTeacher_IdAndDeletedFalse(teacherId))
                .thenReturn(studentsFromRepo);

        //when
        List<Student> returned = studentServiceImpl.findAllByTeacher_IdAndDeletedFalse(teacherId);

        //then
        assertEquals(studentsFromRepo, returned);
    }

    @Test
    void testFindById_CorrectStudent_ResultsInStudentBeingReturned() {
        //given
        int existingStudentId = 1;
        Teacher teacher = Teacher.builder()
                .languages(Set.of(Language.JAVA))
                .build();
        Student student = Student.builder()
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .teacher(teacher)
                .build();


        when(studentRepository.findById(existingStudentId)).thenReturn(Optional.of(student));
        //when

        Student returned = studentServiceImpl.findById(existingStudentId);

        //then
        assertEquals(student, returned);
    }

    @Test
    void testFindById_IncorrectStudent_ResultsInEntityNotFoundException() {
        //given
        String exceptionMsg = "Student with id=1 has not been found";

        //when //then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentServiceImpl.findById(1));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testUpdate_CorrectValues_ResultsInStudentBeingUpdated(){
        //given
        Student studentToUpdate = Student.builder()
                .firstName("Test")
                .lastName("Testowy")
                .build();
        Student studentUpdated = Student.builder()
                .firstName("Te")
                .lastName("Tes")
                .build();
        when(studentRepository.findById(anyInt())).thenReturn(Optional.of(studentToUpdate));
        when(studentRepository.save(any(Student.class))).thenReturn(studentUpdated);
        //when
        Student returned = studentServiceImpl.update(1,studentUpdated);

        //then
        verify(studentRepository).save(studentToUpdate);
        assertEquals(studentUpdated.getId(),returned.getId());
        assertEquals(studentUpdated,returned);

    }

    @Test
    void testUpdate_IncorrectStudent_ResultsInEntityNotFoundException(){
        //given
        String exceptionMsg = "Student with id=1 has not been found";
        Student student = Student.builder()
                .firstName("Te")
                .lastName("Tes")
                .build();

        when(studentRepository.findById(anyInt())).thenReturn(Optional.empty());
        //when //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> studentServiceImpl.update(1,student));
        assertEquals(exceptionMsg,exception.getMessage());

    }


    //test z użyciem Captora będzie miał sens w momencie kiedy przechwytywany obiekt
    // powstaje już w ramach wykonywania metody, a nie kiedy jest do niej przekazywany
//    @Test
//    void testSave_CorrectLanguage_ResultsInStudentBeingSaved_UsingCaptor() {
//        //given
//        Teacher teacher = Teacher.builder()
//                .languages(Set.of(Language.JAVA))
//                .build();
//        Student student = Student.builder()
//                .firstName("Test")
//                .lastName("Testowy")
//                .language(Language.JAVA)
//                .teacher(teacher)
//                .build();
//
//        //when
//        studentService.save(student);
//
//        //then
//        verify(studentRepository).save(studentArgumentCaptor.capture());
//        Student saved = studentArgumentCaptor.getValue();
//        assertEquals(student.getFirstName(), saved.getFirstName());
//        assertEquals(student.getLastName(), saved.getLastName());
//        assertEquals(student.getLanguage(), saved.getLanguage());
//        assertEquals(student.getTeacher(), saved.getTeacher());
//    }
}