package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.*;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.IRegionService;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import gr.aueb.cf.schoolapp.validator.TeacherInsertValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/teachers")         // base path
@RequiredArgsConstructor             // inject τα dependency ITeacherService & IRegionService
public class TeacherController {

    private final ITeacherService teacherService;
    private final IRegionService regionService;
    private final TeacherInsertValidator teacherInsertValidator;

//    @Autowired             // το αποφεύγουμε λόγω @RequiredArgsConstructor
//    public TeacherController(ITeacherService teacherService, IRegionService regionService) {
//        this.teacherService = teacherService;
//        this.regionService = regionService;
//    }

    @GetMapping("/insert")
    public String getTeacherForm(Model model) {
        model.addAttribute("teacherInsertDTO", TeacherInsertDTO.empty());      // Όπως το έχουμε ονομάσει στο html file th:object  -  το empty έχει οριστεί με default τιμες στο dto ως μέθοδος
//        model.addAttribute("regionsReadOnlyDTO", regions());                 // δεν χρειάζεται γιατι εχουμε παρακατω @ModelAttribute
        return "teacher-insert";       // html page
    }

    @PostMapping("/insert")            // έλεγχος από τον Controller - bean validation - syntax
    public String teacherInsert(@Valid @ModelAttribute("teacherInsertDTO") TeacherInsertDTO teacherInsertDTO,
                                BindingResult bindingResult, Model model,
                                RedirectAttributes redirectAttributes) {

        teacherInsertValidator.validate(teacherInsertDTO, bindingResult);    // business rules

        if (bindingResult.hasErrors()) {
//            model.addAttribute("regionsReadOnlyDTO", regions());    // γίνεται auto λόγω της @ModelAttribute
            return "teacher-insert";    // γίνεται populate από το DTO με data
        }

        try {
            // save τον teacher
            TeacherReadOnlyDTO teacherReadOnlyDTO = teacherService.saveTeacher(teacherInsertDTO);

            // επιστρέφει ένα success page

            // PRG - Post-Redirect-Get (http code 302) - Προστασία από Refresh & Insert x2
            redirectAttributes.addFlashAttribute("teacherReadOnlyDTO", teacherReadOnlyDTO);       // για να υπάρχουν ξανά τα data
            return "redirect:/teachers/success";                      // get controller success page
        } catch (EntityAlreadyExistsException | EntityInvalidArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());        // th:if="${errorMessage}"  - teacher-insert
            return "teacher-insert";
        }
    }

    @GetMapping("/success")
    public String teacherInsertSuccess(Model model) {
        if (!model.containsAttribute("teacherReadOnlyDTO")) {       // ελέγχει το F5
            return "redirect:/teachers";
        }
        return "teacher-success";
    }

    @GetMapping({"", "/"})
    public String getPaginatedTeachersDeletedFalse(@PageableDefault(page = 0, size = 5, sort = "lastname") Pageable pageable,
                                                   Model model) {
        Page<TeacherReadOnlyDTO> teachersPage = teacherService.getPaginatedTeachersDeletedFalse(pageable);
        model.addAttribute("teachers", teachersPage.getContent());          // API του Page για να φέρνει τα data LIST
        model.addAttribute("page", teachersPage);
        return "teachers";                        // html page
    }

    @GetMapping("/edit/{uuid}")                   // for specific uuid & θέλει @PathVariable - ή ως query params μπορούμε
    public String getTeacherEdit(@PathVariable UUID uuid, Model model) throws EntityNotFoundException {
        try {
            TeacherEditDTO teacherEditDTO = teacherService.getTeacherByUUIDDeletedFalse(uuid);
            model.addAttribute("teacherEditDTO", teacherEditDTO);
        } catch(EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "teacher-edit";
    }

    @PostMapping("/edit")
    public String updateTeacher(@Valid @ModelAttribute TeacherEditDTO teacherEditDTO,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                Model model) {


    }


    @ModelAttribute("regionsReadOnlyDTO")         // Εκτελείται πριν από κάθε request (get) handler
    public List<RegionReadOnlyDTO> region() {
        return regionService.findAllRegionsSortedByName();

//        // dummy data
//        return List.of(
//                new RegionReadOnlyDTO(1L, "Αθήνα"),
//                new RegionReadOnlyDTO(2L, "Βόλος"),
//                new RegionReadOnlyDTO(3L, "Θεσσαλονίκη")
//        );
    }
}
