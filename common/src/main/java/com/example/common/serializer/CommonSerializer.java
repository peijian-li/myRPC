package com.example.common.serializer;

public interface CommonSerializer {

    Integer JSON_SERIALIZER = 0;

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
