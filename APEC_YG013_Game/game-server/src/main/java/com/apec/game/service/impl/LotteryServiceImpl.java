package com.apec.game.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.dto.UserInfoVO;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.game.dao.*;
import com.apec.game.dto.LotteryDTO;
import com.apec.game.dto.PrizeAwardDTO;
import com.apec.game.dto.PrizePeopleDTO;
import com.apec.game.model.*;
import com.apec.game.service.LotteryService;
import com.apec.game.util.SnowFlakeKeyGen;
import com.apec.game.vo.*;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by hmy on 2017/12/21.
 *
 * @author hmy
 */
@Service
public class LotteryServiceImpl implements LotteryService {

    @Autowired
    private SnowFlakeKeyGen idGen;

    @InjectLogger
    private Logger logger;

    @Autowired
    private LotteryDAO lotteryDAO;

    @Autowired
    private LotteryHintDAO lotteryHintDAO;

    @Autowired
    private LotteryFormDAO lotteryFormDAO;

    @Autowired
    private ActiveRuleDAO activeRuleDAO;

    @Autowired
    private PrizeAwardDAO prizeAwardDAO;

    @Autowired
    private PrizePeopleDAO prizePeopleDAO;

    @Autowired
    private ParticipationConditionDAO participationConditionDAO;

    @Autowired
    private SpringCloudClient springCloudClient;

    @Value("${query_userInfo_url}")
    private String queryUserInfoPageUrl;

