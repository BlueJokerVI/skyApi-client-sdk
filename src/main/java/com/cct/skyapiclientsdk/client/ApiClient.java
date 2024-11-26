package com.cct.skyapiclientsdk.client;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.cct.skyapiclientsdk.model.req.GetReq;
import com.cct.skyapiclientsdk.model.req.PostReq;
import com.cct.skyapiclientsdk.model.resp.BaseResponse;
import com.cct.skyapiclientsdk.utils.SignUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 调用第三方接口的客户端
 *
 * @author cct
 */
public class ApiClient {

    private String accessKey;

    private String secretKey;

    private String uid;

    private String gatewayUrl;

    public ApiClient(String accessKey, String secretKey, String uid, String gatewayUrl) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.uid = uid;
        this.gatewayUrl = gatewayUrl;
    }


    public BaseResponse<String> getTest(GetReq req) {

        String uri = "/api/test/get";
        Map<String, List<String>> headers = getHeaders();
        String param = getMethodParamConvert(req);
        String content = uri + "\n" + param;
        String sign = SignUtils.sign(content, secretKey);
        headers.put("sign", Collections.singletonList(sign));
        Map<String, Object> params = paramToMap(param);
        HttpResponse response = HttpRequest.get(gatewayUrl + uri)
                .header(headers)
                .form(params)
                .execute();

        String res = response.body();
        return JSONUtil.toBean(res, new TypeReference<BaseResponse<String>>() {
        }, true);
    }


    public BaseResponse<String> postTest(PostReq postReq) {
        Map<String, List<String>> headers = getHeaders();
        String uri = "/api/test/post";
        String param = JSONUtil.toJsonStr(postReq, JSONConfig.create().setIgnoreNullValue(true));
        String content = uri + "\n" + param;
        String sign = SignUtils.sign(content, secretKey);
        headers.put("sign", Collections.singletonList(sign));
        HttpResponse response = HttpRequest.post(gatewayUrl + uri)
                .header(headers)
                .body(param)
                .execute();
        String res = response.body();
        return JSONUtil.toBean(res, new TypeReference<BaseResponse<String>>() {
        }, true);
    }

    /**
     * 针对GET请求query参数处理
     * 将getObject对象的每个字段和其值转成形如a=1&b=2的字符串
     */
    private String getMethodParamConvert(Object getObject) {
        // 如果对象为空，直接返回空字符串
        if (getObject == null) {
            return "";
        }

        Class<?> clazz = getObject.getClass();
        // 获取所有字段
        Field[] fields = clazz.getDeclaredFields();
        List<String> paramList = new ArrayList<>();
        for (Field field : fields) {
            try {
                // 设置访问权限，允许访问私有字段
                field.setAccessible(true);
                // 获取字段的值
                Object value = field.get(getObject);
                // 如果字段值非空，则添加到 paramList 中
                if (value != null && !value.toString().isEmpty()) {
                    paramList.add(field.getName() + "=" + value.toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // 将字段名和字段值组合成字符串，用 "&" 连接
        return String.join("&", paramList);
    }

    /**
     * 针对GET请求将处理好的query参数字符串转为MAP对象
     */
    private Map<String, Object> paramToMap(String queryStr) {
        if (StrUtil.isBlank(queryStr)) {
            return null;
        }
        String[] split = queryStr.split("&");
        Map<String, Object> params = new HashMap<>();
        for (String s : split) {
            String[] kv = s.split("=");
            params.put(kv[0], kv[1]);
        }
        return params;
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
