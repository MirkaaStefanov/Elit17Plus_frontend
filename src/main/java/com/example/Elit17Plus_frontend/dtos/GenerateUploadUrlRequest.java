package com.example.Elit17Plus_frontend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateUploadUrlRequest {
    private String filename;
    private String contentType;
}
