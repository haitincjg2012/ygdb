package com.apec.systemuser.web;

import com.apec.framework.base.BaseController;
import com.apec.framework.common.Constants;
import com.apec.framework.common.PageJSON;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.common.util.BaseSpringUtil;
import com.apec.framework.dto.BaseDTO;
import com.apec.systemuser.vo.SysUserVO;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author xxx
 */
public class MyBaseController extends BaseController {

    protected PageRequest genPageRequest(BaseDTO dto) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        int pageNumber = 1;
        int pageSize = 10;
        if (dto.getPageNumber() > 0) {
            pageNumber = dto.getPageNumber();
        }
        if (dto.getPageSize() > 0 && dto.getPageSize() < Integer.valueOf(Constants.MAX_FETCHSIZE)) {
            pageSize = dto.getPageSize();
        }
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        return pageRequest;
    }

    @Override
    public <T> T getFormJSON(String json, Class<T> clz) {
        Object formJSOn = BaseJsonUtil.getValueBykey(json, "formJSON");
        if (null == formJSOn)
        {
            //服务调用
            return BaseJsonUtil.parseObject(json, clz);
        } else
        {
            //面面调用
            PageJSON<T> pageJSON = getPageJSON(json, clz);
            return pageJSON.getFormJSON();
        }
    }

    public Long getUserId(String json) {
        Object formJSOn = BaseJsonUtil.getValueBykey(json, "formJSON");
        if (null == formJSOn)
        {
            //服务调用
            return 0L;
        } else
        {
            //页面调用
            PageJSON<Object> pageJSON = getPageJSON(json, Object.class);
            return NumberUtils.toLong(getUserNo(pageJSON));
        }
    }

    public String getOrgCode(String json) {
        Object formJSOn = BaseJsonUtil.getValueBykey(json, "formJSON");
        if (null == formJSOn)
        {
            //服务调用
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
     * @param errorCode errorCode
     */
    public <T> void  setErrorResultDate(ResultData<T> resultData,String errorCode){
        resultData.setSucceed(false);
        resultData.setErrorCode(errorCode);
        resultData.setErrorMsg(BaseSpringUtil.getMessage(errorCode));
    }

    public String getUserRoleNo(String json)
    {
        Object formJSOn = BaseJsonUtil.getValueBykey(json, "formJSON");
        if(null == formJSOn)
        {
            // 服务调用
            return "";
        }
        else
        {
            // 页面调用
            PageJSON<Object> pageJSON = getPageJSON(json, Object.class);
            return getSysUserInfo(pageJSON).getUserRoleNo();
        }
    }

    /**
     * 获取系统用户信息
     * @param pageJSON 页面JSON字符串
     * @return 返回用户编号
     */
    protected SysUserVO getSysUserInfo(PageJSON<?> pageJSON)
    {
        SysUserVO userInfo = new SysUserVO();
        if(null != pageJSON && null != pageJSON.getRequestAttrMap())
        {
            String userInfoJson = (String)pageJSON.getRequestAttrMap().get( Constants.USER_INFO );
            if(StringUtils.isNotBlank( userInfoJson ))
            {
                userInfo = BaseJsonUtil.parseObject( userInfoJson, SysUserVO.class );
            }

        }
        return userInfo;
    }
}
