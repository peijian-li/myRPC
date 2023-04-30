package com.example.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SerializerCode {

    JSON(0,"JSON序列化器");

    private final int code;
    private final String description;

}
