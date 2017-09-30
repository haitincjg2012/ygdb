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
	 * 排名序号
	 * */
	private double rankNo;
	
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
	
	/**
	 * 重量单位
	 * */
	private String weight;
	
	/**
	 * 规格数量
	 * json字符串    key：规格；value：数量
	 * */
	private String attrNumberMap;
	
	/**
	 * 头像图片
	 * */
	private String imgUrl;
}
