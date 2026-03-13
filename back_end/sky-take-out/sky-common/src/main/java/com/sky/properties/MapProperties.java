package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "sky.map")
@Component
public class MapProperties {

    // 项目密钥
    private String ak;
    // 商家地址
    private String shopAddress;

    // 地理编码uri
    private String geocoding;
    // 路线规划uri
    private String driving;
}
