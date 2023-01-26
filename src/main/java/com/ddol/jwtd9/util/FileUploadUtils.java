package com.ddol.jwtd9.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtils {
    public static String saveFile(String uploadDir, String fileName, MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        System.out.println(uploadPath.toRealPath());
        if(!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String replaceFileName = fileName + "." + FilenameUtils.getExtension(file.getResource().getFilename());

        try(InputStream is = file.getInputStream()) {
            Path filePath = uploadPath.resolve(replaceFileName);
            Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("File is not saved! : " + fileName, e);
        }

        return replaceFileName;
    }

    public static boolean deleteFile(String uploadDir, String fileName) throws IOException {
        boolean result = false;

        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)) result = true;

        try{
            Path filePath = uploadPath.resolve(fileName);
            Files.delete(filePath);
            result = true;
        } catch (IOException e) {
            throw new IOException("file is not deleted! : " + fileName, e);
        }

        return result;
    }
}
