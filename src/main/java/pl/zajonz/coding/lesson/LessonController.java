package pl.zajonz.coding.lesson;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public LessonDto create(@RequestBody @Valid CreateLessonCommand command) {
        return LessonDto.fromEntity(lessonService.save(command));
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public void update(@PathVariable int id, @RequestBody @Valid UpdateLessonTermCommand command) {
        lessonService.updateLesson(command, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        lessonService.deleteById(id);
    }
}
