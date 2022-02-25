package com.doldolseo.doldolseo_msa_crew.service;

import com.doldolseo.doldolseo_msa_crew.domain.Crew;
import com.doldolseo.doldolseo_msa_crew.domain.CrewMember;
import com.doldolseo.doldolseo_msa_crew.domain.CrewMemberId;
import com.doldolseo.doldolseo_msa_crew.dto.CrewAndCrewMemberDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewMemberDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewPageDTO;
import com.doldolseo.doldolseo_msa_crew.repository.CrewMemberReopsitory;
import com.doldolseo.doldolseo_msa_crew.repository.CrewRepository;
import com.doldolseo.doldolseo_msa_crew.utils.PagingParams;
import com.doldolseo.doldolseo_msa_crew.utils.OtherRestUtil;
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
    @Autowired
    OtherRestUtil restUtil;

    /* Crew */
    @Override
    public CrewPageDTO getCrewPage(Pageable pageable, String memberId) {
        Page<CrewDTO> crewPage;
        if (memberId == null)
            crewPage = entityPageToDtoPage(crewRepository.findAll(pageable));
        else
            crewPage = entityPageToDtoPage_Obj(crewRepository.findAllByMemberId(pageable, memberId));

        PagingParams pagingParams = new PagingParams(5, crewPage);
        CrewPageDTO crewPageDTO = new CrewPageDTO();
        crewPageDTO.setCrewList(crewPage.getContent());
        crewPageDTO.setEndBlockPage(pagingParams.getStartBlockPage());
        crewPageDTO.setEndBlockPage(pagingParams.getEndBlockPage());
        crewPageDTO.setTotalPages(pagingParams.getTotalPages());

        return crewPageDTO;
    }

    @Override
    public List<CrewDTO> getCrewList(String crewMemberId) {
        return entityListToDtoList_Obj(crewMemberReopsitory.findCrewByMemberId(crewMemberId));
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
        dto.setCrewMemberDTO_Joined(entityListToDtoList_CrewMember(crewMember));
        dto.setCrewMemberDTO_Wating(null);

        return dto;
    }

    @Override
    public CrewAndCrewMemberDTO getCrew(String crewLeader) {
        Crew crew = crewRepository.findByCrewLeader(crewLeader);

        List<CrewMember> crewMemberJoined =
                crewMemberReopsitory.findAllByCrew_CrewLeaderAndCrewMemberState(crewLeader, "JOINED");
        List<CrewMember> crewMemberWating =
                crewMemberReopsitory.findAllByCrew_CrewLeaderAndCrewMemberState(crewLeader, "WATING");

        CrewAndCrewMemberDTO dto = new CrewAndCrewMemberDTO();
        dto.setCrewDTO(entityToDto(crew));
        dto.setCrewMemberDTO_Joined(entityListToDtoList_CrewMember(crewMemberJoined));
        dto.setCrewMemberDTO_Wating(entityListToDtoList_CrewMember(crewMemberWating));

        return dto;
    }

    /*
    - 크루 생성
    - 멤버 권한 변경 (USER -> CREWLEADER)
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCrew(CrewDTO dto,
                           MultipartFile imageFile,
                           String authHeader,
                           String userId) {
        String crewImageName = "default_member.png";
        if (imageFile != null) {
            if (!imageFile.isEmpty()) {
                crewImageName = fileUtil.saveCrewImg(dto.getCrewName(), imageFile);
            }
        }
        dto.setCrewImage(crewImageName);
        dto.setCDate(LocalDateTime.now());
        dto.setCrewPoint(0);
        dto.setCrewLeader(userId);

        crewRepository.save((Crew) dtoToEntity(dto));

        String updateMemberUri
                = "http://doldolseo-member-rest.default.svc.cluster.local:8080/doldolseo/member/role";
        restUtil.member_UpdateRole(updateMemberUri, authHeader, userId, "CREATE"); //변경된 권한
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
    public void updateCrew_Question(CrewDTO dto, Long crewNo) {
        Crew crew = crewRepository.findByCrewNo(crewNo);
        crew.setQuestionFirst(dto.getQuestionFirst());
        if (dto.getQuestionSecond() != null) {
            crew.setQuestionSecond(dto.getQuestionSecond());
        }
        if (dto.getQuestionThird() != null) {
            crew.setQuestionThird(dto.getQuestionThird());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateCrew_Image(MultipartFile imageFile, Long crewNo) {
        Crew crew = crewRepository.findByCrewNo(crewNo);

        String crewImageName = "default_member.png";
        if (imageFile != null) {
            if (!imageFile.isEmpty()) {
                crewImageName = fileUtil.saveCrewImg(crew.getCrewName(), imageFile);
            }
        }
        crew.setCrewImage(crewImageName);
        return crew.getCrewImage();
    }

    /*
        크루 삭제
        - 크루및 크루원 삭제
        - 크루게시글 서비스 : 해당 크루게시글 삭제
        - 회원 서비스 : CREWLEADER -> USER 강등
        ** 추후 분산 트랜잭션 처리 필요
    */
    @Override
    public void deleteCrew(Long crewNo, String authHeader, String userId) {
        String deleteCrewPostUri
                = "http://doldolseo-crew-post-rest.default.svc.cluster.local:8080/doldolseo/crew/post/all/" + crewNo;
        restUtil.crewPost_DeletePost(deleteCrewPostUri);

        String updateMemberUri
                = "http://doldolseo-member-rest.default.svc.cluster.local:8080/doldolseo/member/role";
        restUtil.member_UpdateRole(updateMemberUri, authHeader, userId, "DEMOTION");
        crewRepository.deleteById(crewNo);
    }

    /* CrewMember */
    @Override
    public CrewMemberDTO createCrewMember(CrewMemberDTO dtoIn) {
        dtoIn.setCrewMemberState("WATING");
        dtoIn.setJDate(LocalDateTime.now());

        CrewMember crewMember = crewMemberReopsitory.save((CrewMember) dtoToEntity(dtoIn));
        return entityToDto(crewMember);
    }

    @Override
    public CrewMemberDTO getCrewMember(CrewMemberId crewMemberId) {
        return entityToDto(crewMemberReopsitory.getById(crewMemberId));
    }

    @Override
    public List<CrewMemberDTO> getCrewMemberList(Long crewNo) {
        return entityListToDtoList_CrewMember(crewMemberReopsitory
                .findAllByCrew_CrewNoAndCrewMemberState(crewNo, "JOINED"));
    }

    @Override
    public Integer howManyJoined(String memberId) {
        return crewMemberReopsitory
                .countCrewMemberByMemberId(memberId);
    }

    @Override
    public Boolean areYouCrewMember(Long crewNo, String memberId) {
        return crewMemberReopsitory
                .existsByCrewCrewNoAndMemberIdAndCrewMemberState(crewNo, memberId, "JOINED");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCrewMember(CrewMemberId crewMemberId) {
        CrewMember crewMember = crewMemberReopsitory.getById(crewMemberId);
        if (crewMember.getCrewMemberState().equals("WATING")) {
            crewMember.setCrewMemberState("JOINED");
        }
    }

    @Override
    public void deleteCrewMember(CrewMemberId crewMemberId) {
        String deleteCrewPostUri
                = "http://doldolseo-crew-post-rest.default.svc.cluster.local:8080/doldolseo/crew/post/member" + crewMemberId.getMemberId();
        restUtil.crewPost_DeletePost(deleteCrewPostUri);
        crewMemberReopsitory.deleteById(crewMemberId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCrewLeader(String userId, String idToDelegate, String authHeader) {
        Crew crew = crewRepository.findByCrewLeader(userId);
        if (crew.getCrewLeader().equals(userId)) {
            crew.setCrewLeader(idToDelegate);

            String updateMemberUri
                    = "http://doldolseo-member-rest.default.svc.cluster.local:8080/doldolseo/member/role";
            restUtil.member_UpdateRole(updateMemberUri, authHeader, idToDelegate, "PROMOTION");
            restUtil.member_UpdateRole(updateMemberUri, authHeader, userId, "DEMOTION");
        } else {
            System.out.println("This user is not leader of " + crew.getCrewName());
        }
    }

    @Override
    public boolean isThisUserCrewLeader(String id) {
        return crewRepository.existsByCrewLeader(id);
    }

    /* Utils */
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

    public Page<CrewDTO> entityPageToDtoPage_Obj(Page<Object> objectPage) {
        return modelMapper.map(objectPage, new TypeToken<Page<CrewDTO>>() {
        }.getType());
    }

    public List<CrewDTO> entityListToDtoList_Obj(List<Object> objectList) {
        return modelMapper.map(objectList, new TypeToken<List<CrewDTO>>() {
        }.getType());
    }

    public List<CrewDTO> entityListToDtoList_Crew(List<Crew> crewList) {
        return modelMapper.map(crewList, new TypeToken<List<CrewDTO>>() {
        }.getType());
    }

    public List<CrewMemberDTO> entityListToDtoList_CrewMember(List<CrewMember> crewMemberList) {
        return modelMapper.map(crewMemberList, new TypeToken<List<CrewMemberDTO>>() {
        }.getType());
    }
}
