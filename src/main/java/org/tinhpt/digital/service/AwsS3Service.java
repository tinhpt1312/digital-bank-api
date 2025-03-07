package org.tinhpt.digital.service;

import org.springframework.web.multipart.MultipartFile;
import org.tinhpt.digital.dto.response.BankResponse;

public interface AwsS3Service {
    BankResponse uploadFile(MultipartFile multipartFile);
    BankResponse generatePresignedUrl(String name);
}
