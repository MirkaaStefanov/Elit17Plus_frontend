package com.example.Elit17Plus_frontend.cloudflare;

import com.example.Elit17Plus_frontend.dtos.PresignedUploadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class R2StorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${r2.bucket-name}")
    private String bucketName;

    public String uploadVideo(MultipartFile file) throws IOException {
        // 1. Generate a unique key for the file
        String key = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 2. Create a PutObjectRequest
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        // 3. Send the file to R2
        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return key;
    }


    public String getPresignedVideoUrl(String key) {
        // 1. Create a GetObjectRequest
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // 2. Create a GetObjectPresignRequest
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // Set how long the link is valid
                .getObjectRequest(getObjectRequest)
                .build();

        // 3. Generate the URL
        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

        // 4. Return the URL to the client
        return presignedRequest.url().toString();
    }

    public PresignedUploadDTO generatePresignedUploadUrl(String filename, String contentType) {
        // 1. Генерираме уникален ключ
        String key = UUID.randomUUID().toString() + "_" + filename;

        // 2. Създаваме заявка за PUT (качване)
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        // 3. Създаваме заявка за подписване
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // Линкът за качване е валиден 10 минути
                .putObjectRequest(putObjectRequest)
                .build();

        // 4. Генерираме подписания URL
        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(presignRequest);

        String uploadUrl = presignedPutObjectRequest.url().toString();

        // 5. Връщаме URL-а и ключа на контролера
        return new PresignedUploadDTO(uploadUrl, key);
    }
}
