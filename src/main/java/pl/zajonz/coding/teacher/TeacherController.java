package pl.zajonz.coding.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.teacher.model.Teacher;
import pl.zajonz.coding.teacher.model.TeacherDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/list")
    public String getTeacherList(Model model) {
        model.addAttribute("teachers", teacherService.findAllByDeletedFalse());
        return "teacher/list";
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("languages", Language.values());
        return "teacher/add";
    }

    @PostMapping("/add")
    public String submitTeacher(Teacher teacher) {
        teacherService.save(teacher);
        return "redirect:/teachers/list";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void deleteTeacher(@PathVariable Integer id) {
        teacherService.deleteById(id);
    }

    @GetMapping(params = "language")
    @ResponseBody
    public List<TeacherDto> findAllByLanguage(@RequestParam Language language) {
        return teacherService.findAllByLanguagesContainingAndDeletedFalse(language).stream()
                .map(TeacherDto::fromEntity)
                .toList();
    }
}
