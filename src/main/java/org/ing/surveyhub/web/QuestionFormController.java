package org.ing.surveyhub.web;

import org.ing.surveyhub.domain.QuestionType;
import org.ing.surveyhub.web.form.QuestionForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Sadece frontend tasarımını önizlemek için. Kayıt/persistence yok —
 * backend (entity/repository/service) ayrı olarak eklenecek.
 */
@Controller
public class QuestionFormController {

    @GetMapping("/admin/questions/new")
    public String newQuestion(Model model) {
        model.addAttribute("questionForm", new QuestionForm());
        model.addAttribute("questionTypes", QuestionType.values());
        return "admin/questions/form";
    }

    @PostMapping("/admin/questions/new")
    public String previewSubmit(@ModelAttribute("questionForm") QuestionForm form, Model model) {
        model.addAttribute("questionTypes", QuestionType.values());
        model.addAttribute("submitted", true);
        return "admin/questions/form";
    }
}
