package org.ing.surveyhub.repository;

import org.ing.surveyhub.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select distinct q from Question q left join fetch q.options where q.survey.id = :surveyId")
    List<Question> findWithOptionsBySurveyId(@Param("surveyId") Long surveyId);
}
