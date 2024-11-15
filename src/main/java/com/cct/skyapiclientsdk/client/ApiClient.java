package com.cct.skyapiclientsdk.client;


import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
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

    private static final String GATEWAY_HOST = "http://localhost:8080";

    private String accessKey;

    private String secretKey;

    private String uid;

    public ApiClient(String accessKey, String secretKey, String uid) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.uid = uid;
    }


    public String get(String number) {

        Map<String, List<String>> headers = getHeaders();
        String uri = "/api/test/get";
        String param = "number=" + number;
        String content = uri + "\n" + param;
        String sign = SignUtils.sign(content, secretKey);
        headers.put("sign", Collections.singletonList(sign));

        Map<String, Object> params = new HashMap<>();
        params.put("number", number);
        HttpResponse response = HttpRequest.get(GATEWAY_HOST + uri)
                .header(headers)
                .form(params)
                .execute();

        String res = response.body();
        return res;
    }


    public String post(String postReq) {

        Map<String, List<String>> headers = getHeaders();
        String uri = "/api/test/post";
        String param = JSONUtil.toJsonStr(postReq);
        String content = uri + "\n" + param;
        String sign = SignUtils.sign(content, secretKey);
        headers.put("sign", Collections.singletonList(sign));

        HttpResponse response = HttpRequest.post(GATEWAY_HOST + uri)
                .header(headers)
                .body(param)
                .execute();

        String res = response.body();
        return res;
    }

    private Map<String, List<String>> getHeaders() {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("accessKey", Collections.singletonList(accessKey));
        headers.put("nonce", Collections.singletonList(IdUtil.fastSimpleUUID()));
        headers.put("timestamp", Collections.singletonList(System.currentTimeMillis() + ""));
        headers.put("uid", Collections.singletonList(uid));
        return headers;
    }

}
