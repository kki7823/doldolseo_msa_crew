package com.doldolseo.doldolseo_msa_crew.service;

import com.doldolseo.doldolseo_msa_crew.domain.CrewMemberId;
import com.doldolseo.doldolseo_msa_crew.domain.CrewWatingMemberId;
import com.doldolseo.doldolseo_msa_crew.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CrewService {
    CrewPageDTO getCrewPage(Pageable pageable, String memberId);

    List<CrewDTO> getCrewList(String crewMemberId);

    boolean checkCrewName(String crewName);

    void createCrew(CrewDTO dto, MultipartFile imageFile, String authHeader, String userId);

    CrewAndCrewMemberDTO getCrew(Long crewNo);

    CrewAndAllCrewMemberDTO getCrew(String crewLeader);

    void updateCrew(CrewDTO dto, Long crewNo) throws Exception;

    String updateCrew_Image(MultipartFile imageFile, Long crewNo);

    void deleteCrew(Long crewNo, String authHeader, String userId);

    CrewMemberDTO createCrewMember(CrewMemberId crewMemberId);

    CrewMemberDTO getCrewMember(CrewMemberId crewMemberId);

    CrewWatingMemberDTO getCrewWatingMember(CrewWatingMemberId crewWatingMemberId);

    List<CrewMemberDTO> getCrewMemberList(Long crewrNo);

    List<CrewMemberDTO> getCrewMemberListExcepSelf(Long crewNo, String memberId);

    Boolean checkMemberBelongCrew(CrewMemberId crewMemberId);

     Boolean checkIdHasAnyCrew(String memberId);

    Integer howManyJoined(String memberId);

    CrewWatingMemberDTO createCrewWatingMember(CrewWatingMemberDTO dtoIn);

    boolean areYouLeaderThisCrew(String memberId, Long crewNo);

    boolean areYouAlreadyJoined(Long crewNo, String memberId);

    void updateCrew_Question(CrewDTO dto, Long crewNo);

    void deleteCrewMember(CrewMemberId crewMemberId);

    void deleteCrewWatingMember(CrewWatingMemberId crewWatingMemberId);

    void updateCrewMemberRole(CrewMemberId crewMemberId);

    void updateCrewLeader(String userId, String idToDelegate, String authHeader);

    boolean isThisUserCrewLeader(String id);
}
