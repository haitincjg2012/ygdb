package com.apec.message.vo;

import com.apec.framework.common.enumtype.MessageTemplate;
import com.apec.framework.dto.BaseVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title: SMS 验证码
 *
 * @author yirde  2017/7/21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsCaptchaVO extends BaseVO<Long> {

	/**
	 * 手机号
	 * */
    private String mobile;
    
    /**
     * 模板Key
     * */
    private MessageTemplate templateKey;


}
