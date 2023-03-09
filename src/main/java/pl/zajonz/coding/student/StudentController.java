package pl.zajonz.coding.student;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.student.model.command.CreateStudentCommand;
import pl.zajonz.coding.student.model.command.UpdateStudentCommand;
import pl.zajonz.coding.student.model.dto.StudentDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<StudentDto> findAll() {
        return studentService.findAllByDeletedFalse().stream()
                .map(StudentDto::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public StudentDto findById(@PathVariable int id) {
        return StudentDto.fromEntity(studentService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentDto create(@RequestBody @Valid CreateStudentCommand command) {
        return StudentDto.fromEntity(studentService.save(command));
    }

    @PutMapping("/{id}")
    public StudentDto update(@PathVariable int id, @RequestBody @Valid UpdateStudentCommand command) {
        Student toUpdate = command.toEntity();
        return StudentDto.fromEntity(studentService.update(id, toUpdate));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        studentService.deleteById(id);
    }
}
