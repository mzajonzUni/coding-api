package pl.zajonz.coding.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajonz.coding.lesson.model.Lesson;
import pl.zajonz.coding.teacher.TeacherService;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final TeacherService teacherService;

    @GetMapping("/list")
    public String getLessonList(Model model) {
        model.addAttribute("lessons", lessonService.findAllByDeletedFalse());
        return "lesson/list";
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("teachers", teacherService.findAllByDeletedFalse());
        return "lesson/add";
    }

    @PostMapping("/add")
    public String submitLesson(Lesson lesson) {
        lessonService.save(lesson);
        return "redirect:/lessons/list";
    }

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

    @GetMapping
    @ResponseBody
    public boolean checkDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime term,@RequestParam Integer teacherId) {
        return lessonService.checkDate(term,teacherId);
    }

}
