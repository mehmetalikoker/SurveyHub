package org.ing.surveyhub.service;

import org.ing.surveyhub.domain.QuestionType;
import org.ing.surveyhub.domain.Survey;
import org.ing.surveyhub.domain.SurveyStatus;
import org.ing.surveyhub.exception.SurveyValidationException;
import org.ing.surveyhub.repository.SurveyRepository;
import org.ing.surveyhub.web.form.QuestionForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class SurveyServiceTest {

    @Autowired
    private SurveyRepository surveyRepository;

    private SurveyService service() {
        return new SurveyService(surveyRepository);
    }

    @Test
    void addsQuestionsOfEachTypeAndPublishes() {
        SurveyService service = service();
        Survey survey = service.createDraftSurvey("Müşteri Memnuniyeti", null);

        QuestionForm rating = new QuestionForm();
        rating.setText("Hizmetten memnun musunuz?");
        rating.setType(QuestionType.RATING);
        rating.setMinValue(1);
        rating.setMaxValue(5);
        service.addQuestion(survey.getId(), rating);

        QuestionForm choice = new QuestionForm();
        choice.setText("Hangi kanalı kullanıyorsunuz?");
        choice.setType(QuestionType.MULTIPLE_CHOICE);
        choice.setOptions(new String[] { "Mobil", "Web", "" });
        service.addQuestion(survey.getId(), choice);

        QuestionForm likert = new QuestionForm();
        likert.setText("Tavsiye eder misiniz?");
        likert.setType(QuestionType.LIKERT);
        likert.setLikertScale(5);
        likert.setLikertMinLabel("Kesinlikle Katılmıyorum");
        likert.setLikertMaxLabel("Kesinlikle Katılıyorum");
        service.addQuestion(survey.getId(), likert);

        QuestionForm openEnded = new QuestionForm();
        openEnded.setText("Eklemek istediğiniz bir şey var mı?");
        openEnded.setType(QuestionType.OPEN_ENDED);
        service.addQuestion(survey.getId(), openEnded);

        Survey published = service.publish(survey.getId());

        assertThat(published.getStatus()).isEqualTo(SurveyStatus.PUBLISHED);
        assertThat(published.getPublishedAt()).isNotNull();
        assertThat(published.getQuestions()).hasSize(4);
        assertThat(published.getQuestions().get(1).getOptions()).extracting("text")
                .containsExactly("Mobil", "Web");
    }

    @Test
    void rejectsPublishWithoutQuestions() {
        SurveyService service = service();
        Survey survey = service.createDraftSurvey("Boş Anket", null);

        assertThatThrownBy(() -> service.publish(survey.getId()))
                .isInstanceOf(SurveyValidationException.class);
    }

    @Test
    void rejectsMultipleChoiceWithFewerThanTwoOptions() {
        SurveyService service = service();
        Survey survey = service.createDraftSurvey("Test", null);

        QuestionForm form = new QuestionForm();
        form.setText("Soru");
        form.setType(QuestionType.MULTIPLE_CHOICE);
        form.setOptions(new String[] { "Tek" });

        assertThatThrownBy(() -> service.addQuestion(survey.getId(), form))
                .isInstanceOf(SurveyValidationException.class);
    }

    @Test
    void rejectsInvalidLikertScale() {
        SurveyService service = service();
        Survey survey = service.createDraftSurvey("Test", null);

        QuestionForm form = new QuestionForm();
        form.setText("Soru");
        form.setType(QuestionType.LIKERT);
        form.setLikertScale(4);

        assertThatThrownBy(() -> service.addQuestion(survey.getId(), form))
                .isInstanceOf(SurveyValidationException.class);
    }

    @Test
    void rejectsEditingAfterPublish() {
        SurveyService service = service();
        Survey survey = service.createDraftSurvey("Test", null);

        QuestionForm rating = new QuestionForm();
        rating.setText("Soru");
        rating.setType(QuestionType.RATING);
        rating.setMinValue(1);
        rating.setMaxValue(5);
        service.addQuestion(survey.getId(), rating);
        service.publish(survey.getId());

        QuestionForm anotherRating = new QuestionForm();
        anotherRating.setText("Başka soru");
        anotherRating.setType(QuestionType.RATING);
        anotherRating.setMinValue(1);
        anotherRating.setMaxValue(5);

        assertThatThrownBy(() -> service.addQuestion(survey.getId(), anotherRating))
                .isInstanceOf(SurveyValidationException.class);
    }
}
