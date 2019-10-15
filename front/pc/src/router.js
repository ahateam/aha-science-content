import Vue from 'vue'
import Router from 'vue-router'
import home from './views/home'
import login from './views/login'

//内容
const contentList = () => import('./views/content/contentList')
const imageTextContentList = () => import('./views/content/imageTextContentList')

const contentInfo = () => import('./views/content/contentInfo')
const examineList = () => import('./views/content/examineList')
const addContent = () => import('./views/addContent/addContent')
const editContent = () => import('./views/content/editContent')
const editVideo = () => import('./views/content/editVideo')
const addVideoContent = ()=>import('./views/addContent/addVideoContent')

//专栏
const svipList = () => import('./views/svip/svipList')
const svipInfoList = () => import('./views/svip/svipInfoList')
const addSvip = () => import('./views/svip/addSvip')
const addSvipTag = () => import('./views/svip/addSvipTag')
const svipTagList = () => import('./views/svip/svipTagList')
const editSvipTagList = () => import('./views/svip/editSvipTagList')

//任务墙
const taskList = () => import('./views/taskPage/taskList')
const taskInfo = () => import('./views/taskPage/taskInfo')
const examineTask = () => import('./views/taskPage/examineTask')


//模板
const templateList = () => import('./views/template/templateList')
const templateInfo = () => import('./views/template/templateInfo')
const addTemplate = () => import('./views/template/addTemplate')

//用户
const userList = () => import('./views/user/userList')
const userInfo = () => import('./views/user/userInfo')

//标签
const homeTag = () => import('./views/tag/homeTag')
const vipTag = () => import('./views/tag/vipTag')
const channelTag = () => import('./views/tag/channelTag')

Vue.use(Router)

export default new Router({
    mode: 'hash',
    base: process.env.BASE_URL,
    routes: [
        {
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
                },{
                    path: '/addSvip',
                    name: 'addSvip',
                    component: addSvip
                },{
                    path: '/addSvipTag',
                    name: 'addSvipTag',
                    component: addSvipTag
                },{
                    path: '/svipTagList',
                    name: 'svipTagList',
                    component: svipTagList
                },{
                    path: '/editSvipTagList',
                    name: 'editSvipTagList',
                    component: editSvipTagList
                },
                //任务墙
                {
                    path: '/taskInfo',
                    name: 'taskInfo',
                    component: taskInfo
                },
                {
                    path: '/taskList',
                    name: 'taskList',
                    component: taskList
                },
                {
                    path: '/examineTask',
                    name: 'examineTask',
                    component: examineTask
                },
                //模板
                {
                    path: '/templateList',
                    name: 'templateList',
                    component: templateList
                },
                {
                    path: '/templateInfo',
                    name: 'templateInfo',
                    component: templateInfo
                },
                {
                    path: '/addTemplate',
                    name: 'addTemplate',
                    component: addTemplate
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
				//标签
				{
				    path: '/homeTag',
				    name: 'homeTag',
				    component: homeTag
				},
				{
				    path: '/vipTag',
				    name: 'vipTag',
				    component: vipTag
				},
				{
				    path: '/channelTag',
				    name: 'channelTag',
				    component: channelTag
				},
            ]
        },
        {
            path: '/login',
            name: 'login',
            component: login
        }


    ]
})
