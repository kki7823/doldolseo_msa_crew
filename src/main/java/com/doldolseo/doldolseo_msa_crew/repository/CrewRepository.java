package com.doldolseo.doldolseo_msa_crew.repository;

import com.doldolseo.doldolseo_msa_crew.domain.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {
    boolean existsByCrewName(String crewName);

    Crew findByCrewNo(Long crewNo);

    Crew findByCrewLeader(String id);

    boolean existsByCrewNoAndCrewLeader(Long crewNo, String id);
}
