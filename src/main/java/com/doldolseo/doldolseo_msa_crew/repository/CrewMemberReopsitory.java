package com.doldolseo.doldolseo_msa_crew.repository;

import com.doldolseo.doldolseo_msa_crew.domain.CrewMember;
import com.doldolseo.doldolseo_msa_crew.domain.CrewMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewMemberReopsitory extends JpaRepository<CrewMember, CrewMemberId> {
    List<CrewMember> findAllByCrew_CrewNoAndCrewMemberState(Long crew_crewNo, String state);

    List<CrewMember> findAllByCrew_CrewLeaderAndCrewMemberState(String crew_crewLeader, String state);

    @Query(value = "select c.crew from CrewMember c where c.memberId = ?1")
    List<Object> findCrewByMemberId(String memberId);

    Boolean existsByCrewCrewNoAndMemberIdAndCrewMemberState(Long crewNo, String memberId, String state);
}