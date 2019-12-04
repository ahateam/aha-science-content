import Vue from 'vue'
import Router from 'vue-router'
import home from './views/home'
import login from './views/login'


//内容
const contentList = () => import('./views/content/contentList')
const videoContentList = () => import('./views/content/videoContentList')
const contentInfo = () => import('./views/content/contentInfo')
const examineList = () => import('./views/content/examineList')
const addContent = () => import('./views/addContent/addContent')
const editContent = () => import('./views/content/editContent')
const editVideo = () => import('./views/content/editVideo')
const addVideoContent = () => import('./views/addContent/addVideoContent')
const contentListByDraft = () => import('./views/content/contentListByDraft')

//评论
const replyList = () => import('./views/reply/replyList')
const replyInfo = () => import('./views/reply/replyInfo')
const commentList = () => import('./views/reply/commentList')
//栏目
const svipList = () => import('./views/svip/svipList')
const svipInfoList = () => import('./views/svip/svipInfoList')
const addSvip = () => import('./views/svip/addSvip')
//活动
const activityList = () => import('./views/activity/activityList')
const addActivity = () => import('./views/activity/addActivity')
const addTourBase = () => import('./views/activity/addTourBase')
const tourBaseList = () => import('./views/activity/tourBaseList')
const tourBaseInfo = () => import('./views/activity/tourBaseInfo')
const editActivity = () => import('./views/activity/editActivity')
const editTourBase = () => import('./views/activity/editTourBase')

//广告
const advertList = () => import('./views/advert/advertList')
const addAdvert = () => import('./views/advert/addAdvert')
//用户
const userList = () => import('./views/user/userList')
const userInfo = () => import('./views/user/userInfo')
const adminList = () => import('./views/user/adminList')
const insiderList = () => import('./views/user/insiderList')
const addUser = () => import('./views/user/addUser')
const superAdminList = () => import('./views/user/superAdminList')

//标签
const homeTag = () => import('./views/tag/homeTag')
const tagGroup = () => import('./views/tag/tagGroup')
//申请
const insiderExamineList = () => import('./views/examine/insiderExamineList')
const StringFilterList = () => import('./views/examine/StringFilterList')
//数据
const interestTagList = () => import('./views/dataInfo/interestTagList')
const enrollList = () => import('./views/dataInfo/enrollList')
//专题
const addColumn = () => import('./views/channel/addColumn')
const columnInfo = () => import('./views/channel/columnInfo')
const columnList = () => import('./views/channel/columnList')


Vue.use(Router)

export default new Router({
	mode: 'hash',
	base: process.env.BASE_URL,
	routes: [{
			path: '/',
			name: 'login',
			component: login,
		},
		{
			path: '/content',
			name: 'home',
			component: home,
			children: [
				//内容
				{
					path: '/contentList',
					name: 'contentList',
					component: contentList
				},
				{
					path: '/contentInfo',
					name: 'contentInfo',
					component: contentInfo
				},
				{
					path: '/examineList',
					name: 'examineList',
					component: examineList
				},
				{
					path: '/addContent',
					name: 'addContent',
					component: addContent
				},
				{
					path: '/addVideoContent',
					name: 'addVideoContent',
					component: addVideoContent
				},
				{
					path: '/contentListByDraft',
					name: 'contentListByDraft',
					component: contentListByDraft
				},
				{
					path: '/editContent',
					name: 'editContent',
					component: editContent
				},
				{
					path: '/editVideo',
					name: 'editVideo',
					component: editVideo
				}, {
					path: '/videoContentList',
					name: 'videoContentList',
					component: videoContentList
				},
				//栏目
				{
					path: '/svipList',
					name: 'svipList',
					component: svipList
				},
				{
					path: '/svipInfoList',
					name: 'svipInfoList',
					component: svipInfoList
				}, {
					path: '/addSvip',
					name: 'addSvip',
					component: addSvip
				},
				//活动
				{
					path: '/activityList',
					name: 'activityList',
					component: activityList
				}, {
					path: '/addActivity',
					name: 'addActivity',
					component: addActivity
				}, {
					path: '/addTourBase',
					name: 'addTourBase',
					component: addTourBase
				}, {
					path: '/tourBaseList',
					name: 'tourBaseList',
					component: tourBaseList
				},{
					path: '/tourBaseInfo',
					name: 'tourBaseInfo',
					component: tourBaseInfo
				},{
					path: '/editActivity',
					name: 'editActivity',
					component: editActivity
				},{
					path: '/editTourBase',
					name: 'editTourBase',
					component: editTourBase
				},
				//评论
				{
					path: '/replyList',
					name: 'replyList',
					component: replyList
				}, {
					path: '/replyInfo',
					name: 'replyInfo',
					component: replyInfo
				},{
					path: '/commentList',
					name: 'commentList',
					component: commentList
				},
				//用户
				{
					path: '/userList',
					name: 'userList',
					component: userList
				},
				
				{
					path: '/userInfo',
					name: 'userInfo',
					component: userInfo
				},
				{
					path: '/insiderList',
					name: 'insiderList',
					component: insiderList
				},
				{
					path: '/adminList',
					name: 'adminList',
					component: adminList
				},{
					path: '/addUser',
					name: 'addUser',
					component: addUser
				},
				{
					path: '/superAdminList',
					name: 'superAdminList',
					component: superAdminList
				},
				
				//广告
				{
					path: '/advertList',
					name: 'advertList',
					component: advertList
				},
				{
					path: '/addAdvert',
					name: 'addAdvert',
					component: addAdvert
				},
				//标签
				{
					path: '/homeTag',
					name: 'homeTag',
					component: homeTag
				},
				{
					path: '/tagGroup',
					name: 'tagGroup',
					component: tagGroup
				},
				{
					path: '/insiderExamineList',
					name: 'insiderExamineList',
					component: insiderExamineList
				},
				{
					path: '/enrollList',
					name: 'enrollList',
					component: enrollList
				},{
					path: '/interestTagList',
					name: 'interestTagList',
					component: interestTagList,
					//专题
				},{
					path: '/addColumn',
					name: 'addColumn',
					component: addColumn
				},{
					path: '/columnInfo',
					name: 'columnInfo',
					component: columnInfo
				},{
					path: '/columnList',
					name: 'columnList',
					component: columnList
				},{
					path: '/StringFilterList',
					name: 'StringFilterList',
					component: StringFilterList
				}
				
			]
		},
		{
			path: '/login',
			name: 'login',
			component: login
		}
	

	]
})
