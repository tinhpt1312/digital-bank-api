package org.tinhpt.digital.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.service.AwsS3Service;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
@Tag(name = "Aws S3", description = "Api Aws s3 upload file")
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    @PostMapping(value = "/upload-be", consumes = "multipart/form-data")
    public BankResponse uploadFile(@RequestParam("multipartFile")MultipartFile multipartFile){
        return awsS3Service.uploadFile(multipartFile);
    }

    @PostMapping(value = "/upload-fe")
    public BankResponse generatePresignedUrl(@RequestParam("name") String multipartFile){
        return awsS3Service.generatePresignedUrl(multipartFile);
    }

    @DeleteMapping("/delete")
    public BankResponse deletedFile(@RequestParam String fileName){
        return awsS3Service.deletedFile(fileName);
    }
}
