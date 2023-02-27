package pl.zajonz.coding.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajonz.coding.common.Language;
import pl.zajonz.coding.student.model.Student;
import pl.zajonz.coding.student.model.StudentDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/list")
    public String getStudentList(Model model) {
        model.addAttribute("students", studentService.findAllByDeletedFalse());
        return "student/list";
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("student",new Student());
        model.addAttribute("languages", Language.values());
        return "student/add";
    }
    @PostMapping("/add")
    public String submitStudent(Student student) {
        studentService.save(student);
        return "redirect:/students/list";
    }
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void deleteStudent(@PathVariable Integer id) {
        studentService.deleteById(id);
    }

    @GetMapping("/edit/{id}")
    public String showStudentEditor(@PathVariable Integer id, Model model) {
        model.addAttribute("student", studentService.findById(id));
        return "/student/edit";
    }

    @PatchMapping("/edit")
    @ResponseBody
    public void editStudent(@RequestParam Integer teacherId, @RequestParam Integer studentId) {
        studentService.updateStudent(teacherId,studentId);
    }

    @GetMapping
    @ResponseBody
    public List<StudentDto> findAllByTeacher_IdAndDeletedFalse(@RequestParam int teacherId){
        return studentService.findAllByTeacher_IdAndDeletedFalse(teacherId)
                .stream()
                .map(StudentDto::fromEntity).toList();
    }
}
