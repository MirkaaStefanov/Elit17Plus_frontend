package com.example.Elit17Plus_frontend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoDTO {

    private Long id;

    private PatientDTO patient;

    private String key;

    private LocalDate uploadDate;

}
