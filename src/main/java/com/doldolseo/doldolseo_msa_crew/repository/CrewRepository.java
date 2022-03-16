package com.doldolseo.doldolseo_msa_crew.repository;

import com.doldolseo.doldolseo_msa_crew.domain.Crew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {
    boolean existsByCrewName(String crewName);

    Crew findByCrewNo(Long crewNo);

    Crew findByCrewLeader(String id);

    boolean existsByCrewLeader(String id);

    @Query(value = "select c from Crew c  ,CrewMember m where m.memberId = ?1 and c.crewNo = m.crew.crewNo")
    Page<Object> findAllByMemberId(Pageable pageable, String memberId);

    @Query(value = "select c.crewName from Crew c where c.crewNo = ?1")
    Optional<Object> findCrewNameByCrewNo(Long crewNo);
}
