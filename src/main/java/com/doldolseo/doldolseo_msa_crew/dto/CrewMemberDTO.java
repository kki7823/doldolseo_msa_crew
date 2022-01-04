package com.doldolseo.doldolseo_msa_crew.dto;


import com.doldolseo.doldolseo_msa_crew.domain.Crew;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CrewMemberDTO {
    private String crewMemberId;
    private Crew crew;
    private String crewMemberState;
    private String answerFirst;
    private String answerSecond;
    private String answerThird;
    private LocalDateTime jDate;
}
