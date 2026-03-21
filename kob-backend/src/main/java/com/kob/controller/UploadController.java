package com.kob.controller;

import com.kob.common.Result;
import com.kob.util.MinioUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    private final MinioUtil minioUtil;

    @PostMapping("/file")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 生成一个唯一的文件名
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // 构建完整的对象名，包括目录
            String objectName = "upload/images/" + fileName;

            // 上传文件
            String uploadedObjectName = minioUtil.uploadFile(file, objectName);

            // 返回上传成功的消息
            return Result.success(uploadedObjectName, "上传成功");
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.error("上传失败");
        }
    }

}
