package com.apec.systemconfig.service.impl;

import com.apec.framework.common.Constants;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BeanUtil;
import com.apec.framework.log.InjectLogger;
import com.apec.systemconfig.dao.FeedBackDAO;
import com.apec.systemconfig.dto.FeedBackDTO;
import com.apec.systemconfig.model.FeedBack;
import com.apec.systemconfig.model.QFeedBack;
import com.apec.systemconfig.service.FeedBackService;
import com.apec.systemconfig.util.SnowFlakeKeyGen;
import com.apec.systemconfig.vo.FeedBackVO;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hmy on 2017/8/28.
 * @author hmy
 */
@Service
public class FeedBackServiceImpl implements FeedBackService {

    @Autowired
    private SnowFlakeKeyGen idGen;

    @InjectLogger
    private Logger logger;

    @Autowired
    private FeedBackDAO feedBackDAO;

    /**
     * 增加反馈信息
     */
    @Override
    public String addFeedBackInfo(FeedBackVO feedBackVO,String userId){
        FeedBack feedBack = new FeedBack();
        BeanUtil.copyPropertiesIgnoreNullFilds(feedBackVO,feedBack);
        feedBack.setId(idGen.nextId());
        feedBack.setEnableFlag(EnableFlag.Y);
        feedBack.setCreateDate(new Date());
        feedBack.setCreateBy(userId);
        feedBack.setReportTime(new Date());
        feedBackDAO.save(feedBack);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 多条件查询商品信息
     * 根据多种情况查询
     * 包括like:informantUser,reportedUser,feedBackInfo
     * eq:id,feedBackType
     */
    private Predicate getInputCondition(FeedBackDTO vo)
    {
        List<BooleanExpression> predicates = new ArrayList<>();
        if(null != vo)
        {
            if(vo.getId() != null)
            {
                predicates.add(QFeedBack.feedBack.id.eq(vo.getId()));
            }
            if(!StringUtils.isEmpty(vo.getInformantUser()))
            {
                predicates.add(QFeedBack.feedBack.informantUser.like("%"+vo.getInformantUser()+"%"));
            }
            if(!StringUtils.isEmpty(vo.getReportedUser()))
            {
                predicates.add(QFeedBack.feedBack.reportedUser.like("%" + vo.getReportedUser() + "%"));
            }
            if(!StringUtils.isEmpty(vo.getFeedBackInfo()))
            {
                predicates.add(QFeedBack.feedBack.feedBackInfo.like("%" + vo.getFeedBackInfo() + "%"));
            }
            if(vo.getFeedBackType() != null)
            {
                predicates.add(QFeedBack.feedBack.feedBackType.eq(vo.getFeedBackType()));
            }
            if(vo.getRealm() != null)
            {
                predicates.add(QFeedBack.feedBack.realm.eq(vo.getRealm()));
            }
            if(vo.getStartDate() != null){
                predicates.add(QFeedBack.feedBack.createDate.goe(vo.getStartDate()));
            }
            if(vo.getEndDate() != null){
                predicates.add(QFeedBack.feedBack.createDate.loe(vo.getEndDate()));
            }
        }
        predicates.add(QFeedBack.feedBack.enableFlag.eq(EnableFlag.Y));
        return BooleanExpression.allOf(predicates.toArray(new BooleanExpression[predicates.size()]));
    }

    /**
     * 分页查询反馈信息
     */
    @Override
    public PageDTO<FeedBackVO> queryFeedBackPage(PageRequest pageRequest, FeedBackDTO dto){
        PageDTO<FeedBackVO> result = new PageDTO<>();
        Page<FeedBack> page = feedBackDAO.findAll(getInputCondition(dto),pageRequest);
        List<FeedBackVO> list = new ArrayList<>();
        for(FeedBack feedBack:page){
            FeedBackVO feedBackVO = new FeedBackVO();
            BeanUtil.copyPropertiesIgnoreNullFilds(feedBack,feedBackVO);
            feedBackVO.setFeedBackTypeKey(feedBack.getFeedBackType().getKey());
            list.add(feedBackVO);
        }
        result.setRows(list);
        result.setNumber(page.getNumber()+1);
        result.setTotalPages(page.getTotalPages());
        result.setTotalElements(page.getTotalElements());
        return result;
    }

    /**
     * 查询举报具体信息
     */
    @Override
    public FeedBackVO queryFeedBackInfo(FeedBackVO feedBackVO){
        FeedBack feedBack = feedBackDAO.findOne(feedBackVO.getId());
        if(feedBack == null || feedBack.getId() == null || feedBack.getId() == 0L){
            return new FeedBackVO();
        }
        BeanUtil.copyPropertiesIgnoreNullFilds(feedBack,feedBackVO);
        return feedBackVO;
    }

    /**
     * 增加反馈信息
     */
    @Override
    public String deleteFeedBackInfo(FeedBackVO feedBackVO, String userId){
        FeedBack feedBack = feedBackDAO.findOne(feedBackVO.getId());
        if(feedBack == null || feedBack.getId() == null || feedBack.getId() == 0L){
            //数据不存在
            return Constants.DATA_ISNULL;
        }
        feedBack.setEnableFlag(EnableFlag.N);
        feedBack.setLastUpdateBy(userId);
        feedBack.setLastUpdateDate(new Date());
        feedBackDAO.save(feedBack);
        return Constants.RETURN_SUCESS;
    }

    /**
     * 批量删除反馈信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteFeedBackList(List<Long> ids, String userId){
        feedBackDAO.deleteFeedBackList(ids, userId);
        return Constants.RETURN_SUCESS;
    }



}
