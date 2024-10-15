package com.kob.backend.controller;

import com.kob.backend.common.Result;
import com.kob.backend.utils.MinioUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Resource
    private MinioUtil minioUtil;

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
            e.printStackTrace();
            return Result.error("上传失败");
        }
    }

}
