package com.doldolseo.doldolseo_msa_crew.controller;

import com.doldolseo.doldolseo_msa_crew.dto.CrewDTO;
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
    CrewService crewService;

    @GetMapping(value = "/crew")
    public ResponseEntity<CrewPageDTO> getCrewList(@PageableDefault(size = 30, sort = "crewName", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(crewService.getCrewList(pageable));
    }

    @GetMapping(value = "/crew/{crewNo}")
    public ResponseEntity<CrewDTO> getCrewList(@PathVariable Long crewNo) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(crewService.getCrew(crewNo));
    }

    @PostMapping(value = "/crew")
    public ResponseEntity<CrewDTO> crewCreate(CrewDTO dtoIn,
                                              @RequestParam(required = false) MultipartFile imageFile) throws Exception {
        System.out.println(dtoIn.toString()); //temp
        CrewDTO dtoOut = crewService.createCrew(dtoIn, imageFile);

        return ResponseEntity.status(HttpStatus.OK).body(dtoOut);
    }

    @GetMapping(value = "/crew/check/{crewName}")
    public ResponseEntity<Boolean> crewNameCheck(@PathVariable String crewName) throws Exception {
        Boolean result = crewService.checkCrewName(crewName);

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
}
