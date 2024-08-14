package com.feilu.api.common.entity;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.StrKit;

import java.util.HashMap;
import java.util.Map;

public class Ret extends HashMap {
    private static final String STATE = "state";
    private static final String STATE_OK = "ok";
    private static final String STATE_FAIL = "fail";

    public Ret() {
    }

    public static Ret by(Object key, Object value) {
        return (new Ret()).set(key, value);
    }

    public static Ret create(Object key, Object value) {
        return (new Ret()).set(key, value);
    }

    public static Ret create() {
        return new Ret();
    }

    public static Ret ok() {
        return (new Ret()).setOk();
    }

    public static Ret ok(Object key, Object value) {
        return ok().set(key, value);
    }

    public static Ret fail() {
        return (new Ret()).setFail();
    }

    public static Ret fail(Object key, Object value) {
        return fail().set(key, value);
    }

    public Ret setOk() {
        super.put("state", "ok");
        return this;
    }

    public Ret setFail() {
        super.put("state", "fail");
        return this;
    }

    public boolean isOk() {
        Object state = this.get("state");
        if ("ok".equals(state)) {
            return true;
        } else if ("fail".equals(state)) {
            return false;
        } else {
            throw new IllegalStateException("调用 isOk() 之前，必须先调用 ok()、fail() 或者 setOk()、setFail() 方法");
        }
    }

    public boolean isFail() {
        Object state = this.get("state");
        if ("fail".equals(state)) {
            return true;
        } else if ("ok".equals(state)) {
            return false;
        } else {
            throw new IllegalStateException("调用 isFail() 之前，必须先调用 ok()、fail() 或者 setOk()、setFail() 方法");
        }
    }

    public Ret set(Object key, Object value) {
        super.put(key, value);
        return this;
    }

    public Ret setIfNotBlank(Object key, String value) {
        if (StrKit.notBlank(value)) {
            this.set(key, value);
        }

        return this;
    }

    public Ret setIfNotNull(Object key, Object value) {
        if (value != null) {
            this.set(key, value);
        }

        return this;
    }

    public Ret set(Map map) {
        super.putAll(map);
        return this;
    }

    public Ret set(Ret ret) {
        super.putAll(ret);
        return this;
    }

    public Ret delete(Object key) {
        super.remove(key);
        return this;
    }

    public <T> T getAs(Object key) {
        return (T) this.get(key);
    }

    public String getStr(Object key) {
        Object s = this.get(key);
        return s != null ? s.toString() : null;
    }

    public Integer getInt(Object key) {
        Number n = (Number)this.get(key);
        return n != null ? n.intValue() : null;
    }

    public Long getLong(Object key) {
        Number n = (Number)this.get(key);
        return n != null ? n.longValue() : null;
    }

    public Double getDouble(Object key) {
        Number n = (Number)this.get(key);
        return n != null ? n.doubleValue() : null;
    }

    public Float getFloat(Object key) {
        Number n = (Number)this.get(key);
        return n != null ? n.floatValue() : null;
    }

    public Number getNumber(Object key) {
        return (Number)this.get(key);
    }

    public Boolean getBoolean(Object key) {
        return (Boolean)this.get(key);
    }

    public boolean notNull(Object key) {
        return this.get(key) != null;
    }

    public boolean isNull(Object key) {
        return this.get(key) == null;
    }

    public boolean isTrue(Object key) {
        Object value = this.get(key);
        return value instanceof Boolean && (Boolean)value;
    }

    public boolean isFalse(Object key) {
        Object value = this.get(key);
        return value instanceof Boolean && !(Boolean)value;
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }

    public boolean equals(Object ret) {
        return ret instanceof Ret && super.equals(ret);
    }
}

