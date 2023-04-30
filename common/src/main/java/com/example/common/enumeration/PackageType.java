package com.example.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PackageType {

    REQUEST_PACK(0,"包类型为请求体"),
    RESPONSE_PACK(1,"包类型为响应体");

    private final int code;
    private final String description;

}
