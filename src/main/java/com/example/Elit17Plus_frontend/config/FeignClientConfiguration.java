package com.example.Elit17Plus_frontend.config;

import com.example.Elit17Plus_frontend.exception.VisitException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Configuration
@Slf4j
public class FeignClientConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        final ObjectMapper mapper = new ObjectMapper();
        return (methodKey, response) -> {
            if (response.status() == HttpStatus.BAD_REQUEST.value()) {
                try (InputStream is = response.body().asInputStream()) {
                    // Read the JSON response body and parse it
                    Map<String, Object> errorDetails = mapper.readValue(is, Map.class);
                    String message = (String) errorDetails.get("message");
                    log.error("Received detailed error message from backend: {}", message);
                    return new VisitException(message);
                } catch (IOException e) {
                    log.error("Failed to read and parse error response body", e);
                    return new VisitException("Проблем : Не успях да прочета съобщението от сървъра.");
                }
            }
            return feign.FeignException.errorStatus(methodKey, response);
        };
    }
}

