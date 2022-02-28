package com.doldolseo.doldolseo_msa_crew.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "CREW_WATING_MEMBER_TBL")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(CrewWatingMemberId.class)
public class CrewWatingMember implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "CREW_NO")
    private Crew crew;

    @Id
    @Column(name = "MEMBER_ID")
    private String memberId;

    @Column(name = "ANSWER_1")
    private String answerFirst;

    @Column(name = "ANSWER_2")
    private String answerSecond;

    @Column(name = "ANSWER_3")
    private String answerThird;

    @Column(name = "J_DATE")
    private LocalDateTime jDate;
}
