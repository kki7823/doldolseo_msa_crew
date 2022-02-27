package com.doldolseo.doldolseo_msa_crew.repository;

import com.doldolseo.doldolseo_msa_crew.domain.Crew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {
    boolean existsByCrewName(String crewName);

    Crew findByCrewNo(Long crewNo);

    Crew findByCrewLeader(String id);

    boolean existsByCrewLeader(String id);

    @Query(value = "select b from Crew b ,CrewMember c where c.memberId = ?1 and b.crewNo = c.crew.crewNo")
    Page<Object> findAllByMemberId(Pageable pageable, String memberId);

}
