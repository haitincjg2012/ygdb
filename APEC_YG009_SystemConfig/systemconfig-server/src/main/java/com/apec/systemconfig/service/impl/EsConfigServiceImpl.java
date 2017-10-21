package com.apec.systemconfig.service.impl;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.StringUtil;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.EsConfigStatus;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.elasticsearch.producer.ApecESProducer;
import com.apec.framework.elasticsearch.producer.ESProducerConstants;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.systemconfig.dao.EsConfigDao;
import com.apec.systemconfig.dto.AliasIndexDTO;
import com.apec.systemconfig.dto.EsConfigDTO;
import com.apec.systemconfig.model.EsConfig;
import com.apec.systemconfig.model.QEsConfig;
import com.apec.systemconfig.service.EsConfigService;
import com.apec.systemconfig.service.impl.callback.ReindexJobFinishTask;
import com.apec.systemconfig.util.SnowFlakeKeyGen;
import com.apec.systemconfig.vo.EsConfigVO;
import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by wubi on 2017/9/22.
 */
@Service
public class EsConfigServiceImpl implements EsConfigService {

    @Autowired
    private SnowFlakeKeyGen idGen;

    @Autowired
    private EsConfigDao esConfigDao;

    @Autowired
    private ApecESProducer apecESProducer;

    @Autowired
    private SpringCloudClient springCloudClient;

    @Autowired
    private ReindexJobFinishTask reindexJobFinishTask;

    @InjectLogger
    private Logger logger;


