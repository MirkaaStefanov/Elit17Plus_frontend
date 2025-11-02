package com.example.Elit17Plus_frontend.cloudflare;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class R2Config {

    // 1. Inject the R2 properties from application.yml
    @Value("${r2.client.account-id}")
    private String accountId;

    @Value("${r2.client.access-key}")
    private String accessKey;

    @Value("${r2.client.secret-key}")
    private String secretKey;

    @Bean
    public S3Presigner s3Presigner() {

        // 2. Build the endpoint URL using the injected 'accountId' field
        String endpointUrl = "https://" + this.accountId + ".r2.cloudflarestorage.com";

        // 3. Create credentials using the injected fields
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                this.accessKey,
                this.secretKey
        );

        // 4. Build the presigner
        return S3Presigner.builder()
                .endpointOverride(URI.create(endpointUrl))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of("auto"))
                .build();
    }
}