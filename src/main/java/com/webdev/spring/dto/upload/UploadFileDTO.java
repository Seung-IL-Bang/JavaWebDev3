package com.webdev.spring.dto.upload;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class UploadFileDTO {

    private MultipartFile[] files;
}
