package com.apec.framework.aop.lock;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.apec.framework.aop.annotation.MethodLock;
import com.apec.framework.aop.annotation.LockParam;

/**
 * 分布式锁方法级拦截
 * @author wjw 2016-12-16
 *
 */
@Aspect
@Component
public class MethodBlock
{
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    private static Logger logger = Logger.getLogger(MethodBlock.class);

    private static String CACHE_KEY_LOCK_PREFIX = "cache_key_lock_prex_";

    private long timeOut = 600000;

    @Around("execution(* com.apec..service..*.*(..))")
    public Object lock(ProceedingJoinPoint point) throws Throwable
    {
        Signature signature = point.getSignature();
        Method method = ((MethodSignature)signature).getMethod();
        
        if(method.isAnnotationPresent(MethodLock.class))
        {
            MethodLock anno = method.getAnnotation(MethodLock.class);
            String cacheKeyPrefix = anno.value();
            boolean findAnno=false;
            Object[] args = point.getArgs();
            Parameter[] parameters = method.getParameters();
            String lockKey="";
            
            //偿试去找加注解的参数,只要找到一个就把它当作是key
            if(parameters.length>0)
            {
                for(int i=0;i<parameters.length;i++)
                {
                    Parameter param=parameters[i];
                    if(param.isAnnotationPresent(LockParam.class))
                    {
                        findAnno=true;
                        lockKey=String.valueOf(args[i]);
                        break;
                    }
                }
            }
            
            //在没有找到注解时,如果第一个参数是string,int,long也可以被认定是key
            if(findAnno ||( args.length > 0 && (args[0] instanceof String || args[0] instanceof Integer || args[0] instanceof Long)))
            {
                if(!findAnno)
                {
                    lockKey = String.valueOf(args[0]);
                }
                
                lockKey=cacheKeyPrefix+lockKey;
                //分布式环境采用redis机制
                if(null != redisTemplate)
                {
                    String cacheKey = CACHE_KEY_LOCK_PREFIX+lockKey;
                    Object retVal = null;
                    if(1 == redisTemplate.opsForValue().increment(cacheKey, 1L))
                    {
                        try
                        {
                            //第一个进来的,设定好过期时间
                            redisTemplate.expire(cacheKey, timeOut, TimeUnit.MILLISECONDS);
                            retVal = point.proceed();
                        }
                        catch (Exception e)
                        {
                            if(e instanceof RuntimeException)
                            {
                                throw e;
                            }
                            throw new RuntimeException(e.getMessage(), e);
                        }
                        finally
                        {
                            //没有业务缓存键,此时仅仅去除加锁
                            if(cacheKeyPrefix.isEmpty())
                            {
                                redisTemplate.delete(cacheKey);
                            }
                            
                            //有业务缓存,处理完之后,清理所有相关的key,不仅仅是lock的key
                            else
                            {
                                Set<String> keys = redisTemplate.keys(lockKey+"_*");
                                keys.add(cacheKey);
                                redisTemplate.delete(keys);
                            }
                        }
                    }
                    
                    //后面进来的都会得到异常
                    else
                    {
                        logger.warn(lockKey+" Locked now...");
                        throw new RuntimeException("正在处理中,请勿重复提交");
                    }
                    return retVal;
                }
                
                //单机应用的只能采用同步机制
                else
                {
                    synchronized (this)
                    {
                       return point.proceed();
                    }
                }
            }
        }
        return point.proceed();
    }
}