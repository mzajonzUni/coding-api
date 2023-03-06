package pl.zajonz.coding.teacher;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.teacher.model.Teacher;
import pl.zajonz.coding.teacher.model.command.CreateTeacherCommand;
import pl.zajonz.coding.teacher.model.command.UpdateTeacherCommand;
import pl.zajonz.coding.teacher.model.command.UpdateTeacherLanguageCommand;
import pl.zajonz.coding.teacher.model.dto.TeacherDto;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public List<TeacherDto> findAll() {
        return teacherService.findAllByDeletedFalse().stream()
                .map(TeacherDto::fromEntity)
                .toList();
    }

    @GetMapping(params = "language")
    public List<TeacherDto> findAllByLanguage(@RequestParam Language language) {
        return teacherService.findAllByLanguagesContainingAndDeletedFalse(language).stream()
                .map(TeacherDto::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public TeacherDto findById(@PathVariable int id) {
        return TeacherDto.fromEntity(teacherService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeacherDto submitTeacher(@RequestBody @Valid CreateTeacherCommand command) {
        Teacher toSave = command.toEntity();
        return TeacherDto.fromEntity(teacherService.save(toSave));
    }

    // TODO: 27.02.2023 delete
    // TODO: 27.02.2023 update
    // TODO: 27.02.2023 partial update - updateLanguages

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        teacherService.deleteById(id);
    }

    @PutMapping("/{id}")
    public TeacherDto update(@PathVariable int id, @RequestBody @Valid UpdateTeacherCommand command) {
        return teacherService.update(command, id);
    }

    @PatchMapping("/{id}")
    public TeacherDto updateLanguages(@PathVariable int id, @RequestBody @Valid UpdateTeacherLanguageCommand command) {
        return teacherService.updateLanguages(command, id);
    }
}
