package com.company.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AttachService {

    public String saveFile(MultipartFile file) {
        File folder = new File("uploads");
        if (!folder.exists()) {
            folder.mkdir();
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get("uploads/" + file.getOriginalFilename()); // uploads/zari.jpg
            Files.write(path, bytes);
            return file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] loadAttach(String fileName) throws IOException {
        byte[] imageInByte;

        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new File("uploads/" + fileName));
        } catch (Exception e) {
            return new byte[0];
        }
        int lastIndex = fileName.lastIndexOf(".");
        String extension = fileName.substring(lastIndex + 1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(originalImage, extension, baos);

        baos.flush();
        imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    public Resource load(String fileName) {
        try {
            Path file = Paths.get("uploads/" + fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

}
