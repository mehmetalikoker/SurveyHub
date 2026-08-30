package org.ing.surveyhub.domain;

public enum QuestionType {

    MULTIPLE_CHOICE("Çoktan Seçmeli"),
    OPEN_ENDED("Açık Uçlu"),
    LIKERT("Likert"),
    RATING("Derecelendirme");

    private final String label;

    QuestionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
