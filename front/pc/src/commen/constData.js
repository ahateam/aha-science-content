//全局静态常量

const constData = {}

constData.module = 1180,

constData.chnnelTagmodule = 11705

/* 栏目类型 */
constData.channelType = [{
		name: '栏目',
		value: 0
	},
	{
		name: '专题',
		value: 1
	}
]

/**contentList*/
constData.statusList = [{
		name: '草稿箱',
		value: 1,
	},
	{
		name: '未通过审核',
		value: 2,
	}, {
		name: '已关闭',
		value: 3,
	}, {
		name: '已发布',
		value: 4,
	},
]
constData.powerList = [{
		name: '免费',
		value: 0
	},
	{
		name: '付费',
		value: 1,
	}, {
		name: '会员',
		value: 2,
	}, {
		name: '会员付费',
		value: 3,
	}
]

constData.typeList = [{
		name: '图文',
		value: 5
	},
	{
		name: '视频',
		value: 3
	},
	{
		name: '直播',
		value: 4
	},
	{
		name: '活动',
		value: 8
	}, {
		name: '纯文本',
		value: '0'
	}
]

constData.typeListTemp = [{
	name: '图文',
	value: 5
}, {
	name: '纯文本',
	value: '0'
}]

constData.showList = [{
		name: '三图',
		value: 2,
	},
	{
		name: '右侧图',
		value: 1,
	},
	{
		name: '底部图',
		value: 0,
	}
]
constData.generalStatus = [{
		name: '启用',
		value: 0,
	},
	{
		name: '禁用',
		value: 1,
	}
]
constData.TaskType = [{
		name: '求表扬',
		value: 0,
	},
	{
		name: '求陪玩',
		value: 1,
	},
	{
		name: '分享',
		value: 2,
	},
	{
		name: '制作',
		value: 3,
	}
]
constData.templateType = [{
		name: '图文',
		value: 0,
	},
	{
		name: '视频',
		value: 1,
	},
	{
		name: 'GIF表情',
		value: 2,
	},
	{
		name: '音频',
		value: 3,
	},
	{
		name: '陪吃',
		value: 4,
	}
]
constData.tagstatus = [{
		name: '启用',
		value: 1,
	},
	{
		name: '禁用',
		value: 0,
	}
]

constData.authList = [{
	name: '超级管理员权限',
	id: '4'
},{
	name: '管理员权限',
	id: '3'
}, {
	name: '内部人员权限',
	id: '2'
}, {
	name: '普通用户权限',
	id: '1'
}]
constData.advertTypeList = [{
	name: 'APP主页广告',
	id: '0'
}, {
	name: '科普栏目广告',
	id: '1'
}, {
	name: '启动页广告',
	id: '2'
}]

export default constData
