package org.stadium.adminapi.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;


@Service
public class MediaFileService {

    private Path root;
    @Value("${app.upload.path}")
    private String rootPath;

    public String uploadFile(MultipartFile file, String path) {
        String fileName= getFileName(file.getOriginalFilename());
        root = Paths.get(rootPath + path);
        try {
            if (!Files.exists(root))
                Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }

        try {
            Files.copy(file.getInputStream(), this.root.resolve(Objects.requireNonNull(fileName)), StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Stream<Path> getAllFiles(String path) {
        root = Paths.get(rootPath + path);
        try {
            return Files.walk(this.root, 1).filter(p -> !p.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public Resource getFile(String url) {
        try {
            Path file = Paths.get(rootPath + url);
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

    public boolean deleteFile(String url) {
        try {
            Files.delete(Paths.get(rootPath + url));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAllFiles(String path) {
        root = Paths.get(rootPath + path);
        try {
            FileSystemUtils.deleteRecursively(root);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    private String getFileName(String originalFilename) {
        String extension = ".png";

        if(originalFilename == null)
            return UUID.randomUUID() + extension;
        int index = originalFilename.lastIndexOf(".");

        if(index != -1) {
            extension = originalFilename.substring(index);
        }
        return UUID.randomUUID() + extension;
    }
}
