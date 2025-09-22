package com.example.Elit17Plus_frontend.clients;

import com.example.Elit17Plus_frontend.dtos.BenefitDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "EP-benefits", url = "${backend.base-url}/benefits")
public interface BenefitClient {

    @GetMapping
    List<BenefitDTO> getAll(@RequestHeader("Authorization") String auth);

    @GetMapping("/byId")
    BenefitDTO getBenefitById(@RequestParam("id") UUID id, @RequestHeader(value = "Authorization", required = false) String auth);


    @PostMapping("/add")
    void createBenefit(@RequestBody BenefitDTO benefitDTO, @RequestHeader("Authorization") String auth);

    @PutMapping("/update/{id}")
    void updateBenefit(@PathVariable UUID id, @RequestBody BenefitDTO benefitDTO, @RequestHeader("Authorization") String auth);

    @PostMapping("/delete")
    void deleteBenefit(@RequestParam("id") UUID id, @RequestHeader("Authorization") String auth);

}
