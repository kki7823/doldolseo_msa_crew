package com.doldolseo.doldolseo_msa_crew.service;

import com.doldolseo.doldolseo_msa_crew.dto.CrewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface CrewService {
    public Page<CrewDTO> getCrewList(Pageable pageable);
    public boolean checkCrewName(String crewName);
    public CrewDTO createCrew(CrewDTO dto, MultipartFile imageFile);
    public CrewDTO getCrew(Long crewNo);
    public void updateCrew(CrewDTO dto, String action);
}
