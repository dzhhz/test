package com.feilu.api.common.utils;

import java.math.BigDecimal;
import java.util.Map;

public class MapUtils {

    /**
     * 从 map 中根据 key 获取值并转换为 String。若值为 null，则返回 "null" 字符串。
     *
     * @param map  Map 对象
     * @param key  要获取的键
     * @return 转换后的 String 值
     */
    public static String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : "null";
    }

    /**
     * 从 map 中根据 key 获取值并转换为 Integer。若值为 null 或无法转换，则返回 null。
     *
     * @param map  Map 对象
     * @param key  要获取的键
     * @return 转换后的 Integer 值，或 null
     */
    public static Integer getIntegerValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                // 值无法转换为 Integer，返回 null
                return null;
            }
        }
        // 值为 null 或其他类型，返回 null
        return null;
    }

    /**
     * 从 map 中根据 key 获取值并转换为 int。若值为 null 或无法转换，则返回 0。
     *
     * @param map  Map 对象
     * @param key  要获取的键
     * @return 转换后的 int 值，或 0
     */
    public static int getIntValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                // 值无法转换为 int，返回 0
                return 0;
            }
        }
        // 值为 null 或其他类型，返回 0
        return 0;
    }


    /**
     * 从 map 中根据 key 获取值并转换为 long。若值为 null 或无法转换，则返回 0L。
     *
     * @param map  Map 对象
     * @param key  要获取的键
     * @return 转换后的 long 值，或 0L
     */
    public static Long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                // 值无法转换为 long，返回 0L
                return 0L;
            }
        }
        // 值为 null 或其他类型，返回 0L
        return 0L;
    }

    /**
     * 从 map 中根据 key 获取值并转换为 BigDecimal。若值为 null 或无法转换，则返回 null。
     *
     * @param map  Map 对象
     * @param key  要获取的键
     * @return 转换后的 BigDecimal 值，或 null
     */
    public static BigDecimal getBigDecimalValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Integer) {
            return BigDecimal.valueOf((Integer) value);
        } else if (value instanceof Long) {
            return BigDecimal.valueOf((Long) value);
        } else if (value instanceof String) {
            try {
                return new BigDecimal((String) value);
            } catch (NumberFormatException e) {
                // 值无法转换为 BigDecimal，返回 null
                return null;
            }
        }
        // 值为 null 或其他类型，返回 null
        return null;
    }
}
