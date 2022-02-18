package com.doldolseo.doldolseo_msa_crew.service;

import com.doldolseo.doldolseo_msa_crew.domain.CrewMemberId;
import com.doldolseo.doldolseo_msa_crew.dto.CrewAndCrewMemberDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewMemberDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewPageDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CrewService {
    CrewPageDTO getCrewPage(Pageable pageable, String memberId);

    List<CrewDTO> getCrewList(String crewMemberId);

    boolean checkCrewName(String crewName);

    void createCrew(CrewDTO dto, MultipartFile imageFile, String authHeader, String userId);

    CrewAndCrewMemberDTO getCrew(Long crewNo);

    CrewAndCrewMemberDTO getCrew(String crewLeader);

    void updateCrew(CrewDTO dto, Long crewNo) throws Exception;

    String updateCrew_Image(MultipartFile imageFile, Long crewNo);

    void updateCrewMember(CrewMemberId crewMemberId);

    CrewMemberDTO getCrewMember(CrewMemberId crewMemberId);

    List<CrewMemberDTO> getCrewMemberList(Long crewrNo);

    Boolean areYouCrewMember(Long crewNo, String memberId);

    CrewMemberDTO createCrewMember(CrewMemberDTO dtoIn);

    void updateCrew_Question(CrewDTO dto, Long crewNo);

    void deleteCrewMember(CrewMemberId crewMemberId);

    void updateCrewLeader(String userId, String idToDelegate, String authHeader);

    boolean isThisUserCrewLeader(String id);
}
