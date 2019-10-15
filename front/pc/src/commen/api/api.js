import util from 'ahaapi'
import baseUrl from './url'

// let appId = process.env.appID;//应用编号
// let appId ='';


let api = {};
console.log('开始调用ctrl');

/** 内容相关*/
//获取内容列表
api.getContents = function (cnt, callback) {
    util.call(baseUrl + '/content/getContents', cnt, callback)
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
    util.call(baseUrl + '/content/delContentById', cnt, callback)
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


//修改任务状态
api.auditTask = function (cnt, callback) {
    util.call(baseUrl + '/task/auditTask', cnt, callback)
}
//获取标签列表
api.getTags = function (cnt, callback) {
    util.call(baseUrl + '/contentTag/getContentTag', cnt, callback)
}

//获取模板列表
api.getTemplates = function (cnt, callback) {
    util.call(baseUrl + '/task/getTemplates', cnt, callback)
}
//删除模板列表
api.delemplates = function (cnt, callback) {
    util.call(baseUrl + '/task/delemplates', cnt, callback)
}
//添加模板
api.addTemplate = function (cnt, callback) {
    util.call(baseUrl + '/task/createTemplates', cnt, callback)
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
//获取课程
api.getChannelContentTag = function (cnt, callback) {
    util.call(baseUrl + '/channelTag/getChannelContentTag', cnt, callback)
}
//获取课程
api.getChannelContentTag = function (cnt, callback) {
    util.call(baseUrl + '/channelTag/getChannelContentTag', cnt, callback)
}
//创建课程
api.createChannelContentTag = function (cnt, callback) {
    util.call(baseUrl + '/channelTag/createChannelContentTag', cnt, callback)
}
//查询课程
api.getChannelContentTag = function (cnt, callback) {
    util.call(baseUrl + '/channelTag/getChannelContentTag', cnt, callback)
}
//修改课程
api.updateChannelContentTag = function (cnt, callback) {
    util.call(baseUrl + '/channelTag/updateChannelContentTag', cnt, callback)
}
//修改专栏
api.editChannel = function (cnt, callback) {
    util.call(baseUrl + '/channel/editChannel', cnt, callback)
}

/* 科普基地景点 */
//查询景点列表
api.getTourBases = function (cnt, callback) {
    util.call(baseUrl + '/ZskpOtherContent/getTourBases', cnt, callback)
}


export default api

