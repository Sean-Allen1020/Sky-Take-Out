package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliOssConfiguration {

//    @Autowired
//    private AliOssProperties aliOssProperties;

    @Bean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {

        return new AliOssUtil(aliOssProperties.getEndpoint(),
                        aliOssProperties.getBucketName(),
                        aliOssProperties.getRegion());
//                        aliOssProperties.getAccessKeyId(),
//                        aliOssProperties.getAccessKeySecret());
    }
}
