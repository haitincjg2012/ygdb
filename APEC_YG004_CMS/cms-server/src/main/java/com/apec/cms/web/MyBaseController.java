package com.apec.cms.web;

import com.apec.framework.base.BaseController;
import com.apec.framework.common.PageJSON;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.util.JsonUtil;
import com.apec.framework.common.util.SpringUtil;
import com.apec.framework.dto.BaseDTO;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


public class MyBaseController extends BaseController {

    protected PageRequest genPageRequest(BaseDTO dto) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        int pageNumber = 1;
        int pageSize = 10;
        if (dto.getPageNumber() > 0) {
            pageNumber = dto.getPageNumber();
        }
        if (dto.getPageSize() > 0 && dto.getPageSize() < 1000) {
            pageSize = dto.getPageSize();
        }
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        return pageRequest;
    }

    public <T> T getFormJSON(String json, Class<T> clz) {
        Object formJSOn = JsonUtil.getValueBykey(json, "formJSON");
        if (null == formJSOn) //服务调用
        {
            return JsonUtil.parseObject(json, clz);
        } else //面面调用
        {
            PageJSON<T> pageJSON = getPageJSON(json, clz);
            return pageJSON.getFormJSON();
        }
    }

    public Long getUserId(String json) {
        Object formJSOn = JsonUtil.getValueBykey(json, "formJSON");
        if (null == formJSOn) //服务调用
        {
            return 0L;
        } else //页面调用
        {
            PageJSON<Object> pageJSON = getPageJSON(json, Object.class);
            return NumberUtils.toLong(getUserNo(pageJSON));
        }
    }

    public String getOrgCode(String json) {
        Object formJSOn = JsonUtil.getValueBykey(json, "formJSON");
        if (null == formJSOn) //服务调用
        {
            return "";
        } else //页面调用
        {
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
        resultData.setErrorMsg(SpringUtil.getMessage(errorCode));
    }
}
