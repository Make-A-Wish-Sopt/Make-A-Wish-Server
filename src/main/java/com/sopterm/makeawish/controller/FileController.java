package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
@SecurityRequirement(name = "Authorization")
@Tag(name = "Image 관련 API")
public class FileController {

    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${cloud.aws.bucket.image}")
    private String imageBucketName;
    @Value("${cloud.aws.bucket.path}")
    private String imageBucketPath;

    private final FileService fileService;

    @Operation(summary = "이미지 업로드를 위한 url 관련 API")
    @GetMapping("")
    public ResponseEntity<Map<String, String>> getPresignedUrl(
            @RequestParam String fileName
    ) {
        val response = fileService.getSignedUrl(activeProfile, imageBucketName, fileName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}