package com.doldolseo.doldolseo_msa_crew.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CREW_MEMBER_TBL")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CREW_MEMBER_NO")
    private Long crewMemberNo;

    @Column(name = "CREW_MEMBER_ID")
    private String crewMemberId;

    @Column(name = "CREW_MEMBER_STATE")
    private String crewMemberState;

    @Column(name = "ANSWER_1")
    private String answerFirst;

    @Column(name = "ANSWER_2")
    private String answerSecond;

    @Column(name = "ANSWER_3")
    private String answerThird;

    @Column(name = "J_DATE")
    private LocalDateTime jDate;

    @ManyToOne
    @JoinColumn(name = "CREW_NO")
    private Crew crew;
}