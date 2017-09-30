package com.apec.user.dao;

import com.apec.framework.common.PageDTO;
import com.apec.user.dto.UserDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by hmy on 2017/9/19.
 */
public interface FocusUserOrgClientDAO{

    /**
     * 查询我的关注的具体信息
     *
     * @return Object[][]
     */
    PageDTO<Object[]> findMyFocusF(List<Long> userOrgIds, Pageable pageable, UserDTO userDTO);

}
