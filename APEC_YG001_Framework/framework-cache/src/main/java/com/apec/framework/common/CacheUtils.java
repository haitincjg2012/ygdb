package com.apec.framework.common;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public abstract class CacheUtils
{
    private static final Logger log = LoggerFactory.getLogger(CacheUtils.class);
    private static final String SEPARATOR = "_";

    /**
     * 根据多个因子生成key的前缀字符串
     * 
     * @param factors
     *        生成因子
     */
    public static String genKey(Object... factors)
    {
        return StringUtil.join(SEPARATOR, Lists.transform(Lists.newArrayList(factors), new Function<Object, Object>()
        {
            @Override
            public Object apply(Object factor)
            {
                checkNotNull(factor);
                if (factor instanceof Date)
                {
                    return ((Date) factor).getTime();
                }
                return factor;
            }
        }).toArray());
    }


    /**
     * 对象查询工厂
     * 
     * @param <K>
     *        查询key值
     * @param <V>
     *        查询结果
     */
    public abstract static class ObjectFactory<K, V>
    {
        public abstract V get(K key);
    }

    /**
     * 从非cache中查询出来后刷新cache处理器
     * 
     * @param <V>
     *        最新对象
     */
    public abstract static class FlushCacheCallback<V>
    {
        public abstract void flush(V obj);
    }




}
