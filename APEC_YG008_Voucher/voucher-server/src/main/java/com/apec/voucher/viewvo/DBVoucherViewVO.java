package com.apec.voucher.viewvo;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.apec.framework.common.PageDTO;

/**
 * 后台代办分析viewVO
 * @author gunj
 * create by 2017-08-25
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DBVoucherViewVO {

	/**
	 * 规格占比信息；key:规格，value：占比
	 * */
	Map<String, String> proportionMap;
	
	/**
	 * 数量排行信息
	 * */
	PageDTO<DBNumberRankViewVO> dbNumberRankViewVOPage;
}
