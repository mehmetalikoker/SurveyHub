package org.ing.surveyhub.web;

import jakarta.servlet.http.HttpSession;
import org.ing.surveyhub.domain.QuestionType;
import org.ing.surveyhub.web.form.QuestionForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Sadece frontend akışını (soru ekle / önizle / anketi bitir) tasarlamak için.
 * Taslak sorular oturumda (HttpSession) tutulur — gerçek persistence (entity/repository/servis)
 * ayrı olarak eklenecek, bu controller o zaman değişecek.
 */
@Controller
@RequestMapping("/admin/questions")
public class QuestionFormController {

    private static final String DRAFT_KEY = "draftQuestions";
    private static final String FINISHED_KEY = "surveyFinished";

    @GetMapping("/new")
    public String newQuestion(Model model, HttpSession session) {
        model.addAttribute("questionForm", new QuestionForm());
        model.addAttribute("questionTypes", QuestionType.values());
        model.addAttribute("draftQuestions", draftList(session));
        model.addAttribute("surveyFinished", isFinished(session));
        return "admin/questions/form";
    }

    @PostMapping("/preview")
    public String preview(@ModelAttribute("questionForm") QuestionForm form, Model model, HttpSession session) {
        model.addAttribute("questionTypes", QuestionType.values());
        model.addAttribute("draftQuestions", draftList(session));
        model.addAttribute("surveyFinished", isFinished(session));
        model.addAttribute("previewed", true);
        return "admin/questions/form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("questionForm") QuestionForm form, HttpSession session) {
        draftList(session).add(form);
        return "redirect:/admin/questions/new";
    }

    @PostMapping("/finish")
    public String finish(HttpSession session, RedirectAttributes redirectAttributes) {
        if (draftList(session).isEmpty()) {
            redirectAttributes.addFlashAttribute("finishError", "Anketi bitirmek için en az 1 soru eklemelisiniz.");
        } else {
            session.setAttribute(FINISHED_KEY, Boolean.TRUE);
        }
        return "redirect:/admin/questions/new";
    }

    @PostMapping("/reset")
    public String reset(HttpSession session) {
        session.removeAttribute(DRAFT_KEY);
        session.removeAttribute(FINISHED_KEY);
        return "redirect:/admin/questions/new";
    }

    @SuppressWarnings("unchecked")
    private List<QuestionForm> draftList(HttpSession session) {
        List<QuestionForm> list = (List<QuestionForm>) session.getAttribute(DRAFT_KEY);
        if (list == null) {
            list = new ArrayList<>();
            session.setAttribute(DRAFT_KEY, list);
        }
        return list;
    }

    private boolean isFinished(HttpSession session) {
        Boolean finished = (Boolean) session.getAttribute(FINISHED_KEY);
        return Boolean.TRUE.equals(finished);
    }
}
