package com.apec.message.dto;

import com.esms.common.entity.AccountInfo;
import com.esms.common.entity.BusinessType;

public class SmsAccountInfoDTO {

	private AccountInfo accountInfo;
	
	private BusinessType[] businessTypes;

	public AccountInfo getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}

	public BusinessType[] getBusinessTypes() {
		return businessTypes;
	}

	public void setBusinessTypes(BusinessType[] businessTypes) {
		this.businessTypes = businessTypes;
	}
}
