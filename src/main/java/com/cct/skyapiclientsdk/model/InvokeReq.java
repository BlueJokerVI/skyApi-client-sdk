package com.cct.skyapiclientsdk.model;
import lombok.Data;

/**
 * @Author: cct
 * @Description: 调用包装类
 */
@Data
public class InvokeReq {
    /**
     * 接口id
     */
    Long interfaceId;

    /**
     * 请求参数
     */
    String requestParam;
}
