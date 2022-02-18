package com.doldolseo.doldolseo_msa_crew.domain;

import lombok.*;
import java.io.Serializable;


/*
    크루원 복합키 (크루 번호 , 멤버 Id)
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CrewMemberId implements Serializable {
    private Long crew;
    private String memberId;
}
