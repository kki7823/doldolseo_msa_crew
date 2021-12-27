package com.doldolseo.doldolseo_msa_crew.service;

import com.doldolseo.doldolseo_msa_crew.domain.Crew;
import com.doldolseo.doldolseo_msa_crew.dto.CrewDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewPageDTO;
import com.doldolseo.doldolseo_msa_crew.repository.CrewRepository;
import com.doldolseo.doldolseo_msa_crew.utils.PagingParams;
import com.doldolseo.doldolseo_msa_crew.utils.UploadCrewFileUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class CrewServiceImpl implements CrewService {
    @Autowired
    CrewRepository repository;
    @Autowired
    UploadCrewFileUtil fileUtil;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public CrewPageDTO getCrewList(Pageable pageable) {
        Page<CrewDTO> crewPage = entityPageToDtoPage(repository.findAll(pageable));
        PagingParams pagingParams = new PagingParams(5, crewPage);

        CrewPageDTO crewPageDTO = new CrewPageDTO();
        crewPageDTO.setCrewList(crewPage.getContent());
        crewPageDTO.setEndBlockPage(pagingParams.getStartBlockPage());
        crewPageDTO.setEndBlockPage(pagingParams.getEndBlockPage());
        crewPageDTO.setTotalPages(pagingParams.getTotalPages());

        return crewPageDTO;
    }

    @Override
    public boolean checkCrewName(String crewName) {
        return repository.existsByCrewName(crewName);
    }

    @Override
    public CrewDTO createCrew(CrewDTO dto, MultipartFile imageFile) {
        String crewImageName = "default_member.png";
        if (imageFile != null) {
            if (!imageFile.isEmpty()) {
                crewImageName = fileUtil.saveCrewImg(dto.getCrewName(), imageFile);
            }
        }
        dto.setCrewImage(crewImageName);
        dto.setCDate(LocalDateTime.now());
        dto.setCrewPoint(0);

        Crew crew = repository.save(dtoToEntity(dto));
        return entityToDto(crew);
    }

    @Override
    public CrewDTO getCrew(Long crewNo) {
        return entityToDto(repository.findByCrewNo(crewNo));
    }

    @Override
    public void updateCrew(CrewDTO dto, String action) {

    }

    public Crew dtoToEntity(CrewDTO dto) {
        return modelMapper.map(dto, Crew.class);
    }

    public CrewDTO entityToDto(Crew crew) {
        return modelMapper.map(crew, CrewDTO.class);
    }

    public Page<CrewDTO> entityPageToDtoPage(Page<Crew> crewPage) {
        return modelMapper.map(crewPage, new TypeToken<Page<CrewDTO>>() {
        }.getType());
    }
}
