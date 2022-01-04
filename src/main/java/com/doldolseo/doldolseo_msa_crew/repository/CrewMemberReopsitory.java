package com.doldolseo.doldolseo_msa_crew.repository;

import com.doldolseo.doldolseo_msa_crew.domain.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewMemberReopsitory extends JpaRepository<CrewMember, String> {
    List<CrewMember> findAllByCrew_CrewNoAndCrewMemberState(Long crew_crewNo, String state);
    List<CrewMember> findAllByCrew_CrewLeaderAndCrewMemberState(String crew_crewLeader, String state);
}
