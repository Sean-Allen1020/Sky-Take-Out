package com.sky.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.sky.properties.AliOssProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    private String endpoint;
    private String bucketName;
    private String region;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    public String upload(MultipartFile file) throws com.aliyuncs.exceptions.ClientException {

        // 1. 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 2. 组装文件名，也就是上传后的文件名，自定义即可
        String originalFile = file.getOriginalFilename();
        String extension = "";
        //判定原文件是否有"."
        if (originalFile != null) {
            int dot = originalFile.lastIndexOf(".");
            if (dot != -1 && dot != originalFile.length() - 1) {
                extension = originalFile.substring(dot);
            }
        }
        String fileName = UUID.randomUUID() + extension;

        // 3. 可选：生成文件夹名，这里用日期
        String dir = "sky-take-out/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        // 4. 组装object名
        String objectName = dir + "/" + fileName;

        // 创建OSSClient实例。
        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
        // 5. 获取文件的inputStream，用这种try()书写的话，可以省略 .close()关闭流的代码，并且会自动在正确的时机关闭
        try (InputStream fileInputStream = file.getInputStream()) {

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, fileInputStream);

            // 上传文件。
            ossClient.putObject(putObjectRequest);

        } catch (OSSException | ClientException | IOException e) {
            throw new ServiceException("OSS upload failed: " + e.getMessage(), e);

        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        // 6. 返回云端文件路径url，让前端进行渲染
        String[] split = endpoint.split("//");
        String url = split[0] + "//" + bucketName + "." + split[1] + "/" + objectName;
        log.info("文件上传到:{}", url);
        return url;

    }
}
