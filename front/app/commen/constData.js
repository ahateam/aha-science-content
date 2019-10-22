const constData = {}

/** 首页常量*/
constData.module = 1180

constData.userStatus = [{
		key: 0,
		val: '正常'
	},
	{
		key: 1,
		val: '被禁言'
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
		val: '掌上科普首页'
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
}, {
	key: '8',
	val: '科普活动'
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

export default constData
