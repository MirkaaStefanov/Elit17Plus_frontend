package com.example.Elit17Plus_frontend.clients;

import com.example.Elit17Plus_frontend.config.FeignClientConfiguration;
import com.example.Elit17Plus_frontend.dtos.VisitDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "EP-visits", url = "${backend.base-url}/visits", configuration = FeignClientConfiguration.class)
public interface VisitClient {

    @PostMapping("/{patientId}")
    VisitDTO addVisit(@PathVariable UUID patientId, @RequestHeader("Authorization") String auth);

    @GetMapping("/getForPatient/{patientId}")
    List<VisitDTO> getForPatient(@PathVariable UUID patientId, @RequestHeader("Authorization") String auth);


}
