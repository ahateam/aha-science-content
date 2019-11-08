import util from 'ahaapi'
import baseUrl from './url'

// let appId = process.env.appID;//应用编号
// let appId ='';


let api = {};
console.log('开始调用ctrl');

/** 内容相关*/
//获取内容列表
api.getContents = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/getContents', cnt, callback)
}
//获取内容列表
api.searchContents = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/searchContents', cnt, callback)
}
//修改内容
api.editContent = function (cnt, callback) {
    util.call(baseUrl + '/content/editContent', cnt, callback)
}


//获取专栏
api.getChannels = function (cnt, callback) {
    util.call(baseUrl + '/channel/getChannels', cnt, callback)
}
//添加内容
api.addContent = function (cnt, callback) {
    util.call(baseUrl + '/content/addContent', cnt, callback)
}
//删除内容
api.delContentById = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/delContentById', cnt, callback)
}
//添加专栏
api.createChannel = function (cnt, callback) {
    util.call(baseUrl + '/channel/createChannel', cnt, callback)
}
//添加专栏
api.createChannel = function (cnt, callback) {
    util.call(baseUrl + '/channel/createChannel', cnt, callback)
}
//禁用专栏
api.banChannel = function (cnt, callback) {
    util.call(baseUrl + '/channel/banChannel', cnt, callback)
}


//查询标签分组
api.getContentTagGroup = function (cnt, callback) {
    util.call(baseUrl + '/contentTag/getContentTagGroup', cnt, callback)
}
//创建标签分组
api.createContentTagGroup = function (cnt, callback) {
    util.call(baseUrl + '/contentTag/createContentTagGroup', cnt, callback)
}
//删除标签
api.delContentTag = function (cnt, callback) {
    util.call(baseUrl + '/contentTag/delContentTag', cnt, callback)
}


//修改任务状态
api.auditTask = function (cnt, callback) {
    util.call(baseUrl + '/task/auditTask', cnt, callback)
}
//获取标签列表
api.getTags = function (cnt, callback) {
    util.call(baseUrl + '/contentTag/getContentTag', cnt, callback)
}

//获取评论
api.getReplyList = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/getReplyList', cnt, callback)
}
//删除评论
api.delReply = function (cnt, callback) {
    util.call(baseUrl + '/reply/delReply', cnt, callback)
}

//查询申请
api.getApplyAuthoritys = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/getApplyAuthoritys', cnt, callback)
}
//审核申请
api.examineApplyAuthority = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/examineApplyAuthority', cnt, callback)
}


//获取内容标签
api.getContentTag = function (cnt, callback) {
    util.call(baseUrl + '/contentTag/getContentTag', cnt, callback)
}
//创建内容标签
api.createContentTag = function (cnt, callback) {
    util.call(baseUrl + '/contentTag/createContentTag', cnt, callback)
}
//修改内容标签
api.editteContentTag = function (cnt, callback) {
    util.call(baseUrl + '/contentTag/editteContentTag', cnt, callback)
}

//创建用户
api.register = function (cnt, callback) {
    util.call(baseUrl + '/ZskpUserService/register', cnt, callback)
}
//获取用户
api.getUsers = function (cnt, callback) {
    util.call(baseUrl + '/ZskpUserService/getUsers', cnt, callback)
}
//封禁用户
api.closeUser = function (cnt, callback) {
    util.call(baseUrl + '/ZskpUserService/closeUser', cnt, callback)
}
//修改用户信息
api.updateUserInfo = function (cnt, callback) {
    util.call(baseUrl + '/ZskpUserService/updateUserInfo', cnt, callback)
}
//用户登录
api.login = function (cnt, callback) {
    util.call(baseUrl + '/ZskpUserService/login', cnt, callback)
}
//创建广告
api.createAdvert = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/createAdvert', cnt, callback)
}
//删除广告
api.delAdvert = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/delAdvert', cnt, callback)
}
//修改广告
api.updateAdvert = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/updateAdvert', cnt, callback)
}
//查询广告
api.getAdverts = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/getAdverts', cnt, callback)
}
//查询用户兴趣标签
api.getInterestTags = function (cnt, callback) {
    util.call(baseUrl + '/ZskpUserService/getInterestTags', cnt, callback)
}
//查询报名
api.getEnrolls = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/getEnrolls', cnt, callback)
}

//修改专栏
api.editChannel = function (cnt, callback) {
    util.call(baseUrl + '/channel/editChannel', cnt, callback)
}
//删除专栏
api.delChannel = function (cnt, callback) {
    util.call(baseUrl + '/channel/delChannel', cnt, callback)
}

/* 科普基地景点 */
//查询景点列表
api.getTourBases = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/getTourBases', cnt, callback)
}
//创建景点
api.createTourBase = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/createTourBase', cnt, callback)
}
//删除景点
api.delupdateTourBase = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/delupdateTourBase', cnt, callback)
}
//修改景点
api.updateTourBase = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/updateTourBase', cnt, callback)
}
//查询关键词
api.getKeywords = function (cnt, callback) {
    util.call(baseUrl + '/ZskpKeyword/getKeywords', cnt, callback)
}
//创建关键词
api.createKeyword = function (cnt, callback) {
    util.call(baseUrl + '/ZskpKeyword/createKeyword', cnt, callback)
}
//删除关键词
api.delKeyword = function (cnt, callback) {
    util.call(baseUrl + '/ZskpKeyword/delKeyword', cnt, callback)
}


export default api

