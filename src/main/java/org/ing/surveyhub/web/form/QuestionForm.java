package org.ing.surveyhub.web.form;

import org.ing.surveyhub.domain.QuestionType;

/**
 * Soru oluşturma/düzenleme formu için bean. Entity DEĞİL — sadece view binding amaçlı.
 */
public class QuestionForm {

    private String text;
    private QuestionType type = QuestionType.MULTIPLE_CHOICE;
    private boolean required = true;
    private String section;

    // Çoktan Seçmeli
    private boolean allowMultipleSelection;
    private String[] options = new String[] { "", "" };

    // Açık Uçlu
    private Integer maxLength;

    // Likert
    private int likertScale = 5;
    private String likertMinLabel = "Kesinlikle Katılmıyorum";
    private String likertMaxLabel = "Kesinlikle Katılıyorum";

    // Derecelendirme
    private Integer minValue = 1;
    private Integer maxValue = 5;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public boolean isAllowMultipleSelection() {
        return allowMultipleSelection;
    }

    public void setAllowMultipleSelection(boolean allowMultipleSelection) {
        this.allowMultipleSelection = allowMultipleSelection;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public int getLikertScale() {
        return likertScale;
    }

    public void setLikertScale(int likertScale) {
        this.likertScale = likertScale;
    }

    public String getLikertMinLabel() {
        return likertMinLabel;
    }

    public void setLikertMinLabel(String likertMinLabel) {
        this.likertMinLabel = likertMinLabel;
    }

    public String getLikertMaxLabel() {
        return likertMaxLabel;
    }

    public void setLikertMaxLabel(String likertMaxLabel) {
        this.likertMaxLabel = likertMaxLabel;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }
}
