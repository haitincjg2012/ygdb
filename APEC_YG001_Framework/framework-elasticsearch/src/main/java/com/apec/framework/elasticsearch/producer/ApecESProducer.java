package com.apec.framework.elasticsearch.producer;

import com.alibaba.fastjson.TypeReference;
import com.apec.framework.common.Constants;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.elasticsearch.eslistener.ApecESResponseListener;
import com.apec.framework.elasticsearch.vo.*;
import com.apec.framework.log.InjectLogger;
import org.apache.http.StatusLine;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

/**
 * Title: ES Producer
 *     ***该Producer  不提供并发控制,使用ES推荐的部分更新操作
 * @author yirde  2017/7/10.
 */
@Component
public class ApecESProducer {

    @Autowired
    RestClient restClient;

    @InjectLogger
    private Logger logger;

    @Autowired
    ApecESResponseListener apecESResponseListener;

    /**
     * 异步推送ES信息
     * @param bodyJson Body Json
     */
    public ESPostResponseVO postESInfo(String indexUrl, String bodyJson) throws BusinessException{
          InputStream is = null;
          BufferedReader reader = null;
          try {
              Response indexResponse = restClient.performRequest(
                      ESProducerConstants.OPREATION_POST,   //Post操作
                      indexUrl,         //索引路径
                      Collections.<String, String>emptyMap(),
                      new NStringEntity(bodyJson, ContentType.APPLICATION_JSON)
              );
              logger.info("[ApecESProducer][Post] IndexResponse:{}", indexResponse.getStatusLine());
              is = indexResponse.getEntity().getContent();
              reader =  new BufferedReader(new InputStreamReader(is, Constants.SYSTEM_ENCODING),ESProducerConstants.BUFFER_SIZE);
              StringBuilder sb = new StringBuilder();
              String line;
              while ((line = reader.readLine()) != null){
                  sb.append(line).append("\n");
             }
              String resString = sb.toString();
              reader.close();
              is.close();

              return JsonUtil.parseObject(resString , ESPostResponseVO.class);
          }catch (Exception ex){
              if(reader != null){
                  try {
                      reader.close();
                  }catch (Exception ioEx){
                      logger.error("[ApecESProducer] [Post] BufferedReader Close failed! ",ioEx);
                  }
              }
              if(is != null){
                  try {
                      is.close();
                  }catch (Exception ioEx){
                      logger.error("[ApecESProducer][Post] InputStream Close failed! ",ioEx);
                  }
              }
              logger.error("[ApecESProducer] [Post] Failed ! bodyJson:{},indexUrl:{}",bodyJson,indexUrl,ex);
              throw new BusinessException(Constants.SYS_ERROR);
          }
    }

    /**
     * put 操作
     * @param indexUrl
     */
    public ESPostResponseVO putEsInfo(String indexUrl, String mapping) throws BusinessException{
        InputStream is = null;
        BufferedReader reader = null;
        try {
            Response indexResponse = restClient.performRequest(
                    ESProducerConstants.OPREATION_PUT,   //put操作
                    indexUrl,         //索引路径
                    Collections.<String, String>emptyMap(),
                    new NStringEntity(mapping, ContentType.APPLICATION_JSON)
            );
            logger.info("[ApecESProducer][putEsInfo] IndexResponse:{}", indexResponse.getStatusLine());
            is = indexResponse.getEntity().getContent();
            reader =  new BufferedReader(new InputStreamReader(is, Constants.SYSTEM_ENCODING),ESProducerConstants.BUFFER_SIZE);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
            String resString = sb.toString();
            reader.close();
            is.close();

            return JsonUtil.parseObject(resString , ESPostResponseVO.class);
        }catch (Exception ex){
            if(reader != null){
                try {
                    reader.close();
                }catch (Exception ioEx){
                    logger.error("[ApecESProducer] [putEsInfo] BufferedReader Close failed! ",ioEx);
                }
            }
            if(is != null){
                try {
                    is.close();
                }catch (Exception ioEx){
                    logger.error("[ApecESProducer][Put] InputStream Close failed! ",ioEx);
                }
            }
            logger.error("[ApecESProducer] [putEsInfo] Failed ! indexUrl:{}",indexUrl,ex);
            throw new BusinessException(Constants.SYS_ERROR);
        }
    }

    /**
     * 部分更新 按DOC 模式
     * **** 文档 合并
     * @param indexUrl 索引路径
     * @param indexId 文档ID
     * @param bodyJson 内容JSON
     * @return 是否更新成功
     */
    public boolean postESInfoForUpdateByDoc(String indexUrl,String indexId,Object bodyJson){

        ESPostResponseVO esPostResponseVO = postESInfoForUpdate(indexUrl,indexId,JsonUtil.toJSONString(ESUpdateDocRequestVO.getInstnce(bodyJson)));
        return esPostResponseVO != null;
    }

