package pl.zajonz.coding.lesson;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.zajonz.coding.lesson.model.Lesson;
import pl.zajonz.coding.lesson.model.command.CreateLessonCommand;
import pl.zajonz.coding.lesson.model.command.UpdateLessonTermCommand;
import pl.zajonz.coding.lesson.model.dto.LessonDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lessons")
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public List<LessonDto> findAll() {
        return lessonService.findAllByDeletedFalse().stream()
                .map(LessonDto::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public LessonDto findById(@PathVariable int id) {
        return LessonDto.fromEntity(lessonService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LessonDto create(@RequestBody @Valid CreateLessonCommand command) {
        return LessonDto.fromEntity(lessonService.save(command));
    }
    // TODO: 02.03.2023 dokończyć funkcjonalności - zamiana na rest


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLesson(@PathVariable Integer id) {
        lessonService.deleteById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public LessonDto updateTerm(@PathVariable Integer id, @RequestBody @Valid UpdateLessonTermCommand command) {
        Lesson toUpdate = command.toEntity();
        return LessonDto.fromEntity(lessonService.updateTerm(id, toUpdate));
    }

}
