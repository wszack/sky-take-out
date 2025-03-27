package com.sky.utils;

import com.sky.properties.MinIoProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * MinIO 工具类，封装了常见的文件操作功能
 */
public class MinioUtil {

    @Autowired
    private MinIoProperties minIoProperties;

    /**
     * 上传 MultipartFile 文件到 MinIO
     *
     * @param image 上传的文件（MultipartFile类型）
     * @return 文件访问URL
     */
    public String uploadFile(MultipartFile image) {
        if (image == null) {
            throw new RuntimeException("image is null");
        }

        String bucketName = minIoProperties.getBucketName();
        String endPoint = minIoProperties.getEndPoint();
        String accessKey = minIoProperties.getAccessKey();
        String secretKey = minIoProperties.getSecretKey();

        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endPoint) // 替换为你的 MinIO 服务器地址
                    .credentials(accessKey, secretKey) // 替换为你的访问密钥和秘密密钥
                    .build();

            InputStream inputStream = image.getInputStream();

            // 拼接文件名
            String originalFilename = image.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String suffix = originalFilename.substring(index);
            String objectName = UUID.randomUUID().toString().replace("-", "");
            String fileName = objectName + suffix;

            // 上传文件到 MinIO
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, image.getSize(), -1)
                    .build());
            //获取文件访问URL
            // 返回文件路径
            String filePath = endPoint + "/" + bucketName + "/" + fileName;
            return filePath;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}