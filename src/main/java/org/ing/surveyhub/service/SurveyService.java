package org.ing.surveyhub.service;

import org.ing.surveyhub.domain.Question;
import org.ing.surveyhub.domain.QuestionOption;
import org.ing.surveyhub.domain.QuestionType;
import org.ing.surveyhub.domain.Survey;
import org.ing.surveyhub.domain.SurveyStatus;
import org.ing.surveyhub.exception.SurveyValidationException;
import org.ing.surveyhub.repository.SurveyRepository;
import org.ing.surveyhub.web.form.QuestionForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Anket/soru iş kuralları burada uygulanır — entity'ler sade veri taşıyıcı
 * (bkz. docs/PLAN-TR.md: "İş kuralı servis katmanında, şemada değil").
 */
@Service
public class SurveyService {

    private static final Set<Integer> ALLOWED_LIKERT_SCALES = Set.of(3, 5, 7);

    private final SurveyRepository surveyRepository;

    public SurveyService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Transactional
    public Survey createDraftSurvey(String title, String description) {
        Survey survey = new Survey(requireText(title, "Anket başlığı zorunlu."), blankToNull(description));
        return surveyRepository.save(survey);
    }

    @Transactional
    public Question addQuestion(Long surveyId, QuestionForm form) {
        Survey survey = getEditableSurvey(surveyId);

        QuestionType type = form.getType();
        if (type == null) {
            throw new SurveyValidationException("Soru tipi seçilmeli.");
        }

        Question question = new Question(requireText(form.getText(), "Soru metni boş olamaz."), type);
        question.setRequired(form.isRequired());
        question.setSection(blankToNull(form.getSection()));

        switch (type) {
            case MULTIPLE_CHOICE -> applyMultipleChoice(question, form);
            case OPEN_ENDED -> applyOpenEnded(question, form);
            case LIKERT -> applyLikert(question, form);
            case RATING -> applyRating(question, form);
        }

        survey.addQuestion(question);
        survey.setUpdatedAt(Instant.now());
        surveyRepository.save(survey);
        return question;
    }

    @Transactional
    public Survey publish(Long surveyId) {
        Survey survey = getEditableSurvey(surveyId);
        if (survey.getQuestions().isEmpty()) {
            throw new SurveyValidationException("Anketi yayınlamak için en az 1 soru eklemelisiniz.");
        }
        survey.setStatus(SurveyStatus.PUBLISHED);
        survey.setPublishedAt(Instant.now());
        return surveyRepository.save(survey);
    }

    private Survey getEditableSurvey(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new SurveyValidationException("Anket bulunamadı: " + surveyId));
        if (survey.getStatus() != SurveyStatus.DRAFT) {
            throw new SurveyValidationException("Sadece taslak (DRAFT) anketler düzenlenebilir.");
        }
        return survey;
    }

    private void applyMultipleChoice(Question question, QuestionForm form) {
        List<String> options = new ArrayList<>();
        if (form.getOptions() != null) {
            for (String opt : form.getOptions()) {
                if (opt != null && !opt.isBlank()) {
                    options.add(opt.trim());
                }
            }
        }
        if (options.size() < 2) {
            throw new SurveyValidationException("Çoktan seçmeli soru için en az 2 seçenek girmelisiniz.");
        }
        question.setAllowMultipleSelection(form.isAllowMultipleSelection());
        for (String opt : options) {
            question.addOption(new QuestionOption(opt));
        }
    }

    private void applyOpenEnded(Question question, QuestionForm form) {
        Integer maxLength = form.getMaxLength();
        if (maxLength != null && maxLength <= 0) {
            throw new SurveyValidationException("Maksimum karakter sayısı pozitif olmalı.");
        }
        question.setMaxLength(maxLength);
    }

    private void applyLikert(Question question, QuestionForm form) {
        if (!ALLOWED_LIKERT_SCALES.contains(form.getLikertScale())) {
            throw new SurveyValidationException("Likert ölçeği 3, 5 veya 7 olmalı.");
        }
        question.setLikertScale(form.getLikertScale());
        question.setLikertMinLabel(requireText(form.getLikertMinLabel(), "Likert en düşük uç etiketi boş olamaz."));
        question.setLikertMaxLabel(requireText(form.getLikertMaxLabel(), "Likert en yüksek uç etiketi boş olamaz."));
    }

    private void applyRating(Question question, QuestionForm form) {
        Integer min = form.getMinValue();
        Integer max = form.getMaxValue();
        if (min == null || max == null || min >= max) {
            throw new SurveyValidationException("Derecelendirme için min. değer maks. değerden küçük olmalı.");
        }
        question.setMinValue(min);
        question.setMaxValue(max);
    }

    private static String requireText(String value, String errorMessage) {
        if (value == null || value.isBlank()) {
            throw new SurveyValidationException(errorMessage);
        }
        return value.trim();
    }

    private static String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
