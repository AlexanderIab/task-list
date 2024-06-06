package com.iablonski.crm.tasklist.service.impl;

import com.iablonski.crm.tasklist.domain.exception.ImageUploadException;
import com.iablonski.crm.tasklist.domain.task.TaskImage;
import com.iablonski.crm.tasklist.service.ImageService;
import com.iablonski.crm.tasklist.service.props.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public String upload(final TaskImage image) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new ImageUploadException(
                    "Image upload failed: " + e.getMessage());
        }
        MultipartFile multipartFile = image.file();
        if (multipartFile.isEmpty()
                || multipartFile.getOriginalFilename() == null) {
            throw new ImageUploadException("Image upload failed.");
        }
        String fileName = generateFilename(multipartFile);
        InputStream inputStream;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (Exception e) {
            throw new ImageUploadException(
                    "Image upload failed: " + e.getMessage());
        }
        saveImage(inputStream, fileName);
        return fileName;
    }

    @SneakyThrows
    private void saveImage(final InputStream inputStream,
                           final String fileName) {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .stream(inputStream,
                                inputStream.available(),
                                -1)
                        .bucket(minioProperties.getBucket())
                        .object(fileName)
                        .build()
        );
    }

    private String generateFilename(final MultipartFile multipartFile) {
        String extension = getExtension(multipartFile);
        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(final MultipartFile multipartFile) {
        return Objects.requireNonNull(multipartFile.getOriginalFilename())
                .substring(multipartFile
                        .getOriginalFilename().lastIndexOf(".") + 1);
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .build()
        );
        if (!found) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .build()
            );
        }
    }
}
