package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dto.RegionReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.IRegionService;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/teachers")         // base path
@RequiredArgsConstructor             // inject τα dependency ITeacherService & IRegionService
public class TeacherController {

    private final ITeacherService teacherService;
    private final IRegionService regionService;
//    private final TeacherInsertValidator teacherInsertValidator;

//    @Autowired             // το αποφεύγουμε λόγω @RequiredArgsConstructor
//    public TeacherController(ITeacherService teacherService, IRegionService regionService) {
//        this.teacherService = teacherService;
//        this.regionService = regionService;
//    }

    @GetMapping("/insert")
    public String getTeacherForm(Model model) {
        model.addAttribute("teacherInsertDTO", TeacherInsertDTO.empty());      // Όπως το έχουμε ονομάσει στο html file th:object  -  το empty έχει οριστεί με default τιμες στο dto ως μέθοδος
//        model.addAttribute("regionsReadOnlyDTO", regions());                   // φτιάχνουμε μέθοδο που επιστρέφει τη λίστα
        return "teacher-insert";       // html page
    }

    @PostMapping("/insert")            // έλεγχος από τον Controller
    public String teacherInsert(@Valid @ModelAttribute("teacherInsertDTO") TeacherInsertDTO teacherInsertDTO,
                                BindingResult bindingResult, Model model,
                                RedirectAttributes redirectAttributes) {

//        teacherInsertValidator.validate(teacherInsertDTO, bindingResult);

        // bean validation - syntax
        if (bindingResult.hasErrors()) {
//            model.addAttribute("regionsReadOnlyDTO", regions());    // γίνεται auto λόγω της @ModelAttribute
            return "teacher-insert";    // γίνεται populate από το DTO με data
        }

        try {
            // save τον teacher
            // επιστρέφει ένα success page
        } catch () {

        }
    }


    @ModelAttribute("regionsReadOnlyDTO")    // Εκτελείται πριν από κάθε request (get) handler
    public List<RegionReadOnlyDTO> region() {
//        return regionService.findAllRegionsSortedByName();

        // dummy data
        return List.of(
                new RegionReadOnlyDTO(1L, "Αθήνα"),
                new RegionReadOnlyDTO(2L, "Βόλος"),
                new RegionReadOnlyDTO(3L, "Θεσσαλονίκη")
        );
    }
}
