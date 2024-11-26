package com.cct.skyapiclientsdk;

import com.cct.skyapiclientsdk.client.ApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cct
 * ApiClientSdkAutoConfig自动配置类
 */

@Data
@ConfigurationProperties(prefix = "sky-api.client")
@Configuration
public class ApiClientSdkAutoConfig {
    private String accessKey;
    private String secretKey;
    private String uid;
    private String url;

    @Bean
    public ApiClient apiClient() {
        return new ApiClient(accessKey, secretKey, uid, url);
    }
}
