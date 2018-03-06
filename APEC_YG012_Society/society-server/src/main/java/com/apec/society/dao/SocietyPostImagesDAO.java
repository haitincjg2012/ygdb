package com.apec.society.dao;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.mongodb.dao.BaseDAO;
import com.apec.society.model.SocietyPostImages;

import java.util.List;

/**
 * Created by hmy on 2017/10/30.
 *
 * @author hmy
 */
public interface SocietyPostImagesDAO extends BaseDAO<SocietyPostImages,Long> {

    /**
     * 根据帖子id查询上传的图片
     * @param societyPostId 帖子id
     * @param enableFlag 状态码
     * @return 上传的图片
     */
    List<SocietyPostImages> findBySocietyPostIdAndEnableFlagOrderBySort(Long societyPostId,EnableFlag enableFlag);


}
