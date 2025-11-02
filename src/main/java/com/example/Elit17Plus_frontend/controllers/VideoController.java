package com.example.Elit17Plus_frontend.controllers;

import com.example.Elit17Plus_frontend.clients.VideoClient;
import com.example.Elit17Plus_frontend.cloudflare.R2StorageService;
import com.example.Elit17Plus_frontend.dtos.GenerateUploadUrlRequest;
import com.example.Elit17Plus_frontend.dtos.PresignedUploadDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/video")
public class VideoController {

    private final VideoClient videoClient;
    private final R2StorageService r2StorageService;

    @PostMapping("/generate-upload-url")
    @ResponseBody // <-- Връщаме JSON
    public ResponseEntity<PresignedUploadDTO> generateUploadUrl(
            @RequestBody GenerateUploadUrlRequest requestBody, // <-- Очакваме JSON
            HttpServletRequest request) {

        // 1. Проверка за сигурност
        String role = (String) request.getSession().getAttribute("sessionRole");
        if (!"ADMIN".equals(role)) {
            // Връщаме 403 Forbidden, ако не е админ
            return ResponseEntity.status(403).build();
        }

        // 2. Извикваме сървиса, за да генерира URL и ключ
        try {
            PresignedUploadDTO presignedUploadDTO = r2StorageService.generatePresignedUploadUrl(
                    requestBody.getFilename(),
                    requestBody.getContentType()
            );
            // 3. Връщаме DTO-то (с URL-а и ключа) на JavaScript-а
            return ResponseEntity.ok(presignedUploadDTO);
        } catch (Exception e) {
            log.error("Failed to generate presigned upload URL", e);
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/upload")
    public String saveVideoMetadata(
            @RequestParam String key, // <-- Получаваме ключа от R2
            @RequestParam String patientId, // <-- Получаваме ID-то на пациента
            HttpServletRequest request) {

        // 1. Проверка за сигурност
        String token = (String) request.getSession().getAttribute("sessionToken");
        String role = (String) request.getSession().getAttribute("sessionRole");
        if (!"ADMIN".equals(role)) {
            return "redirect:/";
        }

        // 3. Извикваме Feign клиента, за да кажем на backend-а да запази метаданните
        try {
            // Увери се, че VideoClient.saveVideoMetadata очаква DTO и "Bearer " + token
            videoClient.uploadVideo(UUID.fromString(patientId), key, token);
        } catch (Exception e) {
            log.error("Failed to save video metadata to backend", e);
            // TODO: Може да добавим съобщение за грешка към модела
            // model.addAttribute("errorMessage", "Failed to save video metadata.");
        }

        // 4. Презареждаме страницата на пациента
        return "redirect:/patients/" + patientId;
    }



}
