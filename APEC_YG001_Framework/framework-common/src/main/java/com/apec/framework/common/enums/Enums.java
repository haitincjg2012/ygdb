package com.apec.framework.common.enums;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Title: Enums 工具类
 *
 * @author yirde  2017/4/6.
 */
public class Enums {

    private static final ConcurrentHashMap<Class, HashMap<String, Enum>> ENUM_CACHE = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<Class, List<?>> LIST_ALL_CACHE = new ConcurrentHashMap<>();

    /**
     * 根据任意Enum的Ordinal获取值
     * @param enumType  enumType
     * @param ordinal ordinal
     * @return T T
     */
    public static <T extends Enum<T> & BaseEnum> T getEnumByOrdinal(Class<T> enumType, int ordinal) {
        if (enumType == null || ordinal < 0) {
            return null;
        }

        //must be subclass of Enum
        if (!Enum.class.isAssignableFrom(enumType)) {
            throw new IllegalArgumentException("class " + enumType.getName() + " must be a subclass of Enum.");
        }

        //must be implementation of BaseEnum
        if (!BaseEnum.class.isAssignableFrom(enumType)) {
            throw new IllegalArgumentException("class " + enumType.getName() + " must be an implementation of BaseEnum.");
        }

        T[] items = enumType.getEnumConstants();

        if (items == null || items.length <= ordinal) {
            throw new IllegalArgumentException("Enum " + enumType.getName() + " don't have " + ordinal + " items");
        }

        return items[ordinal];
    }

    /**
     * get enum by key
     * @param enumType enumType
     * @param key key
     * @return <T> <T>
     */
    public static <T extends Enum<T> & BaseEnum> T getEnumByKey(Class<T> enumType, String key) {
        if (enumType == null || key == null) {
            return null;
        }

        //must be subclass of Enum
        if (!Enum.class.isAssignableFrom(enumType)) {
            throw new IllegalArgumentException("class " + enumType.getName() + " must be a subclass of Enum.");
        }

        //must be implementation of BaseEnum
        if (!BaseEnum.class.isAssignableFrom(enumType)) {
            throw new IllegalArgumentException("class " + enumType.getName() + " must be an implementation of BaseEnum.");
        }

        T result = null;
        Map<String, Enum> cache = ENUM_CACHE.get(enumType);
        if (cache == null) {
            EnumSet set = EnumSet.allOf(enumType);
            HashMap<String, Enum> newEnum = new HashMap<>(16);
            for (Object object : set) {
                T t = (T) object;
                newEnum.put(t.getKey(), t);
                if (t.getKey().equals(key)) {
                    result = t;
                }
            }
            ENUM_CACHE.putIfAbsent(enumType, newEnum);
        } else {
            result = (T) cache.get(key);
        }
        return result;
    }

    public static <T extends Enum<T> & BaseEnum> T getEnumByNameOrNull(Class<T> enumType, String name) {
        if (enumType == null || name == null) {
            return null;
        }

        //must be subclass of Enum
        if (!Enum.class.isAssignableFrom(enumType)) {
            throw new IllegalArgumentException("class " + enumType.getName() + " must be a subclass of Enum.");
        }

        //must be implementation of BaseEnum
        if (!BaseEnum.class.isAssignableFrom(enumType)) {
            throw new IllegalArgumentException("class " + enumType.getName() + " must be an implementation of BaseEnum.");
        }

        T result;
        try {
            result = T.valueOf(enumType, name);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return result;
    }

//    /**
//     * list an immutable view for enum values
//     *
//     * @param <T>
//     * @param enumType
//     * @return
//     */
//    public static <T extends Enum<T>> List<T> unmodifiableList(Class<T> enumType) {
//        if (enumType == null) {
//            return Collections.EMPTY_LIST;
//        }
//
//        //must be subclass of Enum
//        if (!Enum.class.isAssignableFrom(enumType)) {
//            throw new IllegalArgumentException("class " + enumType.getName() + " must be a subclass of Enum.");
//        }
//
//        List<T> result = (List<T>) LIST_ALL_CACHE.get(enumType);
//        if (result == null) {
//            result = new ArrayList<>();
//            EnumSet set = EnumSet.allOf(enumType);
//            for (Object object : set) {
//                T t = (T) object;
//                result.add(t);
//            }
//            result = Collections.unmodifiableList(result);
//              LIST_ALL_CACHE.putIfAbsent(enumType, result);
//        }
//
//        return result;
//    }
}
