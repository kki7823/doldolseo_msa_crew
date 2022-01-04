package com.doldolseo.doldolseo_msa_crew.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CREW_TBL")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CREW_NO")
    private Long crewNo;

    @Column(name = "CREW_NAME")
    private String crewName;

    @Column(name = "AREA_NO_1")
    private String areaNoFirst;

    @Column(name = "AREA_NO_2")
    private String areaNoSecond;

    @Column(name = "AREA_NO_3")
    private String areaNoThird;

    private String intro;

    @Column(name = "INTRO_DETAIL")
    private String introDetail;

    private String recruit;

    @Column(name = "QUESTION_1")
    private String questionFirst;

    @Column(name = "QUESTION_2")
    private String questionSecond;

    @Column(name = "QUESTION_3")
    private String questionThird;

    @Column(name = "CREW_IMG")
    private String crewImage;

    @Column(name = "CREW_POINT")
    private Integer crewPoint;

    @Column(name = "C_DATE")
    private LocalDateTime cDate;

    private String crewLeader;
}
