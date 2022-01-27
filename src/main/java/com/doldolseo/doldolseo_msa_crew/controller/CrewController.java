package com.doldolseo.doldolseo_msa_crew.controller;

import com.doldolseo.doldolseo_msa_crew.dto.CrewAndCrewMemberDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewMemberDTO;
import com.doldolseo.doldolseo_msa_crew.dto.CrewPageDTO;
import com.doldolseo.doldolseo_msa_crew.service.CrewService;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class CrewController {
    @Autowired
    CrewService service;

    @GetMapping(value = "/crew")
    public ResponseEntity<CrewPageDTO> getCrewList(@PageableDefault(size = 30, sort = "crewName", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCrewPage(pageable));
    }

    @GetMapping(value = "/crew/members/{crewMemberId}")
    public ResponseEntity<List<CrewDTO>> getCrewListByMember(@PathVariable(value = "crewMemberId") String crewMemberId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCrewList(crewMemberId));
    }

    @GetMapping(value = "/crew/{crewNo}")
    public ResponseEntity<CrewAndCrewMemberDTO> getCrew(@PathVariable Long crewNo) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCrew(crewNo));
    }

    @GetMapping(value = "/crew/manage/{crewLeader}")
    public ResponseEntity<CrewAndCrewMemberDTO> getCrewManagement(@PathVariable String crewLeader) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCrew(crewLeader));
    }

    @PostMapping(value = "/crew")
    public ResponseEntity<CrewDTO> crewCreate(CrewDTO dtoIn,
                                              @RequestParam(required = false) MultipartFile imageFile) throws Exception {
        System.out.println(dtoIn.toString()); //temp
        CrewDTO dtoOut = service.createCrew(dtoIn, imageFile);

        return ResponseEntity.status(HttpStatus.OK).body(dtoOut);
    }

    @PutMapping(value = "/crew/{crewNo}")
    public ResponseEntity<String> updateCrew(@RequestBody CrewDTO dto,
                                             @PathVariable(value = "crewNo") Long crewNo) throws Exception {
        System.out.println(dto.toString());
        service.updateCrew(dto, crewNo);
        return ResponseEntity.status(HttpStatus.OK).body("수정 완료");
    }

    @PutMapping(value = "/crew/{crewNo}/question")
    public ResponseEntity<String> updateCrew_Question(@RequestBody CrewDTO dto,
                                                      @PathVariable(value = "crewNo") Long crewNo) throws Exception {
        service.updateCrew_Question(dto, crewNo);
        return ResponseEntity.status(HttpStatus.OK).body("수정 완료");
    }

    @PutMapping(value = "/crew/{crewNo}/images")
    public ResponseEntity<String> updateCrewImage(@RequestParam MultipartFile imageFile,
                                                  @PathVariable(value = "crewNo") Long crewNo) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateCrew_Image(imageFile, crewNo));
    }

    @GetMapping(value = "/crew/check/{crewName}")
    public ResponseEntity<Boolean> crewNameCheck(@PathVariable String crewName) throws Exception {
        Boolean result = service.checkCrewName(crewName);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @ResponseBody
    @GetMapping(value = "/crew/images/{imageFileName}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public byte[] getCrewImage(@PathVariable("imageFileName") String imageFileName) throws IOException {
        final String URL_CREW_IMAGE = System.getProperty("user.dir") + "/src/main/resources/static/crew_image/";
        String imgPath = URL_CREW_IMAGE + imageFileName;
        InputStream in = new FileInputStream(imgPath);
        byte[] imageByteArr = IOUtils.toByteArray(in);
        in.close();
        return imageByteArr;
    }

    @GetMapping(value = "/crew/member/{crewMemberNo}")
    public ResponseEntity<CrewMemberDTO> getCrewMember(@PathVariable(value = "crewMemberNo") Long crewMemberNo) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCrewMember(crewMemberNo));
    }

    @GetMapping(value = "/crew/{crewNo}/members")
    public ResponseEntity<List<CrewMemberDTO>> getCrewMembers(@PathVariable(value = "crewNo") Long crewNo) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCrewMemberList(crewNo));
    }

    @PostMapping(value = "/crew/{crewNo}/member")
    public ResponseEntity<CrewMemberDTO> joinCrew(@RequestBody CrewMemberDTO dtoIn,
                                                  @PathVariable(value = "crewNo") Long crewNo) throws Exception {
        CrewMemberDTO dtoOut = service.createCrewMember(dtoIn);
        return ResponseEntity.status(HttpStatus.OK).body(dtoOut);
    }

    @PutMapping(value = "/crew/{crewNo}/member/{crewMemberNo}")
    public ResponseEntity<String> updateCrewMemberState(@PathVariable(value = "crewNo") Long crewNo,
                                                        @PathVariable(value = "crewMemberNo") Long crewMemberNo) throws Exception {
        service.updateCrewMember(crewMemberNo);
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    @DeleteMapping(value = "/crew/{crewNo}/member/{crewMemberNo}")
    public ResponseEntity<String> deleteCrewMember(@PathVariable(value = "crewNo") Long crewNo,
                                                   @PathVariable(value = "crewMemberNo") Long crewMemberNo) throws Exception {
        service.deleteCrewMember(crewMemberNo);
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }
}
