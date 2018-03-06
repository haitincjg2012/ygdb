var keyValue = {
	menber: {
		menber: {
			accountInfo: [
                {name:"主账户",value:"ORG_MAIN_ACCOUNT"},
                {name:"子账户",value:"ORG_CHILD_ACCOUNT"},
                {name:"个体",value:"IND_MAIN_ACCOUNT"}
            ],
            userTypeList: [
            	{value: 'DB', name: '代办'},
            	{value: 'KS', name: '客商'},
            	{value: 'LK', name: '冷库'},
            	{value: 'ZZH', name: '种植户'},
            	{value: 'HZS', name: '合作社'}
					//HZS("合作社");
            ],
            customerType: [
            	{value: 'ORG_MAIN_ACCOUNT', name: '组织账户主体账号'},
            	{value: 'ORG_CHILD_ACCOUNT', name: '组织账户子级账号'},
            	{value: 'IND_MAIN_ACCOUNT', name: '个体账号'}
            ],
            userAccountType: [
            	{value: 'ORG_ACCOUNT', name: '组织账户'},
            	{value: 'IND_ACCOUNT', name: '个体账户'}
            ]
		}
	},
	transaction: {
		settlement: {
			typeList: [
                {name:"客商",value:"KS"},
                {name:"果农",value:"ZZH"}
			]
		},
		whistleBlowing: {
			realm: [
                {name:"交收单",value:"VOCHER"},
                {name:"帖子",value:"SOCIETYPOST"},
                {name:"供求",value:"PRODUCT"},
                {name:"行情",value:"ARTICLE"}
			],
			type:[
                {name:"举报",value:"JB"},
                {name:"反馈",value:"FK"}
			]
		}
	},
	integral: {
		customer: {
			editIntegral: [
            	{value: 'REGISTER_LOGIN', name: '注册登录'},
            	{value: 'DAY_FIRST_LOGIN', name: '每日首次登陆'},
            	{value: 'DAY_LOGIN_HOUR', name: '在线时长'},
            	{value: 'VERIFIED_INFO', name: '实名认证信息'},
            	{value: 'MEACH_SEND_ORDER', name: '累计上传吨位每满百吨即送'},
            	{value: 'SINGLE_ONCE_SEND_REQUEST', name: '发布需求即送'},
            	{value: 'SINGLE_ONE_SIGN_IN', name: '每天签到'},
            	{value: 'SUCCESS_QUOTATION', name: '行情竞猜赢积分'},
            	{value: 'SHARE_PROMOTION', name: '分享推广'},
            	{value: 'LOTTERY_LUCK', name: '抽奖获得积分'},
            	{value: 'EXCHANGE_PRODUCT', name: '积分兑换商品'},
            	{value: 'LOTTERY_PRODUCT', name: '抽奖兑换商品'},
            	{value: 'TRIGGER_DISABLED', name: '触发条件用户禁用'},
            	{value: 'GOODS_NOT_AGREE_FACT', name: '货物品质与价格描述跟事实严重不符'},
            	{value: 'EMBEZZLE_OTHERS_INFOMATION', name: '盗用其他用户资质，货品介绍，货品图片等图片，视频信息'},
            	{value: 'DEFAME_OTHERS_GOODS', name: '恶意诋毁他人货品'},
            	{value: 'CUMULATIVE_ASSESSMENT', name: '差评累积'},
            	{value: 'USER_REPORT', name: '举报'},
            	{value: 'PUBLISH_FALSE_INFORMATION', name: '恶意发布无关信息、虚假信息、虚假单据等'},
            	{value: 'REDUCE_VOCHER', name: '您删减了交收单据，系统扣除相应的分数'}
			]
		}
	},
	operation: {
		rank: {
			//实时列表
			actualTimeList: [
                {name:"是",value: true},
                {name:"否",value: false}
			],
			//排行榜类型
			rankingTypeList: [
                {name:"活跃达人",value: "ACTIVE_MAN"},
                {name:"调果达人",value: "FRUITING_PEOPLE"}
			],
			conditionsTypeList: [
                {name:"发布供求",value: "PUBLISH_PRODUCT"},
                {name:"发表评论",value: "GIVE_THE_COMMENT"},
                {name:"发布帖子",value: "PUBLISH_SOCIETYPOST"},
                {name:"调果量",value: "VOUCHER_TOTALNUM"},
                {name:"调果单数",value: "VOUCHER_NUM"},
                {name:"访问频率",value: "ACCESS_FREQUENCY"}
			],
			categoriesList: [
                {name:"上月",value: "LAST_MONTH"},
                {name:"累计",value: "TOTAL"}
			]
		},
		lottery: {
			numType: [
                {name:"每日一次",value: "DAILY_ONE"},
                {name:"每日两次",value: "DAILY_TWO"},
                {name:"一人一次",value: "PEOPLE_ONE"}
			],
			participateCon: [
                {name:"所以等级用户",value: "DAILY_ONE"},
                {name:"每日两次",value: "DAILY_TWO"},
                {name:"一人一次",value: "PEOPLE_ONE"}
			],
			prizeLevel: [
                {name:"一等奖",value: "FIRST_AWARD"},
                {name:"二等奖",value: "SECOND_AWARD"},
                {name:"三等奖",value: "THIRST_AWARD"},
                {name:"安慰奖",value: "CONSOLATION_AWARD"}
			]
		}
	},
	goods: {
		category: {
			attributeShowLevel: [
                {name:"首要显示",value: "FIRST"},
                {name:"次要显示",value: "SECOND"},
                {name:"其它",value: "OTHER"}
			]
		}
	}
}
export default keyValue;
