package com.apec.society.web;

import com.apec.framework.base.BaseController;
import com.apec.framework.cache.CacheService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.PageJSON;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.constants.LoginConstants;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.common.util.BaseSpringUtil;
import com.apec.framework.dto.BaseDTO;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 *
 *  @author yirder
 */
public class MyBaseController extends BaseController {

    @Autowired
    private CacheService cacheService;

    private static final int PAGE_SIZE_MAX = 1000;

    protected PageRequest genPageRequest(BaseDTO dto) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        int pageNumber = 1;
        int pageSize = 10;
        if (dto.getPageNumber() > 0) {
            pageNumber = dto.getPageNumber();
        }
        if (dto.getPageSize() > 0 && dto.getPageSize() < PAGE_SIZE_MAX) {
            pageSize = dto.getPageSize();
        }
        return new PageRequest(pageNumber - 1, pageSize, sort);
    }

    @Override
    public <T> T getFormJSON(String json, Class<T> clz) {
        Object formJSOn = BaseJsonUtil.getValueBykey(json, "formJSON");
        //服务调用
        if (null == formJSOn)
        {
            return BaseJsonUtil.parseObject(json, clz);
        } else
        {
            //页面调用
            PageJSON<T> pageJSON = getPageJSON(json, clz);
            return pageJSON.getFormJSON();
        }
    }

    public Long getUserId(String json) {
        Object formJSOn = BaseJsonUtil.getValueBykey(json, "formJSON");
        //服务调用
        if (null == formJSOn)
        {
            return 0L;
        } else
        {
            //页面调用
            PageJSON<Object> pageJSON = getPageJSON(json, Object.class);
            return NumberUtils.toLong(getUserNo(pageJSON));
        }
    }

    public Long getUserIdFormToken(String json) {
        PageJSON<Object> pageJSON = getPageJSON(json, Object.class);
        String token = "";
        if(pageJSON != null){
            token = (String)pageJSON.getRequestAttrMap().get(Constants.TOKEN);
        }
        if(StringUtils.isBlank(token)){
            return 0L;
        }
        String tokenKey = LoginConstants.PREFIX_TOKEN + token;
        String userNo = cacheService.get( tokenKey );
        return NumberUtils.createLong(userNo);
    }

    public String getOrgCode(String json) {
        Object formJSOn = BaseJsonUtil.getValueBykey(json, "formJSON");
        //服务调用
        if (null == formJSOn)
        {
            return "";
        } else
        {
            //页面调用
            PageJSON<Object> pageJSON = getPageJSON(json, Object.class);
            return getOrgCode(pageJSON);
        }
    }

    public int getSaleId(String json) {

        //HHJ统一默认为1
        return 1;
    }

    /**
     * 设置错误的响应结果
     * @param resultData 响应对象
     * @param <T>
     * @return
     */
    public <T> void  setErrorResultDate(ResultData<T> resultData,String errorCode){
        resultData.setSucceed(false);
        resultData.setErrorCode(errorCode);
        resultData.setErrorMsg(BaseSpringUtil.getMessage(errorCode));
    }

    public <T> T getParamJSON(String json, Class<T> clz) {
        Object parameterJSON = BaseJsonUtil.getValueBykey(json, "requestParameterMap");
        if (null == parameterJSON) //为空则不解析返回空
        {
            return BaseJsonUtil.parseObject("{}", clz);
        } else
        {
            PageJSON<T> pageJSON = super.getRequestParamJSON(json, clz);
            return pageJSON.getParamJSON();
        }
    }
}
