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
    private Crew crew;
    private String memberId;
    private String memberRole;
    private LocalDateTime jDate;
}
