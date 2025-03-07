package org.tinhpt.digital.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.service.AwsS3Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucketName}")
    private String BUCKET_NAME;

    @Value("${aws.s3.url}")
    private String url;

    @Override
    public BankResponse uploadFile(MultipartFile multipartFile) {

        try{
            String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .contentType(multipartFile.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));

            return BankResponse.builder()
                    .responseCode("202")
                    .responseMessage(url + "/" + fileName)
                    .build();
        }catch (Exception e){
            throw new RuntimeException("Error uploading file to S3", e);
        }
    }

    @Override
    public BankResponse generatePresignedUrl(String name) {
        String uniqueFileName = UUID.randomUUID() + "-" + name;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(uniqueFileName)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();

        URL presignUrl = s3Presigner.presignPutObject(presignRequest).url();
        return BankResponse.builder()
                .responseCode("202")
                .responseMessage(presignUrl.toString())
                .build();
    }

    @Override
    public BankResponse deletedFile(String fileName) {
        try{

            s3Client.deleteObject(builder -> builder
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .build());

            return BankResponse.builder()
                    .responseCode("200")
                    .responseMessage("File deleted successfully: " + fileName)
                    .build();
        }catch (Exception e){
            throw new RuntimeException("Error deleting file from s3");
        }
    }


}
