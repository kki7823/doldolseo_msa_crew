package com.doldolseo.doldolseo_msa_crew.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CrewPageDTO {
    private List<CrewDTO> crewList;
    private int startBlockPage;
    private int endBlockPage;
    private int totalPages;
}
