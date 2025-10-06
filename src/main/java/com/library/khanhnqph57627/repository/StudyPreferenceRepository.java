package com.library.khanhnqph57627.repository;

import com.library.khanhnqph57627.entity.StudyPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudyPreferenceRepository extends JpaRepository<StudyPreference, Integer> {

    Optional<StudyPreference> findByUserId(Integer userId);

    boolean existsByUserId(Integer userId);
}