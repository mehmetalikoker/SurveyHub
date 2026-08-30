package org.ing.surveyhub.domain;

public enum SurveyStatus {

    DRAFT("Taslak"),
    PUBLISHED("Yayınlandı"),
    CLOSED("Kapatıldı");

    private final String label;

    SurveyStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