    @Override
    public String addConfig(EsConfigVO esConfigVO, String userId) {

        //校验参数
        if (StringUtils.isBlank(esConfigVO.getIndexAlias())
                || StringUtils.isBlank(esConfigVO.getIndexName())
                || StringUtils.isBlank(esConfigVO.getIndexType())
                || StringUtils.isBlank(esConfigVO.getMappingStr())) {
            logger.warn("[EsConfigServiceImpl][addConfig] Can't save New EsConfig , Parameter verification failed!");
            return Constants.COMMON_MISSING_PARAMS;
        }

        //新增ES索引
        boolean exist = apecESProducer.headESInfo(esConfigVO.getIndexName(), ""); //只判断索引
        if (exist) {
            logger.warn("[EsConfigServiceImpl][addConfig] Can't save New EsConfig , create index exist!");
            return Constants.COMMON_IS_EXIST;
        }
        logger.info("create index :{}", esConfigVO.getIndexName());
        apecESProducer.putEsInfo(esConfigVO.getIndexName(), "");
        String indexUrl;
        //创建类型
        indexUrl = esConfigVO.getIndexName() + ESProducerConstants.OPREATION_MAPPING + Constants.SINGLE_SLASH + esConfigVO.getIndexType();
        logger.info("create type :{}", indexUrl);
        apecESProducer.putEsInfo(indexUrl, esConfigVO.getMappingStr());
        //创建别名
        logger.info("create alias :{}", indexUrl);
        indexUrl = esConfigVO.getIndexName() + ESProducerConstants.OPREATION_ALIAS + Constants.SINGLE_SLASH + esConfigVO.getIndexAlias();
        apecESProducer.putEsInfo(indexUrl, "");

        //保存数据库
        EsConfig config = new EsConfig();
        BeanUtil.copyPropertiesIgnoreNullFilds(esConfigVO, config);
        config.setId(idGen.nextId());
        config.setEnableFlag(EnableFlag.Y);
        config.setCreateDate(new Date());
        config.setCreateBy(userId);
        config.setStatus(EsConfigStatus.USED.getKey());
        config.setVersion(1);
        esConfigDao.save(config);


        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String updateConfigForReIndex(EsConfigVO esConfigVO, String userId) {
        //校验参数
        if (null == esConfigVO.getId()
                || StringUtils.isBlank(esConfigVO.getMappingStr())
                ) {
            logger.warn("[EsConfigServiceImpl][updateConfigForReIndex] Can't reIndex for EsConfig , Parameter verification failed!");
            return Constants.COMMON_MISSING_PARAMS;
        }
        EsConfig esConfig = esConfigDao.findOne(esConfigVO.getId());
        if (StringUtils.isBlank(esConfigVO.getNewIndexName())) {
            esConfigVO.setVersion(esConfig.getVersion());
            esConfigVO.setIndexAlias(esConfig.getIndexAlias());
            esConfigVO.setIndexType(esConfig.getIndexType());
            esConfigVO.setIndexName(esConfig.getIndexName());
            genNewIndex(esConfigVO);
        }
        //判断新建的索引是否存在
        boolean exist = apecESProducer.headESInfo(esConfigVO.getNewIndexName(), "");
        if (exist) {
            logger.warn("[EsConfigServiceImpl][updateConfigForReIndex] Can't reIndex for EsConfig , index {} is exist!", esConfigVO.getNewIndexName());
            return Constants.COMMON_IS_EXIST;
        }
        //创建新的index
        logger.info("create index :{}", esConfigVO.getNewIndexName());
        apecESProducer.putEsInfo(esConfigVO.getNewIndexName(), "");
        String indexUrl;
        //创建类型
        indexUrl = esConfigVO.getNewIndexName() + ESProducerConstants.OPREATION_MAPPING + Constants.SINGLE_SLASH + esConfigVO.getIndexType();
        logger.info("create type :{}", indexUrl);
        apecESProducer.putEsInfo(indexUrl, esConfigVO.getMappingStr());



        //从数据库推送数据到ES 调用不同的服务
        if (StringUtils.isNotBlank(esConfigVO.getServerName())
                && StringUtils.isNotBlank(esConfigVO.getMethodName())) {
            Map<String, String> esPushMap = new HashMap<>();
            indexUrl = esConfigVO.getNewIndexName() + Constants.SINGLE_SLASH + esConfigVO.getIndexType();
            esPushMap.put("indexUrl", indexUrl);
            logger.info("indexUrl:{}", indexUrl);
            callServer(esConfigVO.getServerName(), esConfigVO.getMethodName(), esPushMap);
        }

        //创建一条新的记录到数据库中 标记为已使用
        EsConfig newEsConfig = new EsConfig();
        BeanUtil.copyPropertiesIgnoreNullFilds(esConfig, newEsConfig);
        newEsConfig.setId(idGen.nextId());
        newEsConfig.setEnableFlag(EnableFlag.Y);
        newEsConfig.setCreateDate(new Date());
        newEsConfig.setCreateBy(userId);
        newEsConfig.setStatus(EsConfigStatus.USED.getKey());
        newEsConfig.setVersion(newEsConfig.getVersion() + 1);
        newEsConfig.setIndexName(esConfigVO.getNewIndexName());
        newEsConfig.setMappingStr(esConfigVO.getMappingStr());
        esConfigDao.save(newEsConfig);
        //修改数据库中的记录状态为未使用
        esConfig.setStatus(EsConfigStatus.NOT_USED.getKey());
        esConfigDao.saveAndFlush(esConfig);

        //删除别名和旧索引关系
        indexUrl = esConfigVO.getIndexName() + ESProducerConstants.OPREATION_ALIAS + Constants.SINGLE_SLASH + esConfigVO.getIndexAlias();
        logger.info("delete alias :{}", indexUrl);
        Map<String, List<Object>> actionMap = new HashMap<>();
        List<Object> actionList = new ArrayList();
        Map<String, AliasIndexDTO> removeAliasMap = new HashMap<>();
        AliasIndexDTO removeAlias = new AliasIndexDTO(esConfigVO.getIndexName(), esConfigVO.getIndexAlias());
        Map<String, AliasIndexDTO> addAliasMap = new HashMap<>();
        AliasIndexDTO addAlias = new AliasIndexDTO(esConfigVO.getNewIndexName(), esConfigVO.getIndexAlias());
        removeAliasMap.put("remove", removeAlias);
        addAliasMap.put("add", addAlias);
        actionList.add(removeAliasMap);
        actionList.add(addAliasMap);
        actionMap.put("actions", actionList);
        String jsonStr = JsonUtil.toJSONString(actionMap);
        logger.info("reindex script :{}", jsonStr);
        apecESProducer.postESInfo(ESProducerConstants.OPREATION_ALIASES, jsonStr);

        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional
    public String deleteConfig(EsConfigVO esConfigVO, String userId) {
        //校验参数
        if (null == esConfigVO.getId() || StringUtils.isBlank(esConfigVO.getIndexName())
                || StringUtils.isBlank(esConfigVO.getIndexAlias())) {
            logger.warn("[EsConfigServiceImpl][deleteConfig] Can't delete EsConfig , Parameter verification failed!");
            return Constants.COMMON_MISSING_PARAMS;
        }

        //新增ES索引
        boolean exist = apecESProducer.headESInfo(esConfigVO.getIndexName(), ""); //只判断索引
        if (!exist) {
            logger.warn("[EsConfigServiceImpl][deleteConfig] Can't delete EsConfig , delete index is not exist!");
            return Constants.DATA_ISNULL;
        }

        EsConfig esConfig = esConfigDao.findOne(esConfigVO.getId());
        EsConfigStatus configStatus = EsConfigStatus.valueOf(esConfigVO.getStatus());
        if (EsConfigStatus.USED.compareTo(configStatus) == 0) {
            logger.warn("[EsConfigServiceImpl][deleteConfig] Can't delete EsConfig , delete index is used!");
            return Constants.COMMON_IS_EXIST;//已经被使用 不能删除
        }

        //删除索引
        apecESProducer.deleteESInfo(esConfigVO.getIndexType(), "");

        //删除数据库记录 软删除
        esConfig.setEnableFlag(EnableFlag.N);
        esConfigDao.saveAndFlush(esConfig);

        return Constants.RETURN_SUCESS;
    }

    @Override
    @Async
    public void reIndexJob() throws Throwable{
        ResultData<String> jobResult = new ResultData<>();
        jobResult.setErrorCode(Constants.RETURN_SUCESS);
        long startTime = System.currentTimeMillis();
        try {
            //获取所有需要重建的索引
            List<EsConfig> configList = esConfigDao.findByStatusAndEnableFlag(EsConfigStatus.USED.getKey(), EnableFlag.Y);
            EsConfigVO vo;
            for (EsConfig esConfig : configList) {
                //根据规则设置新的索引名称
                vo = new EsConfigVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(esConfig, vo);
                genNewIndex(vo);
                //重建索引
                this.updateConfigForReIndex(vo, vo.getCreateBy());
            }
        } catch (Throwable e) {
            jobResult.setErrorCode(Constants.SYS_ERROR);
            jobResult.setErrorMsg(e.getMessage());
            AsyncResult<ResultData> result = new AsyncResult<>(jobResult);
            //新增回调方法处理job执行结果
            result.addCallback(reindexJobFinishTask);
            throw e;//抛出异常保持事务一致性
        }
        logger.info("======================reindex job execute success============================");
        logger.info("reindex job execute total time :{}", System.currentTimeMillis() - startTime);
    }

    private String genNewIndex(EsConfigVO vo) {
        String alias = vo.getIndexAlias();
        int version = vo.getVersion();
        String newIndex = alias + "_v" + ++version;
        vo.setNewIndexName(newIndex);
        return newIndex;
    }

    @Override
    public PageDTO<EsConfigVO> queryConfigByPage(PageRequest pageRequest, EsConfigDTO dto) {
        PageDTO<EsConfigVO> res = new PageDTO<>();
        List<EsConfigVO> dtoList = Lists.newArrayList();
        Page<EsConfig> esConfigPage = esConfigDao.findAll(filterConditions(dto), pageRequest);
        EsConfigVO target = null;
        for (EsConfig o : esConfigPage) {
            target = new EsConfigVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(o, target);
            dtoList.add(target);
        }
        res.build(esConfigPage.getTotalElements(), pageRequest, dtoList);

        return res;
    }

    /**
     * @function: 生成过滤查询条件
     */
    private Predicate filterConditions(EsConfigDTO dto) {
        List<BooleanExpression> myFilter = Lists.newArrayList();
        QEsConfig param = QEsConfig.esConfig;
        if (null != dto) {
            if (StringUtil.isNotEmpty(dto.getIndexAlias())) {
                myFilter.add(param.indexAlias.like("%" + dto.getIndexAlias() + "%"));
            }
            if (StringUtil.isNotEmpty(dto.getIndexName())) {
                myFilter.add(param.indexName.like(dto.getIndexName()));
            }
            if (StringUtil.isNotEmpty(dto.getStatus())) {
                myFilter.add(param.status.eq(dto.getStatus()));
            }
            myFilter.add(param.enableFlag.eq(EnableFlag.Y));
        }

        myFilter.add(param.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(myFilter.toArray(new BooleanExpression[myFilter.size()]));
    }

    /**
     * 请求其他服务
     *
     * @param server
     * @param method
     * @param reqMap
     * @return
     */
    private ResultData callServer(String server, String method, Map<String, String> reqMap) {
        ResultData resultData = null;
        String url = Constants.HTTP_COLON + Constants.DOUBLE_SLASH + server + Constants.SINGLE_SLASH + method;
        try {
            String res = springCloudClient.post(url, JsonUtil.toJSONString(reqMap));
            resultData = JsonUtil.parseObject(res, ResultData.class);
            if (!resultData.isSucceed()) {
                logger.error("{},result:{},msg:{}", url, resultData.getErrorCode(), resultData.getErrorMsg());
                throw new BusinessException(resultData.getErrorCode(), resultData.getErrorMsg());
            }
        } catch (Exception e) {
            logger.error("调用后台服务异常 " + url, e);
            throw new BusinessException(Constants.SYS_ERROR);
        }
        return resultData;
    }
}
