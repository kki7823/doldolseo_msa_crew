package com.doldolseo.doldolseo_msa_crew.service;

import com.doldolseo.doldolseo_msa_crew.dto.CrewAndCrewMemberDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewMemberDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewPageDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CrewService {
    CrewPageDTO getCrewPage(Pageable pageable);

    List<CrewDTO> getCrewList(String crewMemberId);

    boolean checkCrewName(String crewName);

    CrewDTO createCrew(CrewDTO dto, MultipartFile imageFile);

    CrewAndCrewMemberDTO getCrew(Long crewNo);

    CrewAndCrewMemberDTO getCrew(String crewLeader);

    void updateCrew(CrewDTO dto, Long crewNo);

    String updateCrew_Image(MultipartFile imageFile, Long crewNo);

    void updateCrew_Question(CrewDTO dto, Long crewNo);

    CrewMemberDTO getCrewMember(Long crewMemberNo);

    List<CrewMemberDTO> getCrewMemberList(Long crewrNo);

    CrewMemberDTO createCrewMember(CrewMemberDTO dtoIn);

    void updateCrewMember(Long crewMemberNo);

    void deleteCrewMember(Long crewMemberNo);
}
