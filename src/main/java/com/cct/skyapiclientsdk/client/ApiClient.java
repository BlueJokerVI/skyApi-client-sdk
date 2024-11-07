package com.cct.skyapiclientsdk.client;


import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.cct.skyapiclientsdk.utils.SignUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调用第三方接口的客户端
 *
 * @author cct
 */
public class ApiClient {

    private static final String GATEWAY_HOST = "http://localhost:8888";

    private String accessKey;

    private String secretKey;

    private String uid;

    public ApiClient(String accessKey, String secretKey, String uid) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.uid = uid;
    }

    public String getNum() {

        Map<String, List<String>> headers = getHeaders();
        String uri = "/test/getRandomNum";
        String content = uri + "\n";
        String sign = SignUtils.sign(content, secretKey);
        headers.put("sign",Collections.singletonList(sign));

        HttpResponse response = HttpRequest.get(GATEWAY_HOST + uri)
                .header(headers)
                .execute();

        String res = response.body();
        return res;
    }

    private Map<String, List<String>> getHeaders() {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("accessKey", Collections.singletonList(accessKey));
        headers.put("nonce", Collections.singletonList(IdUtil.fastSimpleUUID()));
        headers.put("timestamp", Collections.singletonList(System.currentTimeMillis() + ""));
        headers.put("uid",Collections.singletonList(uid));

        return headers;
    }

}