    /**
     * 部分更新 按Script模式
     * ***ES开放对象  ctx._source 取得该文档的下的字段
     * @param indexUrl 索引路径
     * @param indexId 文档ID
     * @param scriptJson 内容JSON
     * @return 是否更新成功
     */
    public boolean postESInfoForUpdateByScript(String indexUrl,String indexId ,String scriptJson){
        ESPostResponseVO esPostResponseVO = postESInfoForUpdate(indexUrl,indexId,JsonUtil.toJSONString(ESUpdateScriptRequestVO.getInstnce(scriptJson)));
        return esPostResponseVO != null;
    }

    /**
     * 部分更新  按DOC 进行对象合并   或者 按照 Script 对某一个字段的更新或叠加
     * 避免了多次请求的网络开销。通过减少检索和重建索引步骤之间的时间，我们也减少了其他进程的变更带来冲突的可能性
     * @param indexUrl 索引路劲
     * @param indexId 文档ID
     * @return
     */
     private ESPostResponseVO postESInfoForUpdate(String indexUrl,String indexId,String bodyJson){
         InputStream is = null;
         BufferedReader reader = null;
         try {
             Response indexResponse = restClient.performRequest(
                     ESProducerConstants.OPREATION_POST,   //Post操作
                     indexUrl + indexId + ESProducerConstants.OPREATION_POST_UPDATE,         //索引路径
                     Collections.<String, String>emptyMap(),
                     new NStringEntity(bodyJson, ContentType.APPLICATION_JSON)
             );
             logger.info("[ApecESProducer][Post Update] IndexResponse:{}", indexResponse.getStatusLine());
             is = indexResponse.getEntity().getContent();
             reader =  new BufferedReader(new InputStreamReader(is, Constants.SYSTEM_ENCODING),ESProducerConstants.BUFFER_SIZE);
             StringBuilder sb = new StringBuilder();
             String line;
             while ((line = reader.readLine()) != null){
                 sb.append(line).append("\n");
             }
             String resString = sb.toString();
             reader.close();
             is.close();

             return JsonUtil.parseObject(resString , ESPostResponseVO.class);
         }catch (ResponseException rex){
             if(reader != null){
                 try {
                     reader.close();
                 }catch (Exception ioEx){
                     logger.error("[ApecESProducer] [Post Update] BufferedReader Close failed! ",ioEx);
                 }
             }
             if(is != null){
                 try {
                     is.close();
                 }catch (Exception ioEx){
                     logger.error("[ApecESProducer][Post Update] InputStream Close failed! ",ioEx);
                 }
             }
             logger.info("[ApecESProducer][Post Update] Not Found ! indexUrl:{},indexId:{},response:{}",indexUrl,indexId ,rex.getResponse());
             return null;
         }catch (Exception ex){
             if(reader != null){
                 try {
                     reader.close();
                 }catch (Exception ioEx){
                     logger.error("[ApecESProducer] [Post Update] BufferedReader Close failed! ",ioEx);
                 }
             }
             if(is != null){
                 try {
                     is.close();
                 }catch (Exception ioEx){
                     logger.error("[ApecESProducer][Post Update] InputStream Close failed! ",ioEx);
                 }
             }
             logger.error("[ApecESProducer] [Post Update] Failed ! bodyJson:{},indexUrl:{}",bodyJson,indexUrl,ex);
             throw new BusinessException(Constants.SYS_ERROR);
         }
     }



    /**
     * 检测索引是否存在
     * @param indexUrl 索引的路劲
     * @param indexId 索引的ID
     * @return 是否存在 true 存在 false 不存在
     */
    public boolean headESInfo(String indexUrl,String indexId){
        try {
            Response indexResponse = restClient.performRequest(
                    ESProducerConstants.OPREATION_HEAD,   //Head操作
                    indexUrl + indexId,         //索引路径
                    Collections.<String, String>emptyMap()
            );

            logger.info("[ApecESProducer] [Head]IndexResponse:{}", indexResponse.getStatusLine());
            StatusLine statusLine = indexResponse.getStatusLine();
            return statusLine.getStatusCode() == ESProducerConstants.RESPONSE_CODE;
        }catch (Exception ex){
            logger.error("[ApecESProducer][Head] Failed ! indexUrl:{},indexId:{}",indexUrl,indexId ,ex);
            throw new BusinessException(Constants.SYS_ERROR);
        }
    }

