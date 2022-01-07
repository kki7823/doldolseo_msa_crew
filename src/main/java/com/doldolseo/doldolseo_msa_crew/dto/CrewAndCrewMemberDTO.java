package com.doldolseo.doldolseo_msa_crew.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@ToString
public class CrewAndCrewMemberDTO {
    private CrewDTO crewDTO;
    private List<CrewMemberDTO> crewMemberDTO_Joined;
    private List<CrewMemberDTO> crewMemberDTO_Wating;
}
