//package com.apec.framework.cache.service;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.TreeSet;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.apec.framework.cache.CacheException;
//import com.apec.framework.cache.CacheService;
//
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisCluster;
//import redis.clients.jedis.JedisPool;
//
//@Service("cacheService")
//public class RedisClusterCacheServiceImpl implements CacheService
//{
//
//    @Autowired
//    private JedisCluster jedisCluster;
//    
//    public void add(String key, String value, int minutes) throws CacheException{
//        try{
////            jedisCluster.set(key, value);
////            jedisCluster.expire(key, minutes*60);
//            jedisCluster.setex(key, minutes*60, value);
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
//        }
//    }
//
//    public void add(String key, String value) throws CacheException{
//        try{
//            jedisCluster.set(key, value);
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
//        }
//    }
//
//    public void addList(String key, Collection<String> values) throws CacheException{
//        try{
//            if (values != null && values.size() > 0){
//                remove(key);
//                String [] str = values.toArray(new String[values.size()]);
//                jedisCluster.lpush(key, str);
//            }
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
//        }
//        
//    }
//
//    public void addList(String key, Collection<String> values, int minutes) throws CacheException{
//        try{
//            if (values != null && values.size() > 0){
//                remove(key);
//                String [] str = values.toArray(new String[values.size()]);
//                jedisCluster.lpush(key, str);
//                jedisCluster.expire(key, minutes*60);
//            }
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
//        }
//    }
//
//    public void addOneToList(String key, String value) throws CacheException{
//        try{
//            jedisCluster.lpush(key, value);
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
//        }
//    }
//
//    public String get(String key) throws CacheException{
//        try{
//            return jedisCluster.get(key);
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
//        }
//    }
//
//    public List<String> getList(String key) throws CacheException{
//        try{
//            String obj = jedisCluster.get(key);
//            JSONArray tem = JSONObject.parseArray(obj);
//            List<String> result = new ArrayList<String>();
//            for(int i = 0; i < tem.size(); i++){
//                result.add(tem.getString(i));
//            }
//            return result;
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
//        }
//    }
//
//    public String getListFirstOne(String key) throws CacheException{
//        try{
//            List<String> le = jedisCluster.lrange(key, 0, 1);
//            if (le != null && le.size() > 0){
//                return le.get(0);
//            } else{
//                return null;
//            }
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
//        }
//    }
//
////    public long getCountLike(String keyPrefix) throws CacheException{
////        if(StringUtils.isEmpty(keyPrefix))
////            return 0;
////        try{
////            Set<String> set = jedisCluster.hkeys(keyPrefix + "*");
////            return set.size();
////        } catch (Exception ex){
////            ex.printStackTrace();
////            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
////        }
////    }
//
//    public void remove(String key) throws CacheException{
//        try{
//            jedisCluster.del(key);
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
//        }
//    }
//
////    public void removeLike(String keyPrefix) throws CacheException{
////        if(!StringUtils.isEmpty(keyPrefix)){
////            try{
////                Set<String> set = jedisCluster.hkeys(keyPrefix + "*");
////                for(String key:set){
////                    jedisCluster.del(key);
////                }
////            } catch (Exception ex){
////                ex.printStackTrace();
////                throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
////            }
////        }
////    }
//
//
//    public Long increment(String key, Long value){
//        try{
//            return jedisCluster.incrBy(key, value);
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
//        }
//    }
//
//    public Long getIncrValue(String key) throws CacheException{
//        try{
//            return jedisCluster.incrBy(key, 0);
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
//        }
//    }
//
//    public void expire(String key, int minutes) throws CacheException{
//        try{
//            jedisCluster.expire(key, minutes*60);
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
//        }
//    }
//
//    public boolean exists(String key){
//        try{
//            return jedisCluster.exists(key);
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
//        }
//    }
//
//    public long getCountLike(String keyPrefix) throws CacheException{
//        Set<String> set = new TreeSet<String>();
//        try{
//            Map<String, JedisPool> map = jedisCluster.getClusterNodes();
//            for(Map.Entry<String, JedisPool> entry : map.entrySet()){
//                JedisPool jp = map.get(entry.getKey());
//                Jedis jedis = jp.getResource();
//                try{
//                    set.addAll(jedis.keys(keyPrefix + "*"));
//                }catch (Exception e){
//                    e.printStackTrace();
//                    throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
//                }finally{
//                    jedis.close();
//                }
//            }
//            return set.size();
//        }catch (Exception e){
//            e.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
//        }
//    }
//
//    public void removeLike(String keyPrefix) throws CacheException{
//        Set<String> set = new TreeSet<String>();
//        try{
//            Map<String, JedisPool> map = jedisCluster.getClusterNodes();
//            for(Map.Entry<String, JedisPool> entry : map.entrySet()){
//                JedisPool jp = map.get(entry.getKey());
//                Jedis jedis = jp.getResource();
//                try{
//                    set.addAll(jedis.keys(keyPrefix + "*"));
//                }catch (Exception e){
//                    e.printStackTrace();
//                    throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
//                }finally{
//                    jedis.close();
//                }
//            }
//            for(String key:set){
//                remove(key);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
//        }
//    }
//
//    public void expireSecond(String key, int second) throws CacheException{
//        try{
//            jedisCluster.expire(key, second);
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
//        }
//    }
//}
