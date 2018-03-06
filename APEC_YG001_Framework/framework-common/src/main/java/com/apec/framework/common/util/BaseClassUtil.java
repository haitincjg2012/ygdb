package com.apec.framework.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 类 编 号：BL_PU6030202_ClassUtil
 * 类 名 称：BaseClassUtil
 * 内容摘要：类反射工具类
 * 完成日期：
 * 编码作者：
 * @author xxx
 */
public abstract class BaseClassUtil
{

    public static Class<?> getClass(String className) throws ClassNotFoundException
    {
        try
        {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            throw new ClassNotFoundException("not found class " + className);
        }
    }

    /**
     * 
     * Title: getAnnoFieldList
     * Description: TODO(获取所有有注释的字段,支持多重继承)
     * @param clazz obj
     * @return List<Field> 返回类型
     */
    @SuppressWarnings({"rawtypes", "unused"})
    private static List<Field> getAnnoFieldList(Class<?> clazz)
    {
        List<Field> list = new ArrayList<>();
        Class superClass = clazz.getSuperclass();
        while (true)
        {
            if(superClass != null)
            {
                Field[] superFields = superClass.getDeclaredFields();
                if(superFields != null && superFields.length > 0)
                {
                    for(Field field : superFields)
                    {
                        list.add(field);
                    }
                }
                superClass = superClass.getSuperclass();
            }
            else
            {
                break;
            }
        }
        Field[] objFields = clazz.getDeclaredFields();
        if(objFields != null && objFields.length > 0)
        {
            for(Field field : objFields)
            {

                list.add(field);

            }
        }
        return list;
    }

    /** 
    * 通过反射取到 List<T> 中 T 的类型 
    * @param clazz clazz
    * @param field field
    * @return  Type
    */
    public static Type getGenericFieldType(Class<?> clazz, Field field)
    {
        Type genericFieldType = field.getGenericType();
        if(genericFieldType instanceof ParameterizedType)
        {
            ParameterizedType aType = (ParameterizedType)genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            return fieldArgTypes[0];
        }
        return genericFieldType;
    }

    /** 
    * 通过反射取到 List<T> 中 T 的类型 
    * @param clazz  clazz
    * @param field  field
    * @return Type
    */
    public static Type getGenericFieldType(Class<?> clazz, Field field, int index)
    {
        Type genericFieldType = field.getGenericType();
        if(genericFieldType instanceof ParameterizedType)
        {
            ParameterizedType aType = (ParameterizedType)genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            return fieldArgTypes[index];
        }

        if(index > 0)
        {
            return null;
        }
        return genericFieldType;
    }
}
