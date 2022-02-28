package com.doldolseo.doldolseo_msa_crew.repository;

import com.doldolseo.doldolseo_msa_crew.domain.CrewMember;
import com.doldolseo.doldolseo_msa_crew.domain.CrewMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewMemberReopsitory extends JpaRepository<CrewMember, CrewMemberId> {
    List<CrewMember> findAllByCrew_CrewNo(Long crewNo);

    @Query(value = "select cm from CrewMember cm where cm.crew.crewNo = ?1 and not cm.memberId = ?2")
    List<CrewMember> findAllByCrew_CrewNoExceptSelf(Long crewNo, String memberId);

    List<CrewMember> findAllByCrew_CrewLeader(String crewLeader);

    @Query(value = "select c.crew from CrewMember c where c.memberId = ?1")
    List<Object> findCrewByMemberId(String memberId);

    Integer countCrewMemberByMemberId(String memberId);
}