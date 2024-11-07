package com.cct.skyapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;


/**
 * @BelongsProject: skyApi-backend
 * @Author: cct
 * @Description: 签名工具类
 */
public class SignUtils {

    private static final String GET = "GET";
    private static final String POST = "POST";

    public static String sign(String content, String secretKey) {
        return DigestUtil.md5Hex(content + secretKey);
    }

    public static boolean verify(String sign, String content, String secretKey) {
        return Objects.equals(sign, DigestUtil.md5Hex(content + secretKey));
    }

    public static String getSignContent(HttpServletRequest request) {
        // 获取请求 URI
        String uri = request.getRequestURI();

        // 根据请求方法处理
        if (GET.equalsIgnoreCase(request.getMethod())) {
            return uri + "\n" + getSortedQueryParams(request);
        } else if (POST.equalsIgnoreCase(request.getMethod())) {
            return uri + "\n" + getRequestBody(request);
        }

        // 其他请求方法的情况处理
        return null;
    }

    /**
     * 获取 GET 请求的排序后的参数字符串
     */
    private static String getSortedQueryParams(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        // 使用 TreeMap 进行字典序排序
        TreeMap<String, String> sortedParams = new TreeMap<>();
        parameterMap.forEach((key, values) -> sortedParams.put(key, values[0]));

        // 拼接参数字符串
        return sortedParams.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    /**
     * 获取 POST 请求的 JSON 数据
     */
    private static String getRequestBody(HttpServletRequest request) {
        StringBuilder jsonData = new StringBuilder();

        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON data from request", e);
        }
        return jsonData.toString();
    }

}
