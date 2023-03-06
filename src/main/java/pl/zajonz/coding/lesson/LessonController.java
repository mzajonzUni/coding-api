package pl.zajonz.coding.lesson;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.zajonz.coding.lesson.model.Lesson;
import pl.zajonz.coding.lesson.model.command.CreateLessonCommand;
import pl.zajonz.coding.lesson.model.command.UpdateLessonCommand;
import pl.zajonz.coding.lesson.model.dto.LessonDto;

import java.time.LocalDateTime;
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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public LessonDto update(@PathVariable Integer id, @RequestBody @Valid UpdateLessonCommand command) {
        return LessonDto.fromEntity(lessonService.update(id, command));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public LessonDto updateDate(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime term) {
        return LessonDto.fromEntity(lessonService.updateLesson(term, id));
    }
}
