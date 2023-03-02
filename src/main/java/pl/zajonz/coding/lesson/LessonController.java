package pl.zajonz.coding.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajonz.coding.lesson.model.command.CreateLessonCommand;
import pl.zajonz.coding.lesson.model.dto.LessonDto;
import pl.zajonz.coding.teacher.TeacherService;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final TeacherService teacherService;

    @GetMapping
    public List<LessonDto> findAll() {
        return lessonService.findAllByDeletedFalse().stream()
                .map(LessonDto::fromEntity)
                .toList();
    }

    @PostMapping
    public LessonDto create(@RequestBody CreateLessonCommand command) {
        return LessonDto.fromEntity(lessonService.save(command));
    }

    // TODO: 02.03.2023 dokończyć funkcjonalności - zamiana na rest

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void deleteLesson(@PathVariable Integer id) {
        lessonService.deleteById(id);
    }

    @GetMapping("/edit/{id}")
    public String showLessonEditor(@PathVariable Integer id, Model model) {
        model.addAttribute("lesson", lessonService.findById(id));
        return "/lesson/edit";
    }

    @PatchMapping("/edit")
    @ResponseBody
    public void editLesson(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime term, @RequestParam Integer lessonId) {
        lessonService.updateLesson(term, lessonId);
    }
}
