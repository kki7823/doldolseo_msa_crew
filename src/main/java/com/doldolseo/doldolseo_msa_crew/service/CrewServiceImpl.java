package com.doldolseo.doldolseo_msa_crew.service;

import com.doldolseo.doldolseo_msa_crew.domain.Crew;
import com.doldolseo.doldolseo_msa_crew.domain.CrewMember;
import com.doldolseo.doldolseo_msa_crew.dto.CrewAndCrewMemberDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewMemberDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewPageDTO;
import com.doldolseo.doldolseo_msa_crew.repository.CrewMemberReopsitory;
import com.doldolseo.doldolseo_msa_crew.repository.CrewRepository;
import com.doldolseo.doldolseo_msa_crew.utils.PagingParams;
import com.doldolseo.doldolseo_msa_crew.utils.UploadCrewFileUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CrewServiceImpl implements CrewService {
    @Autowired
    CrewRepository crewRepository;
    @Autowired
    CrewMemberReopsitory crewMemberReopsitory;
    @Autowired
    UploadCrewFileUtil fileUtil;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public CrewPageDTO getCrewList(Pageable pageable) {
        Page<CrewDTO> crewPage = entityPageToDtoPage(crewRepository.findAll(pageable));
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
        return crewRepository.existsByCrewName(crewName);
    }

    @Override
    public CrewAndCrewMemberDTO getCrew(Long crewNo) {
        Crew crew = crewRepository.findByCrewNo(crewNo);
        List<CrewMember> crewMember = crewMemberReopsitory.findAllByCrew_CrewNoAndCrewMemberState(crewNo, "JOINED");

        CrewAndCrewMemberDTO dto = new CrewAndCrewMemberDTO();
        dto.setCrewDTO(entityToDto(crew));
        dto.setCrewMemberDTO_Joined(entityListToDtoList(crewMember));
        dto.setCrewMemberDTO_Wating(null);

        return dto;
    }

    @Override
    public CrewAndCrewMemberDTO getCrew(String crewLeader) {
        Crew crew = crewRepository.findByCrewLeader(crewLeader);
        List<CrewMember> crewMemberJoined = crewMemberReopsitory.findAllByCrew_CrewLeaderAndCrewMemberState(crewLeader, "JOINED");
        List<CrewMember> crewMemberWating = crewMemberReopsitory.findAllByCrew_CrewLeaderAndCrewMemberState(crewLeader, "WATING");

        CrewAndCrewMemberDTO dto = new CrewAndCrewMemberDTO();
        dto.setCrewDTO(entityToDto(crew));
        dto.setCrewMemberDTO_Joined(entityListToDtoList(crewMemberJoined));
        dto.setCrewMemberDTO_Wating(entityListToDtoList(crewMemberWating));

        return dto;
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

        Crew crew = crewRepository.save((Crew) dtoToEntity(dto));
        return entityToDto(crew);
    }

    @Override
    @Transactional
    public void updateCrew(CrewDTO dto, Long crewNo) {
        Crew crew = crewRepository.findByCrewNo(crewNo);
        crew.setAreaNoFirst(dto.getAreaNoFirst());
        crew.setAreaNoSecond(dto.getAreaNoSecond());
        crew.setAreaNoThird(dto.getAreaNoThird());
        crew.setIntro(dto.getIntro());
        crew.setIntroDetail(dto.getIntroDetail());
        crew.setRecruit(dto.getRecruit());
    }

    @Override
    public CrewMemberDTO createCrewMember(CrewMemberDTO dtoIn) {
        dtoIn.setCrewMemberState("WATING");
        dtoIn.setJDate(LocalDateTime.now());

        CrewMember crewMember = crewMemberReopsitory.save((CrewMember) dtoToEntity(dtoIn));
        return entityToDto(crewMember);
    }

    public Object dtoToEntity(CrewDTO dto) {
        return modelMapper.map(dto, Crew.class);
    }

    public Object dtoToEntity(CrewMemberDTO dto) {
        return modelMapper.map(dto, CrewMember.class);
    }

    public CrewDTO entityToDto(Crew crew) {
        return modelMapper.map(crew, CrewDTO.class);
    }

    public CrewMemberDTO entityToDto(CrewMember crewMember) {
        return modelMapper.map(crewMember, CrewMemberDTO.class);
    }

    public Page<CrewDTO> entityPageToDtoPage(Page<Crew> crewPage) {
        return modelMapper.map(crewPage, new TypeToken<Page<CrewDTO>>() {
        }.getType());
    }

    public List<CrewMemberDTO> entityListToDtoList(List<CrewMember> crewMemberList) {
        return modelMapper.map(crewMemberList, new TypeToken<List<CrewMemberDTO>>() {
        }.getType());
    }
}
