package com.apec.voucher.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apec.framework.cache.CacheHashService;
import com.apec.framework.common.*;
import com.apec.framework.common.enumtype.ConditionsType;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.enumtype.RankingType;
import com.apec.framework.common.enumtype.StatisticCategories;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.common.util.DateTimeUtils;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.voucher.dao.ConditionDAO;
import com.apec.voucher.dao.RankingDAO;
import com.apec.voucher.dao.RankingManDAO;
import com.apec.voucher.dao.VoucherDAO;
import com.apec.voucher.dto.RankingDTO;
import com.apec.voucher.model.Condition;
import com.apec.voucher.model.QRanking;
import com.apec.voucher.model.Ranking;
import com.apec.voucher.model.RankingMan;
import com.apec.voucher.service.RankingService;
import com.apec.voucher.util.SnowFlakeKeyGen;
import com.apec.voucher.viewvo.WeekBest;
import com.apec.voucher.vo.ConditionVO;
import com.apec.voucher.vo.RankingVO;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by hmy on 2017/12/19.
 *
 * @author hmy
 */
@Service
public class RankingServiceImpl implements RankingService {

    @InjectLogger
    private Logger logger;

    @Autowired
    private RankingDAO rankingDAO;

    @Autowired
    private RankingManDAO rankingManDAO;

    @Autowired
    private ConditionDAO conditionDAO;

    @Autowired
    private VoucherDAO voucherDAO;

    @Autowired
    private CacheHashService cacheHashService;

    @Autowired
    private SpringCloudClient springCloudClient;

    @Value("${queryForRanking_Product_url}")
    private String queryForRankingFromProductUrl;

    @Value("${queryUserInfoForRanking_Product_url}")
    private String queryUserInfoForRankingFromProductUrl;

    @Value("${queryForRanking_SocietyPost_url}")
    private String queryForRankingFromSocietyPostUrl;

    @Value("${queryForRanking_SocietyPostReply_url}")
    private String queryForRankingFromSocietyPostReplyUrl;

    @Value("${query_userInfo_url}")
    private String queryUserInfoPageUrl;

    @Autowired
    private SnowFlakeKeyGen idGen;

