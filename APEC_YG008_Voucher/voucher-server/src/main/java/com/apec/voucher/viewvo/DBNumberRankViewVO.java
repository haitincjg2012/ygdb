package com.apec.voucher.viewvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.apec.framework.common.enumtype.UserType;

/**
 * 代办数量排行信息
 * @author gunj
 * create by 2017-08-29
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DBNumberRankViewVO {
	
	/**
	 * 姓名
	 * */
	private String name;
	
	/**
	 * 用户类型
	 * */
	private UserType type;
	
	/**
	 * 总数
	 * */
	private double totalNumber;
}
