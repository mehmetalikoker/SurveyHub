package org.ing.surveyhub.web;

import org.ing.surveyhub.domain.QuestionType;
import org.ing.surveyhub.domain.Survey;
import org.ing.surveyhub.exception.SurveyValidationException;
import org.ing.surveyhub.service.SurveyService;
import org.ing.surveyhub.web.form.QuestionForm;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Bir anketin (Survey) sorularını ekleyen admin arayüzü. Artık gerçek kayıt yapıyor:
 * "Soru Ekle" her tıklamada SurveyService üzerinden veritabanına yazıyor, "Anketi Bitir"
 * anketi PUBLISHED yapıp düzenlemeyi kapatıyor (SurveyService.getEditableSurvey kuralı).
 */
@Controller
@RequestMapping("/admin/surveys/{surveyId}/questions")
public class QuestionFormController {

    private final SurveyService surveyService;

    public QuestionFormController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/new")
    public String newQuestion(@PathVariable Long surveyId, Model model) {
        model.addAttribute("survey", findSurveyOr404(surveyId));
        model.addAttribute("questionForm", new QuestionForm());
        model.addAttribute("questionTypes", QuestionType.values());
        return "admin/questions/form";
    }

    @PostMapping("/preview")
    public String preview(@PathVariable Long surveyId,
                           @ModelAttribute("questionForm") QuestionForm form,
                           Model model) {
        model.addAttribute("survey", findSurveyOr404(surveyId));
        model.addAttribute("questionTypes", QuestionType.values());
        model.addAttribute("previewed", true);
        return "admin/questions/form";
    }

    @PostMapping("/add")
    public String add(@PathVariable Long surveyId,
                       @ModelAttribute("questionForm") QuestionForm form,
                       RedirectAttributes redirectAttributes) {
        try {
            surveyService.addQuestion(surveyId, form);
        } catch (SurveyValidationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/surveys/" + surveyId + "/questions/new";
    }

    @PostMapping("/finish")
    public String finish(@PathVariable Long surveyId, RedirectAttributes redirectAttributes) {
        try {
            surveyService.publish(surveyId);
        } catch (SurveyValidationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/surveys/" + surveyId + "/questions/new";
    }

    private Survey findSurveyOr404(Long surveyId) {
        try {
            return surveyService.getSurveyWithQuestions(surveyId);
        } catch (SurveyValidationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
