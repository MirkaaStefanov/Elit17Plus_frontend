package com.example.Elit17Plus_frontend.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDTO {

    private UUID id;
    private String egn; // ЕГН
    private String name;
    private String surname;
    private LocalDate lastVisitDate;
    private List<BenefitDTO> benefits = new ArrayList<>();
    private String image;
    private String description;
    @JsonIgnore
    private transient MultipartFile imageFile;
    @JsonIgnore
    private transient List<UUID> benefitIds;
}
