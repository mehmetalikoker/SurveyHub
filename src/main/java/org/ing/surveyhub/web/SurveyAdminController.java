package org.ing.surveyhub.web;

import org.ing.surveyhub.domain.Survey;
import org.ing.surveyhub.exception.SurveyValidationException;
import org.ing.surveyhub.service.SurveyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/surveys")
public class SurveyAdminController {

    private final SurveyService surveyService;

    public SurveyAdminController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/new")
    public String newSurvey() {
        return "admin/surveys/new";
    }

    @PostMapping("/new")
    public String createSurvey(@RequestParam String title,
                                @RequestParam(required = false) String description,
                                RedirectAttributes redirectAttributes) {
        try {
            Survey survey = surveyService.createDraftSurvey(title, description);
            return "redirect:/admin/surveys/" + survey.getId() + "/questions/new";
        } catch (SurveyValidationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/surveys/new";
        }
    }
}
