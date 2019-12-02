/*左侧导航栏菜单生成的内容*/
const supermenu = [{

		title: '内容管理',
		icon: 'course_guanli1',
		child: [{
				path: '/ContentList',
				title: '图文管理',
			},
			{
				path: '/videoContentList',
				title: '视频管理',
			},
			{
				path: '/replyList',
				title: '评论管理'
			}
		]
	},
	{
		title: '科普栏目管理',
		icon: 'course_xueyuanpeizhi',
		child: [{
			path: '/svipList',
			title: '栏目页面列表',
		}]
	},
	{
		title: '专题管理',
		icon: 'course__zhuanzhengongzuoliang',
		child: [{
			path: '/columnList',
			title: '专题列表',
		}]
	},
	{
		title: '活动管理',
		icon: 'course_tongzhi',
		child: [{
				path: '/activityList',
				title: '活动管理'
			},
			{
				path: '/addActivity',
				title: '发布活动'
			}
		]
	},
	{
		title: '科普基地管理',
		icon: 'course_shouye',
		child: [{
			path: '/tourBaseList',
			title: '基地列表'
		}]
	},
	{
		title: '关键词管理',
		icon: 'course_908caidan_anpai',
		child: [{
			path: '/homeTag',
			title: '关键词列表'
		}, {
			path: '/tagGroup',
			title: '新建关键词'
		}]
	},
	{
		title: '广告管理',
		icon: 'course_tongzhi1',
		child: [{
			path: '/advertList',
			title: '广告列表'
		}, {
			path: '/addAdvert',
			title: '发布广告'
		}, ],
	},
	{
		title: '用户管理',
		icon: 'course_user',
		child: [{
			path: '/superAdminList',
			title: '超级管理员管理'
		}, {
			path: '/adminList',
			title: '管理员管理'
		}, {
			path: '/insiderList',
			title: '内部人员管理'
		}, {
			path: '/userList',
			title: '用户管理'
		}, ]
	},
	{
		title: '审核管理',
		icon: 'course_jilu',
		child: [{
				path: '/insiderExamineList',
				title: '内部人员审核'
			},
			{
				path: '/StringFilterList',
				title: '敏感词管理'
			}
		]
	}, {
		title: '数据统计',
		icon: 'course_tongji',
		child: [{
				path: '/interestTagList',
				title: '用户兴趣标签'
			},
			{
				path: '/enrollList',
				title: '活动报名'
			}
		]
	},
]
const menu = [{

		title: '内容管理',
		icon: 'course_guanli1',
		child: [{
				path: '/ContentList',
				title: '图文管理',
			},
			{
				path: '/videoContentList',
				title: '视频管理',
			},
			{
				path: '/replyList',
				title: '评论管理'
			}
		]
	},
	{
		title: '科普栏目管理',
		icon: 'course_xueyuanpeizhi',
		child: [{
			path: '/svipList',
			title: '栏目页面列表',
		}]
	},
	{
		title: '专题管理',
		icon: 'course__zhuanzhengongzuoliang',
		child: [{
			path: '/columnList',
			title: '专题列表',
		}]
	},
	{
		title: '活动管理',
		icon: 'course_tongzhi',
		child: [{
				path: '/activityList',
				title: '活动管理'
			},
			{
				path: '/addActivity',
				title: '发布活动'
			}
		]
	},
	{
		title: '科普基地管理',
		icon: 'course_shouye',
		child: [{
			path: '/tourBaseList',
			title: '基地列表'
		}]
	},
	{
		title: '关键词管理',
		icon: 'course_908caidan_anpai',
		child: [{
			path: '/homeTag',
			title: '关键词列表'
		}, {
			path: '/tagGroup',
			title: '新建关键词'
		}]
	},
	{
		title: '广告管理',
		icon: 'course_tongzhi1',
		child: [{
			path: '/advertList',
			title: '广告列表'
		}, {
			path: '/addAdvert',
			title: '发布广告'
		}, ],
	},
	{
		title: '用户管理',
		icon: 'course_user',
		child: [{
			path: '/adminList',
			title: '管理员管理'
		}, {
			path: '/insiderList',
			title: '内部人员管理'
		}, {
			path: '/userList',
			title: '用户管理'
		}, ]
	},
	{
		title: '审核管理',
		icon: 'course_jilu',
		child: [{
				path: '/insiderExamineList',
				title: '内部人员审核'
			},
			{
				path: '/StringFilterList',
				title: '敏感词管理'
			}
		]
	}, {
		title: '数据统计',
		icon: 'course_tongji',
		child: [{
				path: '/interestTagList',
				title: '用户兴趣标签'
			},
			{
				path: '/enrollList',
				title: '活动报名'
			}
		]
	},
]

const insiderMenu = [{

		title: '内容管理',
		icon: 'course_shouye',
		child: [{
				path: '/ContentList',
				title: '图文管理',
			},
			{
				path: '/videoContentList',
				title: '视频管理',
			},
			{
				path: '/replyList',
				title: '评论管理'
			}
		]
	},
	{
		title: '活动管理',
		icon: 'course_shouye',
		child: [{
				path: '/activityList',
				title: '活动管理'
			},
			{
				path: '/addActivity',
				title: '发布活动'
			}
		]
	}
]

const userMenu = [{
	title: '内容管理',
	icon: 'course_shouye',

}]


export default {
	supermenu,
	menu,
	insiderMenu,
	userMenu,
}
