package com.example.Elit17Plus_frontend.clients;

import com.example.Elit17Plus_frontend.dtos.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "EP-patients", url = "${backend.base-url}/patients")
public interface PatientClient {

    @GetMapping
    List<PatientDTO> getAll(@RequestHeader("Authorization") String auth);

    @GetMapping("/{id}")
    PatientDTO getPatientById(@PathVariable UUID id,  @RequestHeader("Authorization") String auth);

    @PostMapping("/add")
    PatientDTO createPatient(@RequestBody PatientDTO dto,  @RequestHeader("Authorization") String auth);

    @PutMapping("/update/{id}")
    PatientDTO update(@PathVariable UUID id, @RequestBody PatientDTO patientDTO, @RequestHeader("Authorization") String auth);

    @DeleteMapping("/delete/{id}")
    void deletePatient(@PathVariable UUID id,  @RequestHeader("Authorization") String auth);


    }
