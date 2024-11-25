package com.cct.skyapiclientsdk.utils;
import cn.hutool.crypto.digest.DigestUtil;
import java.util.Objects;


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