    private UserInfoVO getUserInfo(Long userId){
        String userInfoJson = cacheHashService.hget(RedisHashConstants.HASH_USER_PREFIX + userId,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        UserInfoVO userInfo ;
        if(StringUtils.isBlank(userInfoJson)){
            //获取不到数据,记录日志
            logger.warn("[RankingServiceImpl][getUserInfo]Can't find user hash cache. userNo:{}",userId);
            userInfo = new UserInfoVO();
        }else{
            userInfo = BaseJsonUtil.parseObject(userInfoJson,UserInfoVO.class);
        }
        return userInfo;
    }

    @Override
    public PageDTO<RankingVO> rankingPage(RankingDTO rankingDTO, PageRequest pageRequest) {
        PageDTO<RankingVO> result = new PageDTO<>();
        List<RankingVO> list = new ArrayList<>();
        Page<Ranking> page = rankingDAO.findAll(getInputCondition(rankingDTO),pageRequest);
        page.forEach(ranking -> {
            RankingVO rankingVO = new RankingVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(ranking,rankingVO);
            rankingVO.setRankingTypeKey(ranking.getRankingType().getKey());
            rankingVO.setAuditState(findAudit(rankingVO));
            list.add(rankingVO);
        });
        result.setNumber(page.getNumber() + 1);
        result.setTotalPages(page.getTotalPages());
        result.setTotalElements(page.getTotalElements());
        result.setRows(list);
        return result;
    }

    @Override
    public RankingVO findRankingInfo(RankingVO rankingVO) {
        Ranking ranking = rankingDAO.findOne(rankingVO.getId());
        if(ranking == null){
            return null;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(ranking,rankingVO);
        rankingVO.setRankingTypeKey(ranking.getRankingType().getKey());
        rankingVO.setAuditState(findAudit(rankingVO));
        Iterable<Condition> iterable = conditionDAO.findByRankingIdAndEnableFlag(ranking.getId(),EnableFlag.Y);
        List<ConditionVO> conditions = new ArrayList<>();
        iterable.forEach(condition -> {
            ConditionVO conditionVO = new ConditionVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(condition,conditionVO);
            conditionVO.setCategoriesKey(condition.getCategories().getKey());
            conditionVO.setConditionsTypeKey(condition.getConditionsType().getKey());
            conditions.add(conditionVO);
        });
        Iterable<RankingMan> it = rankingManDAO.findByRankingIdAndEnableFlag(ranking.getId(),EnableFlag.Y);
        List<WeekBest> weekBests = new ArrayList<>();
        it.forEach(rankingMan -> {
            WeekBest weekBest = new WeekBest();
            BeanUtil.copyPropertiesIgnoreNullFilds(rankingMan,weekBest);
            weekBests.add(weekBest);
        });
        rankingVO.setConditions(conditions);
        rankingVO.setRankingMans(weekBests);
        return rankingVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addRankingInfo(RankingVO rankingVO, String userId) {
        if(rankingDAO.countByRankingType(rankingVO.getRankingType().name(),EnableFlag.Y.name(),rankingVO.getStartTime(),rankingVO.getEndTime()) > 0 ){
            return ErrorCodeConst.ERROR_TIME;
        }
        Ranking ranking = new Ranking();
        BeanUtil.copyPropertiesIgnoreNullFilds(rankingVO,ranking);
        ranking.setId(idGen.nextId());
        ranking.setEnableFlag(EnableFlag.Y);
        ranking.setCreateBy(userId);
        ranking.setCreateDate(new Date());
        rankingDAO.save(ranking);
        //添加上榜条件
        addConditions(ranking,rankingVO.getConditions(),userId);
        //添加上榜用户
        addRankingMans(ranking,rankingVO.getRankingMans(),userId);
        return Constants.RETURN_SUCESS;
    }

    @Transactional(rollbackFor = Exception.class)
    public String addConditions(Ranking ranking, List<ConditionVO> conditions, String userId){
        if(!CollectionUtils.isEmpty(conditions)){
            conditions.forEach(conditionVO -> {
                Condition condition = new Condition();
                BeanUtil.copyPropertiesIgnoreNullFilds(conditionVO,condition);
                condition.setRankingId(ranking.getId());
                condition.setId(idGen.nextId());
                condition.setEnableFlag(EnableFlag.Y);
                condition.setCreateBy(userId);
                condition.setCreateDate(new Date());
                conditionDAO.save(condition);
            });
        }
        return Constants.RETURN_SUCESS;
    }

    @Transactional(rollbackFor = Exception.class)
    public String addRankingMans(Ranking ranking, List<WeekBest> rankingmans, String userId){
        if(!CollectionUtils.isEmpty(rankingmans)){
            rankingmans.forEach(weekBest -> {
                RankingMan rankingMan = new RankingMan();
                BeanUtil.copyPropertiesIgnoreNullFilds(weekBest,rankingMan);
                UserInfoVO userInfoVO = getUserInfo(weekBest.getUserId());
                if(userInfoVO != null){
                    rankingMan.setName(userInfoVO.getName());
                }
                rankingMan.setRankingId(ranking.getId());
                rankingMan.setId(idGen.nextId());
                rankingMan.setEnableFlag(EnableFlag.Y);
                rankingMan.setCreateBy(userId);
                rankingMan.setCreateDate(new Date());
                rankingManDAO.save(rankingMan);
            });
        }
        return Constants.RETURN_SUCESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateRankingInfo(RankingVO rankingVO, String userId) {
        Ranking ranking = rankingDAO.findOne(rankingVO.getId());
        if(ranking == null){
            return Constants.ENABLE_NOT_NULL;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(rankingVO,ranking);
        ranking.setLastUpdateDate(new Date());
        ranking.setLastUpdateBy(userId);
        rankingDAO.save(ranking);
        //删除原来的上榜条件和上榜用户
        conditionDAO.deleteByRankingIdAndEnableFlag(ranking.getId(),EnableFlag.Y);
        rankingManDAO.deleteByRankingIdAndEnableFlag(ranking.getId(),EnableFlag.Y);
        //添加上榜条件
        addConditions(ranking,rankingVO.getConditions(),userId);
        //添加上榜用户
        addRankingMans(ranking,rankingVO.getRankingMans(),userId);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String outlineRankingInfo(RankingVO rankingVO, String userId) {
        Ranking ranking = rankingDAO.findOne(rankingVO.getId());
        if(ranking == null){
            return Constants.ENABLE_NOT_NULL;
        }
        ranking.setEndTime(new Date());
        ranking.setLastUpdateDate(new Date());
        ranking.setLastUpdateBy(userId);
        rankingDAO.save(ranking);
        if(ranking.getRankingType() == RankingType.ACTIVE_MAN){
            //活跃达人
            cacheHashService.hdelField(RedisConstants.WEEKBEST_PRODUCT,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        }else if(ranking.getRankingType() == RankingType.FRUITING_PEOPLE){
            //调果达人
            cacheHashService.hdelField(RedisConstants.WEEKBEST_VOUCHER,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        }
        return Constants.RETURN_SUCESS;
    }

    @Override
    public PageDTO<WeekBest> queryUserInfoForRanking(RankingVO rankingVO,PageRequest pageRequest) {
        if(CollectionUtils.isEmpty(rankingVO.getConditions())){
            return getUserInfoPage(null,pageRequest);
        }
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        List<Long> userIds = new ArrayList<>();
        rankingVO.getConditions().forEach(conditionVO -> {
            Date startTime;
            Date endTime;
            if(conditionVO.getCategories() == StatisticCategories.LAST_MONTH){
                //上个月第一天
                startTime = DateTimeUtils.getFisrtDayOfMonth(year,month);
                //上个月最后一天
                endTime = DateTimeUtils.getLastDayOfMonth(year,month);
            }else if(conditionVO.getCategories() == StatisticCategories.TOTAL){
                //当年第一天
                startTime = DateTimeUtils.getCurrYearFirst(year);
                endTime = now;
            }else{
                return ;
            }
            List<Object[]> objs = null;
            if(conditionVO.getConditionsType() == ConditionsType.VOUCHER_TOTALNUM){
                objs = voucherDAO.checkVoucherMan(conditionVO.getNum(),0,startTime,endTime);
            }if(conditionVO.getConditionsType() == ConditionsType.VOUCHER_NUM){
                objs = voucherDAO.checkVoucherMan(0,conditionVO.getNum(),startTime,endTime);
            }if(conditionVO.getConditionsType() == ConditionsType.PUBLISH_PRODUCT){
                //发送请求接口查询相应条件的用户
                objs = getUserInfoFormProduct(conditionVO.getNum(),startTime,endTime);
            }if(conditionVO.getConditionsType() == ConditionsType.PUBLISH_SOCIETYPOST){
                //发送请求接口查询相应条件的用户
                objs = getUserInfoFormSocietyPost(conditionVO.getNum(),startTime,endTime);
            }if(conditionVO.getConditionsType() == ConditionsType.GIVE_THE_COMMENT){
                //发送请求接口查询相应条件的用户
                objs = getUserInfoFormSocietyPostReply(conditionVO.getNum(),startTime,endTime);
            }
            if(!CollectionUtils.isEmpty(objs)){
                objs.forEach(objects -> {
                    Long userId = NumberUtils.createLong(Objects.toString(objects[0]));
                    userIds.add(userId);
                });
            }
        });
        List<Long> ids = Lists.newArrayList(userIds);
        if(rankingVO.getConditions().size() > 1){
            Multiset<Long> multiset = HashMultiset.create();
            multiset.addAll(userIds);
            Set<Long> elementSet = multiset.elementSet();
            ids = new ArrayList<>();
            for(Long id :elementSet){
                if(multiset.count(id) == rankingVO.getConditions().size()){
                    ids.add(id);
                }
            }
        }
        //分页查询用户信息
        if(!CollectionUtils.isEmpty(ids) && ids.size() > 0){
            return getUserInfoPage(ids,pageRequest);
        }
        return new PageDTO<>();
    }

    @Override
    public List<WeekBest> queryRankingMan(RankingVO rankingVO) {
        String rankingInfo = null;
        if(rankingVO.getRankingType() == RankingType.ACTIVE_MAN){
            //活跃达人
            rankingInfo = cacheHashService.hget(RedisConstants.WEEKBEST_PRODUCT,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        }else if(rankingVO.getRankingType() == RankingType.FRUITING_PEOPLE){
            //调果达人
            rankingInfo = cacheHashService.hget(RedisConstants.WEEKBEST_VOUCHER,RedisHashConstants.HASH_OBJCONTENT_CACHE);
        }
        RankingVO rankingVO1 = null;
        if(StringUtils.isNotBlank(rankingInfo)){
            rankingVO1 = BaseJsonUtil.parseObject(rankingInfo,RankingVO.class);
        }
        if(rankingVO1 == null){
            //redis中没有数据,则从数据库中查询相应数据
            Ranking ranking = rankingDAO.findFirstByRankingTypeAndEnableFlagAndStartTimeBeforeAndEndTimeAfterOrderByCreateDateDesc(rankingVO.getRankingType(),EnableFlag.Y, new Date(), new Date());
            if(ranking == null){
                return null;
            }
            rankingVO1 = new RankingVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(ranking,rankingVO1);
            Iterable<RankingMan> manIterable = rankingManDAO.findByRankingIdAndEnableFlag(ranking.getId(),EnableFlag.Y);
            Iterator<RankingMan> iterator = manIterable.iterator();
            List<Long> weekBests = new ArrayList<>();
            while(iterator.hasNext()){
                RankingMan rankingMan = iterator.next();
                WeekBest weekBest = new WeekBest();
                BeanUtil.copyPropertiesIgnoreNullFilds(rankingMan,weekBest);
                weekBests.add(rankingMan.getUserId());
            }
            if(!CollectionUtils.isEmpty(weekBests)){
                PageDTO<WeekBest> pageDTO = getUserInfoPage(weekBests,new PageRequest(0,weekBests.size()));
                rankingVO1.setRankingMans(getWeekBestUserInfo(rankingVO1,pageDTO.getRows()));
            }
            Iterable<Condition> conditionIterable = conditionDAO.findByRankingIdAndEnableFlag(ranking.getId(),EnableFlag.Y);
            List<ConditionVO> conditionVOS = new ArrayList<>();
            conditionIterable.forEach(condition -> {
                ConditionVO conditionVO = new ConditionVO();
                BeanUtil.copyPropertiesIgnoreNullFilds(condition,conditionVO);
                conditionVOS.add(conditionVO);
            });
            rankingVO1.setConditions(conditionVOS);
            int diffTime = DateTimeUtils.getDifferTime(rankingVO1.getEndTime(),new Date(),DateTimeUtils.SECOND);
            if(rankingVO.getRankingType() == RankingType.ACTIVE_MAN){
                //活跃达人
                cacheHashService.hset(RedisConstants.WEEKBEST_PRODUCT,RedisHashConstants.HASH_OBJCONTENT_CACHE,BaseJsonUtil.toJSONString(rankingVO1));
                cacheHashService.expire(RedisConstants.WEEKBEST_PRODUCT,diffTime);
            }if(rankingVO.getRankingType() == RankingType.FRUITING_PEOPLE){
                //调果达人
                cacheHashService.hset(RedisConstants.WEEKBEST_VOUCHER,RedisHashConstants.HASH_OBJCONTENT_CACHE,BaseJsonUtil.toJSONString(rankingVO1));
                cacheHashService.expire(RedisConstants.WEEKBEST_VOUCHER,diffTime);
            }
        }
        if(rankingVO1.getActualTime() != null && rankingVO1.getActualTime()){
            //要求实时,查询满足条件的用户,限制三人
            PageDTO<WeekBest> pageDTO = queryUserInfoForRanking(rankingVO1,new PageRequest(0,3));
            if(!CollectionUtils.isEmpty(pageDTO.getRows())){
                return getWeekBestUserInfo(rankingVO1,pageDTO.getRows());
            }
            return null;
        }
        //非实时,返回用户信息
        return rankingVO1.getRankingMans();
    }

    public List<WeekBest> getWeekBestUserInfo(RankingVO rankingVO ,List<WeekBest> users){
        List<Long> ids = new ArrayList<>();
        users.forEach(weekBest -> {
            ids.add(weekBest.getUserId());
        });
        List<Object[]> objects;
        if(rankingVO.getRankingType() == RankingType.ACTIVE_MAN){
            //活跃达人,查询上传供求次数
            objects = getUserInfoFormProduct(ids);
            users.forEach(weekBest -> {
                objects.forEach(objects1 -> {
                    Long userId = NumberUtils.createLong(Objects.toString(objects1[0]));
                    if(Objects.equals(weekBest.getUserId(),userId)){
                        weekBest.setTime(NumberUtils.createInteger(Objects.toString(objects1[1])));
                        weekBest.setCreateDate(new Date());
                    }
                });
            });
        }else if(rankingVO.getRankingType() == RankingType.FRUITING_PEOPLE){
            //调果达人,查询调果数和调果次数
            objects = voucherDAO.queryUserNum(ids);
            users.forEach(weekBest -> {
                objects.forEach(objects1 -> {
                    Long userId = NumberUtils.createLong(Objects.toString(objects1[0]));
                    if(Objects.equals(weekBest.getUserId(),userId)){
                        weekBest.setTime(NumberUtils.createInteger(Objects.toString(objects1[2])));
                        weekBest.setSumWeight(NumberUtils.createDouble(Objects.toString(objects1[1])));
                        weekBest.setCreateDate(new Date());
                    }
                });
            });
        }
        return users;
    }

    private List<Object[]> getUserInfoFormProduct(int num, Date startTime, Date endTime){

        String url = queryForRankingFromProductUrl + "?num=" + num + "&startTime="
                + DateTimeUtils.date2String(startTime,"yyyy-MM-dd HH:mm:ss")
                + "&endTime=" + DateTimeUtils.date2String(endTime,"yyyy-MM-dd HH:mm:ss");
        String json = springCloudClient.get(url);
        ResultData<List<Object[]>> resultData = BaseJsonUtil.parseObject(json, new TypeReference<ResultData<List<Object[]>>>(){});
        return resultData.getData();
    }

    private List<Object[]> getUserInfoFormProduct(List<Long> userIds){
        JSONObject param = new JSONObject();
        param.put("userIds", userIds);
        String json = springCloudClient.post(queryUserInfoForRankingFromProductUrl, param.toString());
        ResultData<List<Object[]>> resultData = BaseJsonUtil.parseObject(json, new TypeReference<ResultData<List<Object[]>>>(){});
        return resultData.getData();
    }

    private List<Object[]> getUserInfoFormSocietyPost(int num, Date startTime, Date endTime){

        String url = queryForRankingFromSocietyPostUrl + "?num=" + num + "&startTime="
                + DateTimeUtils.date2String(startTime,"yyyy-MM-dd HH:mm:ss")
                + "&endTime=" + DateTimeUtils.date2String(endTime,"yyyy-MM-dd HH:mm:ss");
        String json = springCloudClient.get(url);
        ResultData<List<Object[]>> resultData = BaseJsonUtil.parseObject(json, new TypeReference<ResultData<List<Object[]>>>(){});
        return resultData.getData();
    }

    private List<Object[]> getUserInfoFormSocietyPostReply(int num, Date startTime, Date endTime){
        String url = queryForRankingFromSocietyPostReplyUrl + "?num=" + num + "&startTime="
                + DateTimeUtils.date2String(startTime,"yyyy-MM-dd HH:mm:ss")
                + "&endTime=" + DateTimeUtils.date2String(endTime,"yyyy-MM-dd HH:mm:ss");
        String json = springCloudClient.get(url);
        ResultData<List<Object[]>> resultData = BaseJsonUtil.parseObject(json, new TypeReference<ResultData<List<Object[]>>>(){});
        return resultData.getData();
    }

    private PageDTO<WeekBest> getUserInfoPage(List<Long> userId,PageRequest pageRequest){
        JSONObject param = new JSONObject();
        param.put("userIds", userId);
        param.put("pageSize", pageRequest.getPageSize());
        param.put("pageNumber", pageRequest.getPageNumber() + 1);
        String json = springCloudClient.post(queryUserInfoPageUrl, param.toString());
        ResultData<PageDTO<UserInfoVO>> resultData = BaseJsonUtil.parseObject(json, new TypeReference<ResultData<PageDTO<UserInfoVO>>>(){});
        PageDTO<WeekBest> result = new PageDTO<>();
        if(!resultData.isSucceed()){
            return result;
        }
        PageDTO<UserInfoVO> pageDTO = resultData.getData();
        List<WeekBest> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(pageDTO.getRows())){
            pageDTO.getRows().forEach(userInfoVO -> {
                WeekBest weekBest = new WeekBest();
                BeanUtil.copyPropertiesIgnoreNullFilds(userInfoVO,weekBest,"userType");
                weekBest.setUserId(userInfoVO.getId());
                weekBest.setUserType(userInfoVO.getUserType().getKey());
                weekBest.setImageUrl(userInfoVO.getImgUrl());
                list.add(weekBest);
            });
        }
        result.setRows(list);
        result.setTotalElements(pageDTO.getTotalElements());
        result.setNumber(pageRequest.getPageNumber() + 1);
        result.setTotalPages(pageRequest.getPageSize() == 0 ? 1 :
                (int)Math.ceil( (double)pageDTO.getTotalElements() / (double)pageRequest.getPageSize() ));
        return result;
    }

    private String findAudit(RankingVO rankingVO){
        String str = "未开始";
        Date date = new Date();
        if(date.getTime() - rankingVO.getEndTime().getTime() > 0){
            //当前时间已经超过下线时间
            str = "已下线";
        }else if(date.getTime() - rankingVO.getStartTime().getTime() > 0){
            //当前时间已经超过上线时间
            str = "进行中";
        }
        return str;
    }

    /**
     * 根据多种情况查询用户信息
     * 包括like：name，realName, companyName, address, addressDetail eq：mobile，userType
     * @param  rankingDTO vo
     * @return  Predicate
     */
    private Predicate getInputCondition(RankingDTO rankingDTO)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != rankingDTO)
        {

            if (rankingDTO.getActualTime() != null) {
                predicates.add(QRanking.ranking.actualTime.eq(rankingDTO.getActualTime()));
            }
            if (rankingDTO.getRankingType() != null) {
                predicates.add(QRanking.ranking.rankingType.eq(rankingDTO.getRankingType()));
            }

            if(rankingDTO.getStartTime() != null)
            {
                predicates.add(QRanking.ranking.createDate.loe(rankingDTO.getStartTime()));
            }

            if(rankingDTO.getEndTime() != null)
            {
                predicates.add(QRanking.ranking.createDate.goe(rankingDTO.getEndTime()));
            }
        }
        predicates.add(QRanking.ranking.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }


}
