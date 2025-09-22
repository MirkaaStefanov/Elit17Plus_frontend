package com.example.Elit17Plus_frontend.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingRequest implements Serializable {
    private String requestURI;
    private String method;
    private Map<String, String[]> parameters;
}
