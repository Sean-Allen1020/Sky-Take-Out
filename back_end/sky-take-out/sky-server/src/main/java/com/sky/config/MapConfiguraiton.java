package com.sky.config;

import com.sky.properties.MapProperties;
import com.sky.utils.MapUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapConfiguraiton {

    @Bean
    public MapUtil mapUtil(MapProperties mapProperties) {

        return new MapUtil(mapProperties.getAk()
                , mapProperties.getShopAddress()
                , mapProperties.getGeocoding()
                , mapProperties.getDriving());
    }
}