    /**
     * 删除ES 信息
     * @param indexUrl 索引的URL
     * @param indexId 索引的ID
     * @return true 删除成功 false 删除失败
     */
    public boolean deleteESInfo(String indexUrl,String indexId){
        try {
            Response indexResponse = restClient.performRequest(
                    ESProducerConstants.OPREATION_DELETE,   //Head操作
                    indexUrl + indexId,         //索引路径
                    Collections.<String, String>emptyMap()
            );
            logger.info("[ApecESProducer] [Delete]IndexResponse:{}", indexResponse.getStatusLine());
            StatusLine statusLine = indexResponse.getStatusLine();
            return statusLine.getStatusCode() == ESProducerConstants.RESPONSE_CODE;
        }catch (ResponseException ex){
            logger.info("[ApecESProducer][Delete] Not Found ! indexUrl:{},indexId:{},response:{}",indexUrl,indexId ,ex.getResponse());
            Response indexResponse =  ex.getResponse();
            StatusLine statusLine = indexResponse.getStatusLine();

            return statusLine.getStatusCode() == ESProducerConstants.RESPONSE_CODE;
        }catch (Exception ex){
            logger.error("[ApecESProducer][Delete] Failed ! indexUrl:{},indexId:{}",indexUrl,indexId ,ex);
            throw new BusinessException(Constants.SYS_ERROR);
        }
    }

    /**
     * 检索单个文档, 未检索到，返回Null
     * @param <V> V VO对象
     * @param indexUrl 索引的路劲
     * @param indexId 索引的ID
     * @param clazz
     * @return ESGetSingleResponseVO 获取单个文档
     */
    public <V> ESGetSingleResponseVO<V> getSingleESInfoById(String indexUrl, String indexId, Class<V> clazz){
        InputStream is = null;
        BufferedReader reader = null;
        try {
            Response indexResponse = restClient.performRequest(
                    ESProducerConstants.OPREATION_GET,   //Head操作
                    indexUrl + indexId,         //索引路径
                    Collections.<String, String>emptyMap()
            );
            logger.info("[ApecESProducer] [Get Single] IndexResponse:{}", indexResponse.getStatusLine());
            is = indexResponse.getEntity().getContent();
            reader = new BufferedReader(new InputStreamReader(is, Constants.SYSTEM_ENCODING), ESProducerConstants.BUFFER_SIZE);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String resString = sb.toString();
            reader.close();
            is.close();

            ESGetSingleResponseVO<V> responseVO = JsonUtil.parseObject(resString, new TypeReference<ESGetSingleResponseVO<V>>() {
            });
            V data = JsonUtil.parseObject(responseVO.getSource().toString(), clazz);
            responseVO.setSource(data);
            return responseVO;
        }catch (ResponseException rex){
            if(reader != null){
                try {
                    reader.close();
                }catch (Exception ioEx){
                    logger.error("[ApecESProducer] [Get Single] BufferedReader Close failed! ",ioEx);
                }
            }
            if(is != null){
                try {
                    is.close();
                }catch (Exception ioEx){
                    logger.error("[ApecESProducer][Get Single] InputStream Close failed! ",ioEx);
                }
            }
            logger.info("[ApecESProducer][Get Single] Not Found ! indexUrl:{},indexId:{},response:{}",indexUrl,indexId ,rex.getResponse());
             return null;
        }catch (Exception ex){
            if(reader != null){
                try {
                    reader.close();
                }catch (Exception ioEx){
                    logger.error("[ApecESProducer] [Get Single] BufferedReader Close failed! ",ioEx);
                }
            }
            if(is != null){
                try {
                    is.close();
                }catch (Exception ioEx){
                    logger.error("[ApecESProducer][Get Single] InputStream Close failed! ",ioEx);
                }
            }
            logger.error("[ApecESProducer][Get Single] Failed ! indexUrl:{},indexId:{}",indexUrl,indexId ,ex);
            throw new BusinessException(Constants.SYS_ERROR);
        }
    }

    /**
     * //TODO
     * Query String 轻量检索
 *          name 字段中包含 mary 或者 john
     *      date 值大于 2014-09-10
     *      _all_ 字段包含 aggregations 或者 geo
     *    +name:(mary john) +date:>2014-09-10 +(aggregations geo)
     * Base 64转码
     *
     * 过滤 排序
     *
     * 动态条件 ，分页检索
     *  size 页大小 && from 从第几行开始
     */

    /**
     * Release Resources
     * TODO 测试了下，服务进程直接被关闭，PreDestroy并不会被触发
     */
    @PreDestroy
    public void releaseRestClient(){
        logger.info("=========================Release the Elasticsearch Client configuration =========================");
        try {
            restClient.close();
            logger.info("=========================Release the Elasticsearch Client Success =========================");
        }catch (Exception ex){
            logger.error("=========================Release the Elasticsearch Client Failed =========================",ex);
        }
    }

}
