package com.apec.user.vo;

import com.apec.framework.dto.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * Created by hmy on 2017/9/1.
 * @author hmy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOrgImageVO extends BaseVO<Long> {

    /**
     * 地址
     */
    private String imageUrl;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 用户组织编号
     */
    private Long  userOrgId;

}
