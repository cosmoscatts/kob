package com.kob.backend.utils;

import io.minio.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    private static final int PART_SIZE = 5 * 1024 * 1024; // 5MB

    public String uploadFile(MultipartFile file, String objectName) throws Exception {
        String contentType = file.getContentType();
        InputStream inputStream = file.getInputStream();
        long size = file.getSize();

        if (size <= PART_SIZE) {
            // 小文件，直接上传
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, size, -1)
                    .contentType(contentType)
                    .build());
        } else {
            // 大文件，分片上传
            List<String> parts = new ArrayList<>();
            byte[] buffer = new byte[PART_SIZE];
            int partNumber = 1;
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                ByteArrayInputStream partInputStream = new ByteArrayInputStream(buffer, 0, bytesRead);
                ObjectWriteResponse response = minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName + "_part" + partNumber)
                                .stream(partInputStream, bytesRead, -1)
                                .build());
                parts.add(response.object());
                partNumber++;
            }

            // 合并分片
            String mergedObjectName = objectName;
            List<ComposeSource> sources = new ArrayList<>();
            for (String part : parts) {
                sources.add(
                        ComposeSource.builder()
                                .bucket(bucketName)
                                .object(part)
                                .build()
                );
            }

            minioClient.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket(bucketName)
                            .object(mergedObjectName)
                            .sources(sources)
                            .build()
            );

            // 删除分片
            for (String part : parts) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(part)
                                .build()
                );
            }
        }

        return objectName;
    }

    public InputStream downloadFile(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    public void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    public List<String> listFiles(String prefix) throws Exception {
        List<String> files = new ArrayList<>();
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(prefix)
                        .recursive(true)
                        .build()
        );
        for (Result<Item> result : results) {
            files.add(result.get().objectName());
        }
        return files;
    }
}
