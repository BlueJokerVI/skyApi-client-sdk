package com.cct.skyapiclientsdk;

import com.cct.skyapiclientsdk.client.ApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author cct
 * @description ApiClientSdkAutoConfig自动配置类
 */

@Data
@ConfigurationProperties(prefix = "sky-api.client")
@Component
public class ApiClientSdkAutoConfig {
    private String accessKey;
    private String secretKey;
    private String uid;

    @Bean
    public ApiClient apiClient() {
        return new ApiClient(accessKey, secretKey, uid);
    }
}
