package org.ing.surveyhub.repository;

import org.ing.surveyhub.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
