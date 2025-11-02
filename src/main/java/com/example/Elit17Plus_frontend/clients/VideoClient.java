package com.example.Elit17Plus_frontend.clients;

import com.example.Elit17Plus_frontend.dtos.VideoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "EP-videos", url = "${backend.base-url}/videos")
public interface VideoClient {

    @PostMapping("/upload")
    VideoDTO uploadVideo(@RequestParam("patientId") UUID patientId, @RequestParam("key") String key, @RequestHeader("Authorization") String auth);

    @GetMapping("/{id}")
    List<VideoDTO> getAllForPatient(@PathVariable UUID id,  @RequestHeader("Authorization") String auth);


}
