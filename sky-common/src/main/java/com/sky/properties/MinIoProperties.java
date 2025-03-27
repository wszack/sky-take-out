package com.sky.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@Data
@Component
@ConfigurationProperties(prefix = "minio.oss")
public class MinIoProperties {
    private String endPoint;
    private String bucketName;
    private String accessKey;
    private String secretKey;

    public MinIoProperties() {
    }

    public MinIoProperties(String endPoint, String bucketName, String accessKey, String secretKey) {
        this.endPoint = endPoint;
        this.bucketName = bucketName;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 获取
     * @return endPoint
     */
    public String getEndPoint() {
        return endPoint;
    }

    /**
     * 设置
     * @param endPoint
     */
    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    /**
     * 获取
     * @return bucketName
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * 设置
     * @param bucketName
     */
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * 获取
     * @return accessKey
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * 设置
     * @param accessKey
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * 获取
     * @return secretKey
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * 设置
     * @param secretKey
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String toString() {
        return "MinIoProperties{endPoint = " + endPoint + ", bucketName = " + bucketName + ", accessKey = " + accessKey + ", secretKey = " + secretKey + "}";
    }
}
