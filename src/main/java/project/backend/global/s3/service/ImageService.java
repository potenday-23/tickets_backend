package project.backend.global.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String updateImage(MultipartFile file, String entityName, String columnName) {

        if (file == null) {
            throw new BusinessException(ErrorCode.IMAGE_UPLOAD_FAIL);
        }

        // File Path 설정
        String uploadFilePath = entityName + "/" + columnName; // 엔티티명.컬럼명 폴더가 만들어짐

        // File 이름 설정
        String uploadFileName = getUuidFileName(file);

        // Object MetaData
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) {
            String keyName = uploadFilePath + "/" + uploadFileName;

            // S3에 폴더 및 파일 업로드
            amazonS3Client.putObject(new PutObjectRequest(bucket, keyName, inputStream, metadata));

            return amazonS3Client.getUrl(bucket, keyName).toString();

        } catch(IOException e) {
            throw new BusinessException(ErrorCode.IMAGE_UPLOAD_FAIL);
        }
    }

    public String getUuidFileName(MultipartFile file) {
        String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("no name");
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

}
