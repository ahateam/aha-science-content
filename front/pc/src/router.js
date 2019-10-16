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
//评论
const replyList = () => import('./views/reply/replyList')
const replyInfo = () => import('./views/reply/replyInfo')
//专题
const svipList = () => import('./views/svip/svipList')
const svipInfoList = () => import('./views/svip/svipInfoList')
const addSvip = () => import('./views/svip/addSvip')
//活动
const activityList = () => import('./views/activity/activityList')
const addActivity = () => import('./views/activity/addActivity')
//用户
const userList = () => import('./views/user/userList')
const userInfo = () => import('./views/user/userInfo')
const adminList = () => import('./views/user/adminList')
const insiderList = () => import('./views/user/insiderList')
//标签
const homeTag = () => import('./views/tag/homeTag')
const tagGroup = () => import('./views/tag/tagGroup')

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
				//专栏
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
