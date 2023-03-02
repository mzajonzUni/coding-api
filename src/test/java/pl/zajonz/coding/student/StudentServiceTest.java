package pl.zajonz.coding.student;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.teacher.TeacherRepository;
import pl.zajonz.coding.teacher.model.Teacher;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

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
        List<Student> returned = studentService.findAllByDeletedFalse();

        //then
        assertEquals(studentsFromRepo, returned);
    }

//    @Test
//    void testSave_CorrectLanguage_ResultsInStudentBeingSaved() {
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
//        verify(studentRepository).save(student);
//    }

//    @Test
//    void testSave_IncorrectLanguage_ResultsInIllegalArgumentException() {
//        //given
//        String exceptionMsg = "Wrong teacher";
//        Teacher teacher = Teacher.builder()
//                .languages(Set.of(Language.PYTHON))
//                .build();
//        Student student = Student.builder()
//                .firstName("Test")
//                .lastName("Testowy")
//                .language(Language.JAVA)
//                .teacher(teacher)
//                .build();
//
//        //when //then
//        assertThatExceptionOfType(IllegalArgumentException.class)
//                .isThrownBy(() -> studentService.save(student))
//                .withMessage(exceptionMsg);
//
//        verifyNoInteractions(studentRepository);
//
//        //Inne rozwiązanie
//        //        IllegalArgumentException exception = assertThrows(
//        //                IllegalArgumentException.class,
//        //                () -> studentService.save(student));
//        //        assertEquals(exceptionMsg, exception.getMessage());
//    }

    @Test
    void testDeleteById_ResultsInStudentListBeingDeleted() {
        //given
        int studentId = 1;

        //when
        studentService.deleteById(studentId);

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

        when(studentService.findAllByTeacher_IdAndDeletedFalse(teacherId))
                .thenReturn(studentsFromRepo);

        //when
        List<Student> returned = studentService.findAllByTeacher_IdAndDeletedFalse(teacherId);

        //then
        assertEquals(studentsFromRepo, returned);
    }

    @Test
    void testFindById_CorrectStudent_ResultsInStudentBeingUpdated() {
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

        Student returned = studentService.findById(existingStudentId);

        //then
        assertEquals(student, returned);

    }

    @Test
    void testFindById_IncorrectStudent_ResultsInNoSuchElementException() {
        //given
        String exceptionMsg = "No such student with Id " + 1;

        //when //then
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> studentService.findById(1));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testUpdateStudent_CorrectValues_ResultsInStudentBeingReturned() {
        //given
        int studentId = 1;
        int newTeacherId = 1;
        Teacher teacher = Teacher.builder()
                .firstName("TestowyT1")
                .languages(Set.of(Language.JAVA))
                .build();
        Teacher teacher2 = Teacher.builder()
                .firstName("TestowyT2")
                .languages(Set.of(Language.JAVA))
                .build();
        Student student = Student.builder()
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .teacher(teacher2)
                .build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(teacherRepository.findById(newTeacherId)).thenReturn(Optional.of(teacher));

        //when

        studentService.updateStudent(studentId, newTeacherId);

        //then
        assertEquals(teacher, student.getTeacher());
        verify(studentRepository).save(student);
    }

    @Test
    void testUpdateStudent_IncorrectTeacher_ResultsInNoSuchElementException() {
        //given
        int studentId = 1;
        int newTeacherId = 1;
        String exceptionMsg = "No such teacher with Id " + newTeacherId;
        Teacher teacher = Teacher.builder()
                .firstName("TestowyT1")
                .languages(Set.of(Language.JAVA))
                .build();
        Student student = Student.builder()
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .teacher(teacher)
                .build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(teacherRepository.findById(newTeacherId)).thenReturn(Optional.empty());

        //when //then
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> studentService.updateStudent(studentId, newTeacherId));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testUpdateStudent_IncorrectStudent_ResultsInNoSuchElementException() {
        //given
        int studentId = 1;
        int newTeacherId = 1;

        String exceptionMsg = "No such student with Id " + newTeacherId;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        //when //then
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> studentService.updateStudent(studentId, newTeacherId));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testUpdateStudent_IncorrectLanguage_ResultsInIllegalArgumentException() {
        //given
        int studentId = 1;
        int newTeacherId = 1;

        String exceptionMsg = "Wrong teacher";
        Teacher teacher = Teacher.builder()
                .languages(Set.of(Language.C))
                .build();
        Student student = Student.builder()
                .firstName("Test")
                .lastName("Testowy")
                .language(Language.JAVA)
                .teacher(teacher)
                .build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(teacherRepository.findById(newTeacherId)).thenReturn(Optional.of(teacher));

        //when //then

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.updateStudent(studentId, newTeacherId));
        assertEquals(exceptionMsg, exception.getMessage());
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