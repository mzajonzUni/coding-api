package pl.zajonz.coding.lesson;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajonz.coding.common.exception.InvalidDateException;
import pl.zajonz.coding.lesson.model.Lesson;
import pl.zajonz.coding.lesson.model.command.CreateLessonCommand;
import pl.zajonz.coding.lesson.model.command.UpdateLessonTermCommand;
import pl.zajonz.coding.student.StudentRepository;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.teacher.TeacherRepository;
import pl.zajonz.coding.teacher.model.Teacher;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @InjectMocks
    private LessonService lessonService;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private StudentRepository studentRepository;

    @Test
    void testFindAllByDeletedFalse_ResultsInLessonListBeingReturned() {
        //given
        Teacher teacher = Teacher.builder()
                .firstName("TestTeacher")
                .build();
        Student student = Student.builder()
                .firstName("TestStudent")
                .build();
        Lesson lesson = Lesson.builder()
                .student(student)
                .teacher(teacher)
                .build();
        List<Lesson> resultsFromRepo = List.of(lesson);

        when(lessonRepository.findAllByDeletedFalse()).thenReturn(resultsFromRepo);
        //when
        List<Lesson> returned = lessonService.findAllByDeletedFalse();

        //then
        assertEquals(resultsFromRepo, returned);
    }

    @Test
    void testSave_CorrectValues_ResultsInLessonBeingSaved() {
        //given
        LocalDateTime date = LocalDateTime.now().plusDays(3);
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("TestTeacher")
                .build();
        Student student = Student.builder()
                .id(1)
                .firstName("TestStudent")
                .build();
        Lesson lesson = Lesson.builder()
                .id(1)
                .student(student)
                .teacher(teacher)
                .term(date)
                .build();
        CreateLessonCommand command = new CreateLessonCommand();
        command.setTeacherId(teacher.getId());
        command.setStudentId(student.getId());
        command.setTerm(date);

        when(lessonRepository.existsByTeacherIdAndTermBetween(anyInt(),any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(false);
        when(teacherRepository.findById(anyInt())).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(anyInt())).thenReturn(Optional.of(student));
        //when
        lessonService.save(command);

        //then
        verify(lessonRepository).save(any(Lesson.class));
    }

//    @Test
//    void testSave_TermInPast_ResultsInLessonBeingSaved() {
//        //given
//        LocalDateTime termBefore = LocalDateTime.now().minusDays(10);
//        String exceptionMsg = "Invalid date " + termBefore;
//        Teacher teacher = Teacher.builder()
//                .firstName("TestTeacher")
//                .build();
//        Student student = Student.builder()
//                .firstName("TestStudent")
//                .build();
//        Lesson lesson = Lesson.builder()
//                .student(student)
//                .teacher(teacher)
//                .term(termBefore)
//                .build();
//
//        //when //then
//        InvalidDateException exception = assertThrows(
//                InvalidDateException.class,
//                () -> lessonService.save(lesson));
//        assertEquals(exceptionMsg, exception.getMessage());
//    }

//    @Test
//    void testSave_TermOccupied_ResultsInInvalidDateException() {
//        //given
//        LocalDateTime termOccupied = LocalDateTime.now().plusDays(10);
//        String exceptionMsg = "Invalid date " + termOccupied;
//        Teacher teacher = Teacher.builder()
//                .firstName("TestTeacher")
//                .build();
//        Student student = Student.builder()
//                .firstName("TestStudent")
//                .build();
//        Lesson lesson = Lesson.builder()
//                .student(student)
//                .teacher(teacher)
//                .term(termOccupied)
//                .build();
//
//        when(lessonRepository.existsByTeacherIdAndTermBetween(
//                teacher.getId(), lesson.getTerm().minusMinutes(59),
//                lesson.getTerm().plusMinutes(59))).thenReturn(true);
//        //when //then
//        InvalidDateException exception = assertThrows(
//                InvalidDateException.class,
//                () -> lessonService.save(any()));
//        assertEquals(exceptionMsg, exception.getMessage());
//    }

    @Test
    void testDeleteById_CorrectId_ResultsInLessonBeingDeleted() {
        //given
        int lessonId = 1;
        Teacher teacher = Teacher.builder()
                .firstName("TestTeacher")
                .build();
        Student student = Student.builder()
                .firstName("TestStudent")
                .build();
        Lesson lesson = Lesson.builder()
                .student(student)
                .teacher(teacher)
                .term(LocalDateTime.now().plusDays(10))
                .build();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        //when
        lessonService.deleteById(lessonId);
        //then
        verify(lessonRepository).deleteById(lessonId);
    }

    @Test
    void testDeleteById_IncorrectId_ResultsInNoSuchElementException() {
        //given
        int lessonId = 1;
        String exceptionMsg = MessageFormat
                .format("Lesson with id={0} has not been found", lessonId);

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());
        //when  //then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> lessonService.deleteById(lessonId));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testDeleteById_TermInPast_ResultsInInvalidDateException() {
        //given
        int lessonId = 1;
        String exceptionMsg = "The term is in the past";
        Teacher teacher = Teacher.builder()
                .firstName("TestTeacher")
                .build();
        Student student = Student.builder()
                .firstName("TestStudent")
                .build();
        Lesson lesson = Lesson.builder()
                .student(student)
                .teacher(teacher)
                .term(LocalDateTime.now().minusDays(10))
                .build();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        //when  //then
        InvalidDateException exception = assertThrows(
                InvalidDateException.class,
                () -> lessonService.deleteById(lessonId));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testUpdateLesson_CorrectValues_ResultsInLessonBeingUpdated() {
        //given
        int lessonId = 1;
        LocalDateTime date = LocalDateTime.now().plusDays(10);
        LocalDateTime newDate = LocalDateTime.now().plusDays(8);
        Teacher teacher = Teacher.builder()
                .firstName("TestTeacher")
                .build();
        Student student = Student.builder()
                .firstName("TestStudent")
                .build();
        Lesson lesson = Lesson.builder()
                .student(student)
                .teacher(teacher)
                .term(date)
                .build();
        Lesson lessonToUpdate = Lesson.builder()
                .term(newDate)
                .build();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        //when
        lessonService.updateTerm(lessonId, lessonToUpdate);
        //then
        assertEquals(newDate, lesson.getTerm());
        verify(lessonRepository).save(lesson);
    }

    @Test
    void testUpdateLesson_InCorrectLessonId_ResultsInNoSuchElementException() {
        //given
        int lessonId = 1;
        LocalDateTime date = LocalDateTime.now().plusDays(10);
        Lesson lessonToUpdate = Lesson.builder()
                .term(date)
                .build();
        String exceptionMsg = MessageFormat
                .format("Lesson with id={0} has not been found", lessonId);

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());
        //when //then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> lessonService.updateTerm(lessonId, lessonToUpdate));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testUpdateLesson_TermInPast_ResultsInInvalidDateException() {
        //given
        int lessonId = 1;
        LocalDateTime date = LocalDateTime.now().minusDays(10);
        Lesson lessonToUpdate = Lesson.builder()
                .term(date)
                .build();
        String exceptionMsg = "Invalid date " + date;
        Teacher teacher = Teacher.builder()
                .firstName("TestTeacher")
                .build();
        Student student = Student.builder()
                .firstName("TestStudent")
                .build();
        Lesson lesson = Lesson.builder()
                .student(student)
                .teacher(teacher)
                .term(date)
                .build();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        //when //then
        InvalidDateException exception = assertThrows(
                InvalidDateException.class,
                () -> lessonService.updateTerm(lessonId, lessonToUpdate));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testUpdateLesson_TermOccupied_ResultsInInvalidDateException() {
        //given
        int lessonId = 1;
        LocalDateTime dateOccupied = LocalDateTime.now().plusDays(10);
        Lesson lessonToUpdate = Lesson.builder()
                .term(dateOccupied)
                .build();
        String exceptionMsg = "Invalid date " + dateOccupied;
        Teacher teacher = Teacher.builder()
                .firstName("TestTeacher")
                .build();
        Student student = Student.builder()
                .firstName("TestStudent")
                .build();
        Lesson lesson = Lesson.builder()
                .student(student)
                .teacher(teacher)
                .term(dateOccupied)
                .build();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(lessonRepository.existsByTeacherIdAndTermBetween(
                teacher.getId(), lesson.getTerm().minusMinutes(59),
                lesson.getTerm().plusMinutes(59))).thenReturn(true);
        //when //then
        InvalidDateException exception = assertThrows(
                InvalidDateException.class,
                () -> lessonService.updateTerm(lessonId, lessonToUpdate));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testFindById_CorrectId_ResultsInStudentBeingReturned() {
        int lessonId = 1;
        Teacher teacher = Teacher.builder()
                .firstName("TestTeacher")
                .build();
        Student student = Student.builder()
                .firstName("TestStudent")
                .build();
        Lesson lesson = Lesson.builder()
                .student(student)
                .teacher(teacher)
                .build();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        //then
        Lesson returned = lessonService.findById(lessonId);

        //when
        assertEquals(lesson, returned);
    }

    @Test
    void testFindById_IncorrectId_ResultsInNoSuchElementException() {
        //given
        int lessonId = 1;
        String exceptionMsg = "No such lesson with Id" + lessonId;
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());
        //then //when
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> lessonService.findById(lessonId));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testCheckDate_CorrectValues_ResultTrueBeingReturned() {
        //given
        LocalDateTime date = LocalDateTime.now().plusDays(10);
        int teacherId = 1;
        when(lessonRepository.existsByTeacherIdAndTermBetween(
                teacherId, date.minusMinutes(59),
                date.plusMinutes(59))).thenReturn(false);
        //when
        Boolean returned = lessonService.checkDate(date, teacherId);
        //then
        assertEquals(true, returned);
    }

    @Test
    void testCheckDate_DateInPast_ResultFalseBeingReturned() {
        //given
        LocalDateTime date = LocalDateTime.now().minusDays(10);
        int teacherId = 1;

        //when
        Boolean returned = lessonService.checkDate(date, teacherId);
        //then
        assertEquals(false, returned);
    }

    @Test
    void testCheckDate_DateOccupied_ResultFalseBeingReturned() {
        //given
        LocalDateTime date = LocalDateTime.now().plusDays(10);
        int teacherId = 1;

        when(lessonRepository.existsByTeacherIdAndTermBetween(
                teacherId, date.minusMinutes(59),
                date.plusMinutes(59))).thenReturn(true);
        //when
        Boolean returned = lessonService.checkDate(date, teacherId);
        //then
        assertEquals(false, returned);
    }
}