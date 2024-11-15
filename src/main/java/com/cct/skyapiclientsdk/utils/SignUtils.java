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
}
