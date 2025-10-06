package com.library.khanhnqph57627.repository;

import com.library.khanhnqph57627.entity.PersonalSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface PersonalScheduleRepository extends JpaRepository<PersonalSchedule, Integer> {

    List<PersonalSchedule> findByUserId(Integer userId);

    List<PersonalSchedule> findByUserIdAndDayOfWeek(Integer userId, DayOfWeek dayOfWeek);

    List<PersonalSchedule> findByUserIdAndIsActiveTrue(Integer userId);

    @Query("SELECT ps FROM PersonalSchedule ps WHERE ps.user.id = :userId AND ps.dayOfWeek = :dayOfWeek AND ps.isActive = true ORDER BY ps.startTime")
    List<PersonalSchedule> findTodaySchedule(@Param("userId") Integer userId, @Param("dayOfWeek") DayOfWeek dayOfWeek);

    boolean existsByUserIdAndSubjectId(Integer userId, Integer subjectId);

    @Query("SELECT ps.subject.id FROM PersonalSchedule ps WHERE ps.user.id = :userId AND ps.isActive = true")
    List<Integer> findSubjectIdsByUserId(@Param("userId") Integer userId);
}