    @Override
    public String addLottery(LotteryVO lotteryVO, String userId) {
        Lottery lottery = new Lottery();
        BeanUtil.copyPropertiesIgnoreNullFilds(lotteryVO,lottery);
        lottery.setId(idGen.nextId());
        lottery.setEnableFlag(EnableFlag.Y);
        lottery.setCreateDate(new Date());
        lottery.setCreateBy(userId);
        lotteryDAO.save(lottery);
        if(!CollectionUtils.isEmpty(lotteryVO.getLotteryHintVOS())){
            lotteryVO.getLotteryHintVOS().forEach(lotteryHintVO -> {
                LotteryHint lotteryHint = new LotteryHint();
                BeanUtil.copyPropertiesIgnoreNullFilds(lotteryHintVO,lotteryHint);
                lotteryHint.setId(idGen.nextId());
                lotteryHint.setEnableFlag(EnableFlag.Y);
                lotteryHint.setCreateBy(userId);
                lotteryHint.setCreateDate(new Date());
                lotteryHint.setLotteryId(lottery.getId());
                lotteryHintDAO.save(lotteryHint);
            });
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(lottery,lotteryVO);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String updateLottery(LotteryVO lotteryVO, String userId) {
        Lottery lottery = lotteryDAO.findOne(lotteryVO.getId());
        if(lottery == null){
            return Constants.ENABLE_NOT_NULL;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(lotteryVO,lottery);
        lottery.setLastUpdateBy(userId);
        lottery.setLastUpdateDate(new Date());
        lotteryDAO.save(lottery);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String deleteLottery(LotteryVO lotteryVO, String userId) {
        Lottery lottery = lotteryDAO.findOne(lotteryVO.getId());
        if(lottery == null){
            return Constants.ENABLE_NOT_NULL;
        }
        lottery.setEnableFlag(EnableFlag.N);
        lottery.setLastUpdateBy(userId);
        lottery.setLastUpdateDate(new Date());
        lotteryDAO.save(lottery);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String updateLotteryHoint(LotteryHintVO lotteryHintVO, String userId) {
        LotteryHint lotteryHint = lotteryHintDAO.findOne(lotteryHintVO.getId());
        if(lotteryHint == null){
            return Constants.ENABLE_NOT_NULL;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(lotteryHintVO,lotteryHint);
        lotteryHint.setLastUpdateBy(userId);
        lotteryHint.setLastUpdateDate(new Date());
        lotteryHintDAO.save(lotteryHint);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String deleteLotteryHoint(LotteryHintVO lotteryHintVO, String userId) {
        LotteryHint lotteryHint = lotteryHintDAO.findOne(lotteryHintVO.getId());
        if(lotteryHint == null){
            return Constants.ENABLE_NOT_NULL;
        }
        lotteryHint.setEnableFlag(EnableFlag.N);
        lotteryHint.setLastUpdateBy(userId);
        lotteryHint.setLastUpdateDate(new Date());
        lotteryHintDAO.save(lotteryHint);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public LotteryVO queryLotteryInfo(LotteryVO lotteryVO) {
        Lottery lottery = lotteryDAO.findOne(lotteryVO.getId());
        if(lottery == null){
            return null;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(lottery,lotteryVO);
        lotteryVO.setLotteryFormTypeKey(lottery.getLotteryFormType().getKey());
        List<LotteryHintVO> list = new ArrayList<>();
        Iterable<LotteryHint> lotteryHints = lotteryHintDAO.findByLotteryIdAndEnableFlag(lottery.getId(),EnableFlag.Y);
        lotteryHints.forEach(lotteryHint -> {
            LotteryHintVO lotteryHintVO = new LotteryHintVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(lotteryHint,lotteryHintVO);
            lotteryHintVO.setParticipationStateKey(lotteryHint.getParticipationState().getKey());
            list.add(lotteryHintVO);
        });
        lotteryVO.setLotteryHintVOS(list);
        return lotteryVO;
    }

    @Override
    public List<LotteryFormVO> queryLotteryFormList() {
        List<LotteryFormVO> list = new ArrayList<>();
        Iterable<LotteryForm> iterable = lotteryFormDAO.findAll(QLotteryForm.lotteryForm.enableFlag.eq(EnableFlag.Y));
        iterable.forEach(lotteryForm -> {
            LotteryFormVO lotteryFormVO = new LotteryFormVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(lotteryForm,lotteryFormVO);
            lotteryFormVO.setLotteryFormTypeKey(lotteryForm.getLotteryFormType().getKey());
            list.add(lotteryFormVO);
        });
        return list;
    }

    @Override
    public List<ParticipationConditionVO> queryParticipationConditionList() {
        List<ParticipationConditionVO> list = new ArrayList<>();
        Iterable<ParticipationCondition> iterable = participationConditionDAO.findAll(QParticipationCondition.participationCondition.enableFlag.eq(EnableFlag.Y));
        iterable.forEach(participationCondition -> {
            ParticipationConditionVO participationConditionVO = new ParticipationConditionVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(participationCondition,participationConditionVO);
            list.add(participationConditionVO);
        });
        return list;
    }

    @Override
    public String addActiveRule(ActiveRuleVO activeRuleVO, String userId) {
        ActiveRule activeRule = new ActiveRule();
        BeanUtil.copyPropertiesIgnoreNullFilds(activeRuleVO,activeRule);
        if(activeRuleVO.getParticipationPoints() == null){
            activeRule.setParticipationPoints(0);
        }
        if(activeRuleVO.getPoints() == null){
            activeRule.setPoints(0);
        }
        activeRule.setEnableFlag(EnableFlag.Y);
        activeRule.setCreateBy(userId);
        activeRule.setCreateDate(new Date());
        activeRule.setId(idGen.nextId());
        activeRuleDAO.save(activeRule);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String updateActiveRule(ActiveRuleVO activeRuleVO, String userId) {
        ActiveRule activeRule = activeRuleDAO.findOne(activeRuleVO.getId());
        if(activeRuleVO == null){
            return Constants.ENABLE_NOT_NULL;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(activeRuleVO,activeRule);
        activeRule.setLastUpdateBy(userId);
        activeRule.setLastUpdateDate(new Date());
        activeRuleDAO.save(activeRule);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public ActiveRuleVO queryActiveRuleInfoOfLottery(ActiveRuleVO activeRuleVO) {
        ActiveRule activeRule = activeRuleDAO.findByLotteryIdAndEnableFlag(activeRuleVO.getLotteryId(),EnableFlag.Y);
        if(activeRule == null){
            return null;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(activeRule,activeRuleVO);
        if(activeRule.getParticipationNumType() != null){
            activeRuleVO.setParticipationNumTypeKey(activeRule.getParticipationNumType().getKey());
        }
        ParticipationCondition participationCondition = participationConditionDAO.findOne(activeRule.getConditionId());
        if(participationCondition != null){
            activeRuleVO.setConditionName(participationCondition.getConditionName());
        }
        return activeRuleVO;
    }

    @Override
    public String addLotteryPrize(PrizeAwardVO prizeAwardVO, String userId) {
        PrizeAward prizeAward = new PrizeAward();
        BeanUtil.copyPropertiesIgnoreNullFilds(prizeAwardVO,prizeAward);
        prizeAward.setId(idGen.nextId());
        prizeAward.setCreateDate(new Date());
        prizeAward.setCreateBy(userId);
        prizeAward.setEnableFlag(EnableFlag.Y);
        prizeAwardDAO.save(prizeAward);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String updateLotteryPrize(PrizeAwardVO prizeAwardVO, String userId) {
        PrizeAward prizeAward = prizeAwardDAO.findOne(prizeAwardVO.getId());
        if(prizeAward == null){
            return Constants.ENABLE_NOT_NULL;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(prizeAwardVO,prizeAward);
        prizeAward.setLastUpdateDate(new Date());
        prizeAward.setLastUpdateBy(userId);
        prizeAwardDAO.save(prizeAward);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public String deleteLotteryPrize(PrizeAwardVO prizeAwardVO, String userId) {
        PrizeAward prizeAward = prizeAwardDAO.findOne(prizeAwardVO.getId());
        if(prizeAward == null){
            return Constants.ENABLE_NOT_NULL;
        }
        prizeAward.setEnableFlag(EnableFlag.N);
        prizeAward.setLastUpdateDate(new Date());
        prizeAward.setLastUpdateBy(userId);
        prizeAwardDAO.save(prizeAward);
        return Constants.RETURN_SUCESS;
    }

    @Override
    public PrizeAwardVO queryLotteryPrizeInfo(PrizeAwardVO prizeAwardVO) {
        PrizeAward prizeAward = prizeAwardDAO.findOne(prizeAwardVO.getId());
        if(prizeAward == null){
            return null;
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(prizeAward,prizeAwardVO);
        return prizeAwardVO;
    }

    @Override
    public PageDTO<PrizeAwardVO> queryLotteryPrizePage(PrizeAwardDTO prizeAwardDTO, PageRequest pageRequest) {
        PageDTO<PrizeAwardVO> result = new PageDTO<>();
        List<PrizeAwardVO> list = new ArrayList<>();
        Page<PrizeAward> page = prizeAwardDAO.findAll(getInputConditionOfPrize(prizeAwardDTO),pageRequest);
        page.forEach(prizeAward -> {
            PrizeAwardVO prizeAwardVO = new PrizeAwardVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(prizeAward,prizeAwardVO);
            prizeAwardVO.setPrizeNobelKey(prizeAward.getPrizeNobel().getKey());
            list.add(prizeAwardVO);
        });
        result.setRows(list);
        result.setTotalPages(page.getTotalPages());
        result.setNumber(page.getNumber() + 1);
        result.setTotalElements(page.getTotalElements());
        return result;
    }

    @Override
    public PageDTO<PrizePeopleVO> queryLotteryPeoplePage(PrizePeopleDTO prizePeopleDTO, PageRequest pageRequest) {
        PageDTO<PrizePeopleVO> result = new PageDTO<>();
        List<PrizePeopleVO> list = new ArrayList<>();
        Page<PrizePeople> page = prizePeopleDAO.findAll(getInputConditionOfPeople(prizePeopleDTO),pageRequest);
        List<Long> userIds = new ArrayList<>();
        page.forEach(prizePeople -> {
            PrizePeopleVO prizePeopleVO = new PrizePeopleVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(prizePeople,prizePeopleVO);
            if(prizePeople.getPrizeNobel() != null){
                prizePeopleVO.setPrizeNobelKey(prizePeople.getPrizeNobel().getKey());
            }
            list.add(prizePeopleVO);
            userIds.add(prizePeople.getUserId());
        });
        if(!CollectionUtils.isEmpty(userIds) && userIds.size() > 0){
            PageDTO<UserInfoVO> userInfo = getUserInfoPage(userIds,new PageRequest(0,userIds.size()));
            List<UserInfoVO> userInfoList = userInfo.getRows();
            if(!CollectionUtils.isEmpty(userInfoList)){
                list.forEach(prizePeopleVO -> {
                    for(UserInfoVO userInfoVO:userInfoList){
                        if(Objects.equals(userInfoVO.getId(),prizePeopleVO.getUserId())){
                            prizePeopleVO.setImgUrl(userInfoVO.getImgUrl());
                            prizePeopleVO.setMobile(userInfoVO.getMobile());
                            prizePeopleVO.setName(userInfoVO.getName());
                            prizePeopleVO.setUserType(userInfoVO.getUserType());
                            prizePeopleVO.setUserTypeKey(userInfoVO.getUserType().getKey());
                            break;
                        }
                    }
                });
            }
        }
        result.setRows(list);
        result.setTotalPages(page.getTotalPages());
        result.setNumber(page.getNumber() + 1);
        result.setTotalElements(page.getTotalElements());
        return result;
    }

    @Override
    public LotteryVO countLotteryPeople(LotteryVO lotteryVO) {
        PrizePeopleDTO prizePeopleDTO = new PrizePeopleDTO();
        prizePeopleDTO.setLotteryId(lotteryVO.getId());
        //查询参与抽奖总人数
        Long totalPeople = prizePeopleDAO.count(getInputConditionOfPeople(prizePeopleDTO));
        prizePeopleDTO.setLackPeople(true);
        //查询中奖人数
        Long lackPeople = prizePeopleDAO.count(getInputConditionOfPeople(prizePeopleDTO));
        lotteryVO.setTotalPeople(totalPeople);
        lotteryVO.setLackPeople(lackPeople);
        return lotteryVO;
    }

    private PageDTO<UserInfoVO> getUserInfoPage(List<Long> userId,PageRequest pageRequest){
        JSONObject param = new JSONObject();
        param.put("userIds", userId);
        param.put("pageSize", pageRequest.getPageSize());
        param.put("pageNumber", pageRequest.getPageNumber() + 1);
        String json = springCloudClient.post(queryUserInfoPageUrl, param.toString());
        ResultData<PageDTO<UserInfoVO>> resultData = BaseJsonUtil.parseObject(json, new TypeReference<ResultData<PageDTO<UserInfoVO>>>(){});
        return resultData.getData();
    }

    @Override
    public PageDTO<LotteryVO> queryLotteryPage(LotteryDTO lotteryDTO, PageRequest pageRequest) {
        PageDTO<LotteryVO> result = new PageDTO<>();
        List<LotteryVO> list = new ArrayList<>();
        Page<Lottery> page = lotteryDAO.findAll(getInputCondition(lotteryDTO),pageRequest);
        page.forEach(lottery -> {
            LotteryVO lotteryVO = new LotteryVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(lottery,lotteryVO);
            lotteryVO.setLotteryFormTypeKey(lottery.getLotteryFormType().getKey());
            lotteryVO.setAuditState(findAudit(lotteryVO));
            //查询参与人次和中奖人数
            lotteryVO = countLotteryPeople(lotteryVO);
            list.add(lotteryVO);
        });
        result.setRows(list);
        result.setTotalPages(page.getTotalPages());
        result.setNumber(page.getNumber() + 1);
        result.setTotalElements(page.getTotalElements());
        return result;
    }

    private String findAudit(LotteryVO lotteryVO){
        String str = "未开始";
        Date date = new Date();
        if(date.getTime() - lotteryVO.getEndTime().getTime() > 0){
            //当前时间已经超过下线时间
            str = "已下线";
        }else if(date.getTime() - lotteryVO.getStartTime().getTime() > 0){
            //当前时间已经超过上线时间
            str = "进行中";
        }
        return str;
    }

    /**
     * 根据多种情况查询活动信息
     * 包括like：lotteryName
     * @param  lotteryDTO vo
     * @return  Predicate
     */
    private Predicate getInputCondition(LotteryDTO lotteryDTO)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != lotteryDTO)
        {
            if (!StringUtils.isBlank(lotteryDTO.getLotteryName())) {
                predicates.add(QLottery.lottery.lotteryName.like("%" + lotteryDTO.getLotteryName() + "%"));
            }
            if(lotteryDTO.getStartDate() != null){
                predicates.add(QLottery.lottery.createDate.goe(lotteryDTO.getStartDate()));
            }
            if(lotteryDTO.getEndDate() != null){
                predicates.add(QLottery.lottery.createDate.loe(lotteryDTO.getEndDate()));
            }
        }
        predicates.add(QLottery.lottery.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

    /**
     * 根据多种情况查询奖品信息
     * 包括like：lotteryName
     * @param  prizeAwardDTO vo
     * @return  Predicate
     */
    private Predicate getInputConditionOfPrize(PrizeAwardDTO prizeAwardDTO)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != prizeAwardDTO)
        {
            if (prizeAwardDTO.getCheckPrizePoint() != null) {
                predicates.add(QPrizeAward.prizeAward.checkPrizePoint.eq(prizeAwardDTO.getCheckPrizePoint()));
            }
            if (prizeAwardDTO.getPrizeNobel() != null) {
                predicates.add(QPrizeAward.prizeAward.prizeNobel.eq(prizeAwardDTO.getPrizeNobel()));
            }
            if (prizeAwardDTO.getLotteryId() != null && prizeAwardDTO.getLotteryId() != 0L) {
                predicates.add(QPrizeAward.prizeAward.lotteryId.eq(prizeAwardDTO.getLotteryId()));
            }
            if (StringUtils.isNotBlank(prizeAwardDTO.getPrizeName())) {
                predicates.add(QPrizeAward.prizeAward.prizeName.like("%" + prizeAwardDTO.getPrizeName() + "%"));
            }

        }
        predicates.add(QPrizeAward.prizeAward.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

    /**
     * 根据多种情况查询奖品信息
     * 包括like：lotteryName
     * @param  prizePeopleDTO vo
     * @return  Predicate
     */
    private Predicate getInputConditionOfPeople(PrizePeopleDTO prizePeopleDTO)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != prizePeopleDTO)
        {
            if (prizePeopleDTO.getLackPeople() != null) {
                predicates.add(QPrizePeople.prizePeople.lackPeople.eq(prizePeopleDTO.getLackPeople()));
            }
            if (prizePeopleDTO.getPrizeNobel() != null) {
                predicates.add(QPrizePeople.prizePeople.prizeNobel.eq(prizePeopleDTO.getPrizeNobel()));
            }
            if (prizePeopleDTO.getLotteryId() != null && prizePeopleDTO.getLotteryId() != 0L) {
                predicates.add(QPrizePeople.prizePeople.lotteryId.eq(prizePeopleDTO.getLotteryId()));
            }
            if (StringUtils.isNotBlank(prizePeopleDTO.getPrizeAwardName())) {
                predicates.add(QPrizePeople.prizePeople.prizeAwardName.like("%" + prizePeopleDTO.getPrizeAwardName() + "%"));
            }

        }
        predicates.add(QPrizePeople.prizePeople.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

}
