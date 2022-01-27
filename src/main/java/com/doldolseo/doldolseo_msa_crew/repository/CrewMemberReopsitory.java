package com.doldolseo.doldolseo_msa_crew.repository;

import com.doldolseo.doldolseo_msa_crew.domain.Crew;
import com.doldolseo.doldolseo_msa_crew.domain.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewMemberReopsitory extends JpaRepository<CrewMember, Long> {
    List<CrewMember> findAllByCrew_CrewNoAndCrewMemberState(Long crew_crewNo, String state);

    List<CrewMember> findAllByCrew_CrewLeaderAndCrewMemberState(String crew_crewLeader, String state);

    @Query(value = "select c.crew from CrewMember c where c.crewMemberId = ?1")
    List<Object> findCrewByCrewMemberId(String crewMemberId);
}