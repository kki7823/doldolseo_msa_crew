package com.doldolseo.doldolseo_msa_crew.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

public class UploadFileUtil {
    public UploadFileUtil(String uploadPath) {
    }

    public void trasferFile(MultipartFile multipartFile, Path savepath) {
        try {
            multipartFile.transferTo(new File(savepath.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeDirIfNoExist(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void moveFile(Path src, Path dst) {
        try {
            Files.move(src, dst, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //실제 저장될 이미지 이름 생성 -> 저잘할파일이름.확장자 ex) myCrew.png
    public String makeSaveName(String originalFileName, String nameWantSave) {
        String[] splitedFileName = originalFileName.split("\\.");
        String imgExtension = splitedFileName[splitedFileName.length - 1]; //end Of FileName Array splited by '.'

        return nameWantSave + "." + imgExtension;
    }

    public void deleteFile(File fileToDelete) {
        if (fileToDelete.exists()) {
            boolean isDeleted = fileToDelete.delete();
            logWhenDeleteFile(isDeleted, fileToDelete.getPath());
        }
    }

    public void deleteFilesInDir(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    boolean isDeleted = files[i].delete();
                    logWhenDeleteFile(isDeleted, files[i].getPath());
                }
            }
        }
    }

    public void logWhenDeleteFile(boolean isDeleted, String filePath) {
        if (isDeleted) {
            System.out.printf("[%s] %s 삭제 완료", LocalDateTime.now(), filePath);
        } else {
            System.out.printf("[%s] %s 삭제 실패", LocalDateTime.now(), filePath);
        }
    }
}
