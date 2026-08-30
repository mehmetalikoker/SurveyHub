package org.ing.surveyhub.repository;

import org.ing.surveyhub.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    /**
     * open-in-view=false olduğu için (bkz. application.yml) view render aşamasında
     * Hibernate session'ı kapalı olur; questions koleksiyonunu burada, sorgu içinde
     * fetch join ile önceden yüklemek gerekiyor. (question.options AYRICA
     * QuestionRepository.findWithOptionsBySurveyId ile yükleniyor — iki List'i
     * (bag) aynı sorguda fetch join etmek Hibernate'in MultipleBagFetchException'ına
     * takılıyor, bkz. SurveyService.getSurveyWithQuestions.)
     */
    @Query("select distinct s from Survey s left join fetch s.questions where s.id = :id")
    Optional<Survey> findByIdWithQuestions(@Param("id") Long id);
}
