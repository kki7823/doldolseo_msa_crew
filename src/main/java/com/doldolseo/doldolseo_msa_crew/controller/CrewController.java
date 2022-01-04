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

@RestController
public class CrewController {
    @Autowired
    CrewService service;

    @GetMapping(value = "/crew")
    public ResponseEntity<CrewPageDTO> getCrewList(@PageableDefault(size = 30, sort = "crewName", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCrewList(pageable));
    }

    @GetMapping(value = "/crew/{crewNo}")
    public ResponseEntity<CrewAndCrewMemberDTO> getCrew(@PathVariable Long crewNo) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCrew(crewNo));
    }

    @GetMapping(value = "/crew/manage/{crewLeader}")
    public ResponseEntity<CrewAndCrewMemberDTO> getCrew(@PathVariable String crewLeader) throws Exception {
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

    @PostMapping(value = "/crew/{crewNo}/member")
    public ResponseEntity<CrewMemberDTO> joinCrew(@RequestBody CrewMemberDTO dtoIn,
                                                  @PathVariable(value = "crewNo") Long crewNo) throws Exception {
        CrewMemberDTO dtoOut = service.createCrewMember(dtoIn);
        return ResponseEntity.status(HttpStatus.OK).body(dtoOut);
    }
}
