package com.feilu.api.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EntityToMapConverter {
    public static Map<String, Object> convertToMap(Object entity) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(entity));
            } catch (IllegalAccessException e) {
                log.error("EntityToMapConverter error{}", e.getMessage());
            }
        }

        return map;
    }
}
