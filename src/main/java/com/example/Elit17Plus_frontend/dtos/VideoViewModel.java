package com.example.Elit17Plus_frontend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoViewModel {

    private Long id;
    private LocalDate uploadDate;
    private String presignedUrl; // Временният, сигурен URL за <video> тага
    private String patientName; // (Полезно, ако искаш да покажеш името)

}
