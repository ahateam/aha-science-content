//全局静态常量

const constData = {}

constData.module = 1180,
constData.chnnelTagmodule = 11705

/**contentList*/
constData.statusList = [{
		name: '待审核',
		value: 1,
	},
	{
		name: '未通过审核',
		value: 2,
	}, {
		name: '已关闭',
		value: 3,
	},  {
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
		name: '直播',
		value: 4
	},
	{
		name: '纯文本',
		value: 0
	},
	{
		name: '帖子',
		value: 6
	},
	{
		name: '活动',
		value: 8
	},
]

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
constData.tagstatus =  [{
		name: '启用',
		value: 1,
	},
	{
		name: '禁用',
		value: 0,
	}
]

export default constData
