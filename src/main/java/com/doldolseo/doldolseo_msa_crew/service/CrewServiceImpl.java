package com.doldolseo.doldolseo_msa_crew.service;

import com.doldolseo.doldolseo_msa_crew.domain.*;
import com.doldolseo.doldolseo_msa_crew.dto.*;
import com.doldolseo.doldolseo_msa_crew.repository.CrewMemberReopsitory;
import com.doldolseo.doldolseo_msa_crew.repository.CrewRepository;
import com.doldolseo.doldolseo_msa_crew.repository.CrewWatingMemberRepository;
import com.doldolseo.doldolseo_msa_crew.utils.PagingParams;
import com.doldolseo.doldolseo_msa_crew.utils.OtherRestUtil;
import com.doldolseo.doldolseo_msa_crew.utils.RedisUtil;
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
    CrewWatingMemberRepository crewWatingMemberRepository;
    @Autowired
    UploadCrewFileUtil fileUtil;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    OtherRestUtil restUtil;
    @Autowired
    RedisUtil redisUtil;

    /* Crew */

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

        Crew crew = (Crew) dtoToEntity(dto);
        crewRepository.save(crew);

        //크루원 등록
        CrewMemberDTO crewMemberDTO = new CrewMemberDTO();
        crewMemberDTO.setCrew(crew);
        crewMemberDTO.setMemberId(userId);
        crewMemberDTO.setMemberRole("CREWLEADER");
        crewMemberDTO.setJDate(LocalDateTime.now());
        crewMemberReopsitory.save((CrewMember) dtoToEntity(crewMemberDTO));

        String updateMemberUri
                = "http://doldolseo-member-rest.rest.svc.cluster.local:8080/doldolseo/member/role";
        restUtil.member_UpdateRole(updateMemberUri, authHeader, userId, "PROMOTION"); //권한 변경
    }

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
        List<CrewMember> crewMember = crewMemberReopsitory.findAllByCrew_CrewNoOrderByMemberRole(crewNo);

        CrewAndCrewMemberDTO dto = new CrewAndCrewMemberDTO();
        dto.setCrew(entityToDto(crew));
        dto.setCrewMember(entityListToDtoList_CrewMember(crewMember));
        return dto;
    }

    @Override
    public CrewAndAllCrewMemberDTO getCrew(String crewLeader) {
        Crew crew = crewRepository.findByCrewLeader(crewLeader);

        List<CrewMember> crewMember =
                crewMemberReopsitory.findAllByCrew_CrewLeaderOrderByMemberRole(crewLeader);
        List<CrewWatingMember> crewMemberWating =
                crewWatingMemberRepository.findAllByCrew_CrewLeader(crewLeader);

        CrewAndAllCrewMemberDTO dto = new CrewAndAllCrewMemberDTO();
        dto.setCrew(entityToDto(crew));
        dto.setCrewMember(entityListToDtoList_CrewMember(crewMember));
        dto.setCrewWatingMember(entityListToDtoList_CrewWatingMember(crewMemberWating));

        return dto;
    }

    @Override
    public String getCrewName(Long crewNo) throws Exception {
        if (redisUtil.isExist(crewNo)) {
            return redisUtil.get(crewNo);
        } else {
            String crewName = (String) crewRepository.findCrewNameByCrewNo(crewNo).orElseThrow(() -> {
                return new Exception("존재하지 않는 크루 입니다.");
            });
            redisUtil.put(crewNo, crewName, 60L);
            return crewName;
        }
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
    public void updateCrewQuestion(CrewDTO dto, Long crewNo) {
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
    public String updateCrewImage(MultipartFile imageFile, Long crewNo) throws Exception {
        Crew crew = crewRepository.findById(crewNo).orElseThrow(() -> {
            return new Exception("해당하는 크루가 없습니다");
        });
        ;

        String crewImageName = "default_member.png";
        if (imageFile != null) {
            if (!imageFile.isEmpty()) {
                crewImageName = fileUtil.saveCrewImg(crew.getCrewName(), imageFile);
            }
        }
        crew.setCrewImage(crewImageName);
        return crew.getCrewImage();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCrewPoint(Long crewNo, Integer crewPoint) throws Exception {
        Crew crew = crewRepository.findById(crewNo).orElseThrow(() -> {
            return new Exception("해당하는 크루가 없습니다");
        });

        int currPoint = crew.getCrewPoint();
        crew.setCrewPoint(crewPoint + currPoint);
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
                = "http://doldolseo-crew-post-rest.rest.svc.cluster.local:8080/doldolseo/crew/post/all/" + crewNo;
        restUtil.crewPost_DeletePost(deleteCrewPostUri);

        String updateMemberUri
                = "http://doldolseo-member-rest.rest.svc.cluster.local:8080/doldolseo/member/role";
        restUtil.member_UpdateRole(updateMemberUri, authHeader, userId, "DEMOTION");
        crewRepository.deleteById(crewNo);
    }

    /* CrewMember */
    @Override
    public CrewMemberDTO createCrewMember(CrewMemberId crewMemberId) {
        CrewMemberDTO crewMemberDTO = new CrewMemberDTO();
        crewMemberDTO.setCrew(crewRepository.getById(crewMemberId.getCrew()));
        crewMemberDTO.setMemberId(crewMemberId.getMemberId());
        crewMemberDTO.setMemberRole("MEMBER");
        crewMemberDTO.setJDate(LocalDateTime.now());

        CrewMember crewMember
                = crewMemberReopsitory.save((CrewMember) dtoToEntity(crewMemberDTO));
        return entityToDto(crewMember);
    }

    @Override
    public CrewWatingMemberDTO createCrewWatingMember(CrewWatingMemberDTO dtoIn) {
        dtoIn.setJDate(LocalDateTime.now());

        CrewWatingMember crewWatingMember = crewWatingMemberRepository.save((CrewWatingMember) dtoToEntity(dtoIn));
        return entityToDto(crewWatingMember);
    }

    @Override
    public boolean areYouLeaderThisCrew(String memberId, Long crewNo) {
        String crewLeader = getCrew(crewNo).getCrew().getCrewLeader();
        return crewLeader.equals(memberId);

    }

    @Override
    public boolean areYouAlreadyJoined(Long crewNo, String memberId) {
        boolean isExistMember = crewMemberReopsitory.existsById(new CrewMemberId(crewNo, memberId));
        boolean isExistWatingMember = crewWatingMemberRepository.existsById(new CrewWatingMemberId(crewNo, memberId));

        return isExistMember || isExistWatingMember;
    }

    @Override
    public CrewMemberDTO getCrewMember(CrewMemberId crewMemberId) {
        return entityToDto(crewMemberReopsitory.getById(crewMemberId));
    }

    @Override
    public CrewWatingMemberDTO getCrewWatingMember(CrewWatingMemberId crewWatingMemberId) {
        return entityToDto(crewWatingMemberRepository.getById(crewWatingMemberId));
    }

    @Override
    public List<CrewMemberDTO> getCrewMemberList(Long crewNo) {
        return entityListToDtoList_CrewMember(crewMemberReopsitory
                .findAllByCrew_CrewNoOrderByMemberRole(crewNo));
    }

    @Override
    public List<CrewMemberDTO> getCrewMemberListExcepSelf(Long crewNo, String memberId) {
        return entityListToDtoList_CrewMember(crewMemberReopsitory
                .findAllByCrew_CrewNoExceptSelf(crewNo, memberId));
    }

    @Override
    public Integer howManyJoined(String memberId) {
        Integer numOfJoined = crewMemberReopsitory.countCrewMemberByMemberId(memberId);
        Integer numOfWating = crewWatingMemberRepository.countCrewMemberByMemberId(memberId);

        return numOfJoined + numOfWating;
    }

    @Override
    public Boolean checkMemberBelongCrew(CrewMemberId crewMemberId) {
        return crewMemberReopsitory
                .existsById(crewMemberId);
    }

    @Override
    public Boolean checkIdHasAnyCrew(String memberId) {
        return crewMemberReopsitory
                .existsByMemberId(memberId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCrewMemberRole(CrewMemberId crewMemberId) {
        CrewMember crewMember = crewMemberReopsitory.getById(crewMemberId);
        if (crewMember.getMemberRole().equals("MEMBER")) {
            crewMember.setMemberRole("CREWLEADER");
        } else if (crewMember.getMemberRole().equals("CREWLEADER")) {
            crewMember.setMemberRole("MEMBER");
        } else {
            System.out.println("[updateCrewMemberRole] 멤버 권한 오류");
        }
    }

    @Override
    public void deleteCrewMember(CrewMemberId crewMemberId) {
        String deleteCrewPostUri
                = "http://doldolseo-crew-post-rest.rest.svc.cluster.local:8080/doldolseo/crew/post/member/" + crewMemberId.getMemberId();
        restUtil.crewPost_DeletePost(deleteCrewPostUri);
        crewMemberReopsitory.deleteById(crewMemberId);
    }

    @Override
    public void deleteCrewWatingMember(CrewWatingMemberId crewWatingMemberId) {
        crewWatingMemberRepository.deleteById(crewWatingMemberId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCrewLeader(String userId, String idToDelegate, String authHeader) {
        Crew crew = crewRepository.findByCrewLeader(userId);
        if (crew.getCrewLeader().equals(userId)) {
            crew.setCrewLeader(idToDelegate);

            String updateMemberUri
                    = "http://doldolseo-member-rest.rest.svc.cluster.local:8080/doldolseo/member/role";
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

    public Object dtoToEntity(CrewWatingMemberDTO dto) {
        return modelMapper.map(dto, CrewWatingMember.class);
    }


    public CrewDTO entityToDto(Crew crew) {
        return modelMapper.map(crew, CrewDTO.class);
    }

    public CrewMemberDTO entityToDto(CrewMember crewMember) {
        return modelMapper.map(crewMember, CrewMemberDTO.class);
    }

    public CrewWatingMemberDTO entityToDto(CrewWatingMember crewWatingMember) {
        return modelMapper.map(crewWatingMember, CrewWatingMemberDTO.class);
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

    public List<CrewWatingMemberDTO> entityListToDtoList_CrewWatingMember(List<CrewWatingMember> crewWatingMemberList) {
        return modelMapper.map(crewWatingMemberList, new TypeToken<List<CrewWatingMemberDTO>>() {
        }.getType());
    }
}
