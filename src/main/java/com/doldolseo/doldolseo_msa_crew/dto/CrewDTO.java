package com.doldolseo.doldolseo_msa_crew.dto;

import com.doldolseo.doldolseo_msa_crew.domain.CrewMember;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class CrewDTO {
    private Long crewNo;
    private String crewName;
    private String areaNoFirst;
    private String areaNoSecond;
    private String areaNoThird;
    private String intro;
    private String introDetail;
    private String recruit;
    private String questionFirst;
    private String questionSecond;
    private String questionThird;
    private String crewImage;
    private Integer crewPoint;
    private LocalDateTime cDate;
    private String crewLeader;
}
