package com.apec.message.dto;

import com.esms.common.entity.AccountInfo;
import com.esms.common.entity.BusinessType;
import lombok.Data;

/**
 * @author xxx
 */
@Data
public class SmsAccountInfoDTO {

	/**
	 * 账号信息
	 */
	private AccountInfo accountInfo;
	
	private BusinessType[] businessTypes;


}
