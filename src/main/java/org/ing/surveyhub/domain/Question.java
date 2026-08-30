package org.ing.surveyhub.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Column(nullable = false, length = 1000)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @Column(nullable = false)
    private int displayOrder;

    @Column(nullable = false)
    private boolean required = true;

    private String section;

    // Çoktan Seçmeli
    private boolean allowMultipleSelection;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<QuestionOption> options = new ArrayList<>();

    // Açık Uçlu
    private Integer maxLength;

    // Likert
    private Integer likertScale;
    private String likertMinLabel;
    private String likertMaxLabel;

    // Derecelendirme
    private Integer minValue;
    private Integer maxValue;

    protected Question() {
        // JPA
    }

    public Question(String text, QuestionType type) {
        this.text = text;
        this.type = type;
    }

    public void addOption(QuestionOption option) {
        option.setQuestion(this);
        option.setDisplayOrder(options.size());
        options.add(option);
    }

    public Long getId() {
        return id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

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

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
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

    public List<QuestionOption> getOptions() {
        return options;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getLikertScale() {
        return likertScale;
    }

    public void setLikertScale(Integer likertScale) {
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
