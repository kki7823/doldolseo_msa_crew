package com.doldolseo.doldolseo_msa_crew.controller;

import com.doldolseo.doldolseo_msa_crew.domain.CrewMemberId;
import com.doldolseo.doldolseo_msa_crew.domain.CrewWatingMemberId;
import com.doldolseo.doldolseo_msa_crew.dto.*;
import com.doldolseo.doldolseo_msa_crew.service.CrewService;
import com.doldolseo.doldolseo_msa_crew.utils.AuthorityUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class CrewController {
    @Autowired
    CrewService service;
    @Autowired
    AuthorityUtil authorityUtil;

    /* Crew */
    @PostMapping(value = "/crew")
    public ResponseEntity<?> crewCreate(CrewDTO dtoIn,
                                        @RequestParam(required = false) MultipartFile imageFile,
                                        HttpServletRequest request,
                                        @RequestHeader String userId,
                                        @RequestHeader String role) {
        if (authorityUtil.areYouUser(role)) {
            String authHeader = request.getHeader("Authorization");
            service.createCrew(dtoIn, imageFile, authHeader, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Crew Created");
        } else {
            System.out.println("[Error] 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Fail");
        }
    }

    @GetMapping(value = "/crew")
    public ResponseEntity<CrewPageDTO> getCrewList(@PageableDefault(size = 12, sort = "crewName", direction = Sort.Direction.DESC) Pageable pageable,
                                                   @RequestParam(required = false) String memberId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCrewPage(pageable, memberId));
    }

    @GetMapping(value = "/crew/{crewNo}")
    public ResponseEntity<CrewAndCrewMemberDTO> getCrew(@PathVariable Long crewNo) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCrew(crewNo));
    }

    @GetMapping(value = "/crew/{crewNo}/name")
    @ResponseBody
    public String getCrewName(@PathVariable Long crewNo) throws Exception {
        return service.getCrewName(crewNo);
    }

    @PostMapping(value = "/crew/manage")
    public ResponseEntity<?> getCrewManagement(@RequestHeader String userId,
                                               @RequestHeader String role) {
        if (authorityUtil.areYouCrewLeader(role))
            return ResponseEntity.status(HttpStatus.OK).body(service.getCrew(userId));
        else {
            System.out.println("[Error] 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Fail");
        }
    }

    @GetMapping(value = "/crew/check/{crewName}")
    public ResponseEntity<Boolean> crewNameCheck(@PathVariable String crewName) throws Exception {
        Boolean result = service.checkCrewName(crewName);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping(value = "/crew/{crewNo}")
    public ResponseEntity<String> updateCrew(@RequestBody CrewDTO dto,
                                             @RequestHeader String role,
                                             @PathVariable(value = "crewNo") Long crewNo) throws Exception {
        if (authorityUtil.areYouCrewLeader(role)) {
            service.updateCrew(dto, crewNo);
            return ResponseEntity.status(HttpStatus.OK).body("수정 완료");
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Fail");
    }

    @PutMapping(value = "/crew/{crewNo}/question")
    public ResponseEntity<String> updateCrew_Question(@RequestBody CrewDTO dto,
                                                      @RequestHeader String role,
                                                      @PathVariable(value = "crewNo") Long crewNo) throws Exception {
        if (authorityUtil.areYouCrewLeader(role)) {
            service.updateCrewQuestion(dto, crewNo);
            return ResponseEntity.status(HttpStatus.OK).body("수정 완료");
        } else {
            System.out.println("[Error] 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Fail");
        }
    }

    @PutMapping(value = "/crew/{crewNo}/images")
    public ResponseEntity<String> updateCrewImage(@RequestParam MultipartFile imageFile,
                                                  @PathVariable(value = "crewNo") Long crewNo,
                                                  @RequestHeader String role,
                                                  HttpServletResponse response) throws Exception {
        if (authorityUtil.areYouCrewLeader(role)) {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateCrewImage(imageFile, crewNo));
        } else {
            System.out.println("[Error] 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Fail");
        }
    }

    @PutMapping(value = "/crew/{crewNo}/point")
    public ResponseEntity<String> updateCrewPoint(@PathVariable(value = "crewNo") Long crewNo,
                                                  @RequestParam Integer crewPoint) throws Exception {
        service.updateCrewPoint(crewNo, crewPoint);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(value = "/crew/{crewNo}")
    public ResponseEntity<?> deleteCrew(@PathVariable Long crewNo,
                                        @RequestHeader String role,
                                        @RequestHeader String userId,
                                        HttpServletRequest request) {
        if (authorityUtil.areYouCrewLeader(role)) {
            String authHeader = request.getHeader("Authorization");
            service.deleteCrew(crewNo, authHeader, userId);
            return ResponseEntity.status(HttpStatus.OK).body("크루가 폐쇄 되었습니다.");
        } else {
            System.out.println("[Error] 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Fail");
        }
    }

    @ResponseBody
    @GetMapping(value = "/crew/images/{imageFileName}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public byte[] getCrewImage(@PathVariable("imageFileName") String imageFileName) throws IOException {
        final String URL_CREW_IMAGE = System.getProperty("user.dir") + "/src/main/resources/static/crew_image/";
        String imgPath = URL_CREW_IMAGE + imageFileName;

        try {
            InputStream in = new FileInputStream(imgPath);
            byte[] imageByteArr = IOUtils.toByteArray(in);
            in.close();
            return imageByteArr;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /* CrewMember*/
    @PostMapping(value = "/crew/{crewNo}/member")
    public ResponseEntity<?> joinCrew(@RequestBody CrewWatingMemberDTO dtoIn,
                                      @PathVariable(value = "crewNo") Long crewNo,
                                      @RequestHeader String userId) throws Exception {
        if (service.areYouLeaderThisCrew(dtoIn.getMemberId(), crewNo)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 멤버는 크루장 입니다.");

        } else if (service.areYouAlreadyJoined(crewNo, dtoIn.getMemberId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 멤버는 가입 대기상태 이거나 이미 가입된 멤버 입니다.");

        } else if (service.howManyJoined(dtoIn.getMemberId()) > 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("멤버당 3회 이상의 가입은 불가능 합니다");
        }

        if (authorityUtil.isYou(dtoIn.getMemberId(), userId)) {
            CrewWatingMemberDTO dtoOut = service.createCrewWatingMember(dtoIn);
            service.updateCrewPoint(crewNo, 10);
            return ResponseEntity.status(HttpStatus.OK).body(dtoOut);
        } else {
            System.out.println("[Error] 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Fail");
        }
    }

    @GetMapping(value = "/crew/{crewNo}/check/member/{memberId}")
    public ResponseEntity<Boolean> areYouMemberInThisCrew(@PathVariable("crewNo") Long crewNo,
                                                          @PathVariable("memberId") String userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.checkMemberBelongCrew(new CrewMemberId(crewNo, userId)));
    }

    @GetMapping(value = "/crew/check/member/{memberId}")
    public ResponseEntity<Boolean> areYouJoinedAnyCrew(@PathVariable("memberId") String memberId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.checkIdHasAnyCrew(memberId));
    }

    @GetMapping(value = "/crew/check/member")
    public ResponseEntity<Integer> countCrewMember(@RequestHeader String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.howManyJoined(userId));
    }

    @GetMapping(value = "/crew/member")
    public ResponseEntity<?> getCrewWatingMember(@RequestParam Long crewNo,
                                                 @RequestParam String memberId,
                                                 @RequestHeader String role) {
        if (authorityUtil.areYouCrewLeader(role)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(service.getCrewWatingMember(new CrewWatingMemberId(crewNo, memberId)));
        } else {
            System.out.println("[Error] 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Fail");
        }
    }

    @GetMapping(value = "/crew/{crewNo}/members")
    public ResponseEntity<List<CrewMemberDTO>> getCrewMembers(@PathVariable(value = "crewNo") Long crewNo,
                                                              @RequestHeader String userId,
                                                              @RequestParam String exceptSelf) throws Exception {
        if (exceptSelf.equals("y"))
            return ResponseEntity.status(HttpStatus.OK).body(service.getCrewMemberListExcepSelf(crewNo, userId));
        else
            return ResponseEntity.status(HttpStatus.OK).body(service.getCrewMemberList(crewNo));
    }


    @PostMapping(value = "/crew/{crewNo}/member/{memberId}")
    public ResponseEntity<String> agreeJoin(@PathVariable(value = "crewNo") Long crewNo,
                                            @PathVariable(value = "memberId") String memberId,
                                            @RequestHeader String role) throws Exception {
        if (authorityUtil.areYouCrewLeader(role)) {
            service.deleteCrewWatingMember(new CrewWatingMemberId(crewNo, memberId));
            service.createCrewMember(new CrewMemberId(crewNo, memberId));
            service.updateCrewPoint(crewNo, 100);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } else {
            System.out.println("[Error] 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Fail");
        }
    }

    @DeleteMapping(value = "/crew/{crewNo}/member")
    public ResponseEntity<String> kickMember(@PathVariable(value = "crewNo") Long crewNo,
                                             @RequestParam(value = "memberId") String memberId,
                                             @RequestHeader String role) {
        if (authorityUtil.areYouCrewLeader(role)) {
            service.deleteCrewMember(new CrewMemberId(crewNo, memberId));
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } else {
            System.out.println("[Error] 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Fail");
        }
    }

    @DeleteMapping(value = "/crew/{crewNo}/member/wating")
    public ResponseEntity<String> denyJoin(@PathVariable(value = "crewNo") Long crewNo,
                                           @RequestParam(value = "memberId") String memberId,
                                           @RequestHeader String role) {
        if (authorityUtil.areYouCrewLeader(role)) {
            service.deleteCrewWatingMember(new CrewWatingMemberId(crewNo, memberId));
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } else {
            System.out.println("[Error] 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Fail");
        }
    }

    @DeleteMapping(value = "/crew/{crewNo}/member/{memberId}")
    public ResponseEntity<String> deleteCrewMember(@PathVariable(value = "crewNo") Long crewNo,
                                                   @PathVariable(value = "memberId") String memberId,
                                                   @RequestHeader String userId,
                                                   @RequestHeader String role) {
        if (authorityUtil.areYouUser(role) && memberId.equals(userId)) {
            service.deleteCrewMember(new CrewMemberId(crewNo, memberId));
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } else {
            System.out.println("[Error] 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Fail");
        }
    }

    @PutMapping(value = "/crew/{crewNo}/leader")
    public ResponseEntity<?> delegateLeader(HttpServletRequest request,
                                            @PathVariable Long crewNo,
                                            @RequestParam String memberId,
                                            @RequestHeader String userId,
                                            @RequestHeader String role) {
        if (authorityUtil.areYouCrewLeader(role)) {
            CrewMemberId crewMemberId = new CrewMemberId(crewNo, memberId);
            String idToDelegate = service.getCrewMember(crewMemberId).getMemberId();

            String authHeader = request.getHeader("Authorization");
            service.updateCrewLeader(userId, idToDelegate, authHeader);

            service.updateCrewMemberRole(new CrewMemberId(crewNo, userId));
            service.updateCrewMemberRole(new CrewMemberId(crewNo, idToDelegate));

            return ResponseEntity.status(HttpStatus.OK).body("Delegated");
        } else {
            System.out.println("[Error] 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Fail");
        }
    }

    @PostMapping(value = "/crew/leader/{memberId}")
    public ResponseEntity<?> checkCrewLeader(@PathVariable(value = "memberId") String memberId,
                                             @RequestHeader String role) {
        if (authorityUtil.areYouCrewLeader(role)) {
            return ResponseEntity.status(HttpStatus.OK).body(service.isThisUserCrewLeader(memberId));
        } else {
            System.out.println("[Error] 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Fail");
        }
    }
}
