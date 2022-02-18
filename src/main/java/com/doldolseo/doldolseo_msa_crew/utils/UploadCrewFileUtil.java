package com.doldolseo.doldolseo_msa_crew.utils;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadCrewFileUtil extends UploadFileUtil {
    private final String ROOT_PATH;
    private final String CREW_IMG_PATH;

    public UploadCrewFileUtil(String uploadPath) {
        super(uploadPath);
        this.ROOT_PATH = uploadPath;
        this.CREW_IMG_PATH = ROOT_PATH + "/crew_image/";
    }

    public String saveCrewImg(String crewName, MultipartFile crewImgFile) {
        if (crewImgFile != null) {
            String originalImgFileName = crewImgFile.getOriginalFilename();
            String fileName = crewName + "." + originalImgFileName.substring(originalImgFileName.lastIndexOf(".") + 1);
            makeDirIfNoExist(CREW_IMG_PATH);
            Path savePath = Paths.get(CREW_IMG_PATH + "/" + fileName);
            trasferFile(crewImgFile, savePath);

            return fileName;
        }
        return null;
    }
}
