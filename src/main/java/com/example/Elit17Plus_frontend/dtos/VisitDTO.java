package com.example.Elit17Plus_frontend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitDTO {

    private UUID id;
    private LocalDate visitDate;
    private PatientDTO patient;

}
