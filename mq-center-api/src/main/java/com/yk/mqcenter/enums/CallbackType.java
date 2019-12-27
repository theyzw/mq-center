package com.yk.mqcenter.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 回调类型
 *
 * @author yzw
 * @date 2019/07/18 11:45
 */
public enum CallbackType {

    /** dubbo */
    DUBBO(1, "dubbo"),
    /** rest */
    REST(2, "rest");

    private static Map<Integer, CallbackType> VALUE_MAP = new HashMap<Integer, CallbackType>();

    static {
        for (CallbackType p : CallbackType.values()) {
            VALUE_MAP.put(p.getValue(), p);
        }
        VALUE_MAP = Collections.unmodifiableMap(VALUE_MAP);
    }

    private int value;
    private String name;

    CallbackType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static CallbackType get(Integer value) {
        return VALUE_MAP.get(value);
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

}