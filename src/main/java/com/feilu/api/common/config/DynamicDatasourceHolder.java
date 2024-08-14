package com.feilu.api.common.config;


/**
 * @author dzh
 */
public class DynamicDatasourceHolder {

    // 使用 ThreadLocal 进行线程隔离
    private static final ThreadLocal<String> CURRENT_DATA_SOURCE = ThreadLocal.withInitial(() -> null);

    /**
     * 获取当前线程的数据源
     *
     * @return 当前线程的数据源
     */
    public static String getDataSource() {
        return CURRENT_DATA_SOURCE.get();
    }

    /**
     * 设置当前线程的数据源
     *
     * @param dataSourceKey 数据源的键
     */
    public static void setDataSource(String dataSourceKey) {
        CURRENT_DATA_SOURCE.set(dataSourceKey);
    }

    /**
     * 移除当前线程的数据源
     */
    public static void removeDataSource() {
        CURRENT_DATA_SOURCE.remove();
    }
}
