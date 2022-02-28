package com.doldolseo.doldolseo_msa_crew.repository;

import com.doldolseo.doldolseo_msa_crew.domain.CrewMember;
import com.doldolseo.doldolseo_msa_crew.domain.CrewWatingMember;
import com.doldolseo.doldolseo_msa_crew.domain.CrewWatingMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewWatingMemberRepository extends JpaRepository<CrewWatingMember, CrewWatingMemberId> {
    List<CrewWatingMember> findAllByCrew_CrewLeader(String crewLeader);

    Integer countCrewMemberByMemberId(String memberId);
}
