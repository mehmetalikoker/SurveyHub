package org.ing.surveyhub.exception;

/**
 * Anket/soru iş kuralları ihlal edildiğinde fırlatılır (örn. başlıksız anket,
 * en az 1 sorusu olmayan anketi yayınlama, geçersiz soru tipi ayarları).
 */
public class SurveyValidationException extends RuntimeException {

    public SurveyValidationException(String message) {
        super(message);
    }
}
