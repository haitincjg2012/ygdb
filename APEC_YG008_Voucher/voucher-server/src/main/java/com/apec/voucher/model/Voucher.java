package com.apec.voucher.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import com.apec.framework.common.Constants;
import com.apec.framework.common.enumtype.UserType;
import com.apec.framework.jpa.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 上传凭据Model
 * @author gunj
 * create by 2017-07-14
 * */
@Data
@Entity
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
@Table(name = "voucher")
public class Voucher extends BaseModel<Long>{

	private static final long serialVersionUID = -6561460455388065950L;
	
	/**
	 * 用户id
	 * */
	@Column(name = "user_id",nullable = false)
	private Long userId;
	
	/**
	 * 手机号
	 * */
	@Column(name = "mobile")
	private String mobile;
	
	/**
	 * 城市id
	 * */
	@Column(name = "city_id")
	private String cityId;
	
	/**
	 * 城市名称
	 * */
	@Column(name = "city_name")
	private String cityName;
	
	
	/**
	 * 县id
	 * */
	@Column(name = "county_id")
	private String countyId;
	
	/**
	 * 县名称
	 * */
	@Column(name = "county_name")
	private String countyName;
	
	/**
	 * 镇id
	 * */
	@Column(name = "town_id")
	private String townId;
	
	/**
	 * 镇名称
	 * */
	@Column(name = "town_name")
	private String townName;

	/**
	 * 卖货方类型：果农，客商
	 * */
	@Enumerated(value = EnumType.STRING)
	@Column(name = "type",nullable = false,length = 7)
	private UserType type;
	
	/**
	 * 出货仓库
	 * */
	@Column(name = "ship_warehouse",nullable = false, length = 50)
	private String shipWarehouse;
	
	/**
	 * 售货市场
	 * */
	@Column(name = "sale_market", nullable = false, length = 50)
	private String saleMarket;
	
	/**
	 * 买方姓名
	 * */
	@Column(name = "name", nullable = false, length = 30)
	private String name;
	
	/**
	 * 交收日期
	 * */
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	@Temporal(value = TemporalType.DATE)
	@Column(name = "delivery_time", nullable = false, length = 20)
	private Date deliveryTime;
	
	/**
	 * 凭据图片路径
	 * */
	@Column(name = "voucher_url")
	private String voucherUrl;

	/**
	 * 审核状态
	 */
	private String auditState;

	/**
	 * 通过状态
	 */
	private Date passDate;



}
