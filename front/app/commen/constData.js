const constData = {}

/** 首页常量*/
constData.module = 1180

/* 支付平台常量 */
constData.payInfoList = [{
		key: 0,
		id: 'wxpay',
		name: '微信支付',
		value: 0
	},
	{
		key: 1,
		id: 'toutiao',
		name: '头条支付',
		value: 1
	},
	{
		key: 2,
		id: 'qq',
		name: 'qq支付',
		value: 2
	},
	{
		key: 3,
		id: 'sinaweibo',
		name: '新浪微博支付',
		value: 3
	},
	{
		key: 4,
		id: 'xiaomi',
		name: '小米支付',
		value: 4
	},
	{
		key: 5,
		id: 'alipay',
		name: '支付宝支付',
		value: 5
	},
	{
		key: 6,
		id: 'baidu',
		name: '百度支付',
		value: 6
	}
]

/* 登录平台常量 */
constData.providerList = [{
		key: 0,
		id: 'weixin',
		name: '微信登录'
	},
	{
		key: 1,
		id: 'toutiao',
		name: '头条登录'
	},
	{
		key: 2,
		id: 'qq',
		name: 'qq登录'
	},
	{
		key: 3,
		id: 'sinaweibo',
		name: '新浪微博登录'
	},
	{
		key: 4,
		id: 'xiaomi',
		name: '小米登录'
	},
	{
		key: 5,
		id: 'alipay',
		name: '支付宝登录'
	},
	{
		key: 6,
		id: 'baidu',
		name: '百度登录'
	}
]

/** 资讯内容状态*/
constData.contentStatus = [{
	key: 0,
	val: '草稿'
}, {
	key: 1,
	val: '未发布'
}, {
	key: 2,
	val: '已关闭'
}, {
	key: 3,
	val: '已删除'
}, {
	key: 4,
	val: '已发布'
}, {
	key: 5,
	val: '发布失败'
}]

/* 资讯标签以及专栏状态*/
constData.tagStatus = [{
		key: '0',
		val: '禁用'
	}, {
		key: '1',
		val: '启用'
	}],

/* 标签分组 */
constData.tagGroupType = [{
		key: '0',
		val: '首页'
	},
	{
		key: '1',
		val: 'VIP'
	},
	{
		key: '2',
		val: '任务墙'
	},
	{
		key: '3',
		val: '模板'
	}
]

/* 首页文章展示样式 */
constData.contentShow = [{
	key: '0',
	val: '底部展示'
}, {
	key: '1',
	val: '右侧展示'
}, {
	key: '2',
	vak: '文章三图or视频竖屏'
}]

/* 资讯类型 */
constData.contentType = [{
	key: '0',
	val: '纯文本'
}, {
	key: '3',
	val: '视频'
}, {
	key: '5',
	val: '图文'
}]

/* 是否付费 */
constData.contentPaid = [{
	key: '0',
	val: '免费'
}, {
	key: '1',
	val: '付费'
}]

/* 点赞 */
constData.appraise = [{
		key: '0',
		val: '赞'
	},
	{
		key: '1',
		val: '踩'
	}
]

/* 渐变色数组 */
constData.colorData = [
	'linear-gradient(120deg,#69b5e1,#bedbed)',
	'linear-gradient(90deg,#ff9a9e,#fad0c4)',
	'linear-gradient(151deg,#a18cd1,#fbc2eb)',
	'linear-gradient(65deg,#465EFB,#C2FFD8)',
	'linear-gradient(71deg,#fcb69f,#ffecd2)',
	'linear-gradient(123deg,#ff8177,#b12a5b)',
	'linear-gradient(30deg,#114357,#F29492)',
	'linear-gradient(40deg,#64b3f4,#c2e59c)',
	'linear-gradient(50deg,#fc00ff,#00dbde)'
]

export default constData
