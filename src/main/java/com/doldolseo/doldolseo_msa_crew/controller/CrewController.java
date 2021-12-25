package com.doldolseo.doldolseo_msa_crew.controller;

import com.doldolseo.doldolseo_msa_crew.dto.CrewDTO;
import com.doldolseo.doldolseo_msa_crew.service.CrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CrewController {
    @Autowired
    CrewService crewService;

    @PostMapping(value = "/crew")
    public ResponseEntity<CrewDTO> crewCreate(CrewDTO dtoIn,
                                              @RequestParam(required = false) MultipartFile imageFile) throws Exception {
        System.out.println(dtoIn.toString()); //temp
        CrewDTO dtoOut = crewService.createCrew(dtoIn, imageFile);

        return ResponseEntity.status(HttpStatus.OK).body(dtoOut);
    }

    @GetMapping(value = "/crew/{crewName}")
    public ResponseEntity<Boolean> crewNameCheck(@PathVariable String crewName) throws Exception {
        Boolean result = crewService.checkCrewName(crewName);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

//    @ResponseBody
//    @GetMapping(value = "/review/images/{imageUUID}/{imageFileName}",
//            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
//    public byte[] getReviewImage(@PathVariable("imageUUID") String uuid,
//                                 @PathVariable("imageFileName") String imageFileName,
//                                 HttpServletRequest request) throws IOException {
//
//        String imgPath = System.getProperty("user.dir") + "/src/main/resources/static/review_image/" + uuid + "/" + imageFileName;
//        InputStream in = new FileInputStream(imgPath);
//        byte[] imageByteArr = IOUtils.toByteArray(in);
//        in.close();
//        return imageByteArr;
//    }
}
