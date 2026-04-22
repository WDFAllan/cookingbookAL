package com.example.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/upload")
public class UploadController {

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    @PostMapping
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);

        String ext = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf("."));
        }

        String filename = UUID.randomUUID() + ext;
        Files.copy(file.getInputStream(), dir.resolve(filename));

        return ResponseEntity.ok(Map.of("url", "/uploads/" + filename));
    }
}
