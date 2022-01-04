package com.doldolseo.doldolseo_msa_crew.service;

import com.doldolseo.doldolseo_msa_crew.dto.CrewAndCrewMemberDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewMemberDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewPageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface CrewService {
    CrewPageDTO getCrewList(Pageable pageable);

    boolean checkCrewName(String crewName);

    CrewDTO createCrew(CrewDTO dto, MultipartFile imageFile);

    CrewAndCrewMemberDTO getCrew(Long crewNo);

    CrewAndCrewMemberDTO getCrew(String crewLeader);

    void updateCrew(CrewDTO dto, Long crewNo);

    CrewMemberDTO createCrewMember(CrewMemberDTO dtoIn);
}
