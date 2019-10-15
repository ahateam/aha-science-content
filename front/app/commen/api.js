import util from 'ahaapi'
import baseUrl from './url'

// let appId = process.env.appID;//应用编号
// let appId ='';

let api = {};
console.log('开始调用ctrl');

/********************************** 内容接口 **********************************/
// 获取标签<创建内容，保存为正常（已发布）>

api.getContentTag = function(cnt, callback) {
	util.call(baseUrl + '/contentTag/getContentTag', cnt, callback)
}

//根据标签获取 内容列表
api.getContents = function(cnt, callback) {
	util.call(baseUrl + '/content/getContents', cnt, callback)
}
	
//根据id获取内容
api.getContentById = function(cnt, callback) {
	util.call(baseUrl + '/content/getConntent', cnt, callback)
}

/********************* 专栏接口 ******************************/ 
//获取专栏列表 
api.getChannel = function(cnt, callback) {
	util.call(baseUrl + '/channel/getChannels', cnt, callback)
}

//根据专栏id获取专栏 
api.getChannlById = function(cnt, callback) {
	util.call(baseUrl + '/channel/getChannlById', cnt, callback)
}

//获取专栏下内容 
api.getContentByChannelId = function(cnt, callback) {
	util.call(baseUrl + '/channel/getContentByChannelId', cnt, callback)
}

//根据专栏id获取专栏内容标签(channlContentTag)
api.getChannlContentTagByChannelId = function(cnt, callback) {
	util.call(baseUrl + '/channel/getChannlContentTagByChannelId', cnt, callback)
}

//查询用户是否购买课程 
api.getChannelContentTagPower = function(cnt, callback) {
	util.call(baseUrl + '/channel/getChannelContentTagPower', cnt, callback)
}

/************************ 用户相关接口 **************************/
//注册 
api.register = function(cnt, callback) {
	util.call(baseUrl + '/ZskpUserService/register', cnt, callback)
}

//平台账号登录（手机号） 
api.login = function(cnt, callback) {
	util.call(baseUrl + '/ZskpUserService/login', cnt, callback)
}

//根据id获取用户
api.getUserById = function(cnt, callback) {
	util.call(baseUrl + '/weixin/getUserById', cnt, callback)
}

//微信获取用户openid
api.getAccessToken = function(cnt, callback) {
	util.call(baseUrl + '/weixin/getAccessToken', cnt, callback)
}
//微信登录 
api.loginByWxOpenId = function(cnt, callback) {
	util.call(baseUrl + '/weixin/loginByWxOpenId', cnt, callback)
}

//头条获取openid  
api.ttGetSessionkey = function(cnt, callback) {
	util.call(baseUrl + '/otherLogin/ttGetSessionkey', cnt, callback)
}

//头条登录
api.loginByTtOpenId = function(cnt, callback) {
	util.call(baseUrl + '/otherLogin/loginByTtOpenId', cnt, callback)
}

//支付宝获取openid 
api.alipayGetSessionkey = function(cnt, callback) {
	util.call(baseUrl + '/otherLogin/alipayGetSessionkey', cnt, callback)
}

//支付宝登录
api.loginByAlipayOpenId = function(cnt, callback) {
	util.call(baseUrl + '/otherLogin/loginByAlipayOpenId', cnt, callback)
}
/*************************************** 点赞评论接口 ********************************************/
//点赞 
api.createUpvote = function(cnt, callback) {
	util.call(baseUrl + '/appraise/createAppraise', cnt, callback)
}

//获取点赞数量 
api.getAppraiseCount = function(cnt, callback) {
	util.call(baseUrl + '/appraise/getAppraiseCount', cnt, callback)
}

//获取评论列表 
api.getCommentByContentId = function(cnt, callback) {
	util.call(baseUrl + '/reply/getReplyList', cnt, callback)
}

//评论 
api.createReply = function(cnt, callback) {
	util.call(baseUrl + '/reply/createReply', cnt, callback)
}

/* 科普活动 */
//查询科普基地详情 
api.getTourBase = function(cnt, callback) {
	util.call(baseUrl + '/ZskpOtherContent/getTourBase', cnt, callback)
}

//查询科普基地的景点 
api.getTourBases = function(cnt, callback) {
	util.call(baseUrl + '/ZskpOtherContent/getTourBases', cnt, callback)
}

//活动报名 
api.createEnroll = function(cnt, callback) {
	util.call(baseUrl + '/ZskpOtherContent/createEnroll', cnt, callback)
}
/* 广告 */
//查询广告 
api.getAdverts = function(cnt, callback) {
	util.call(baseUrl + '/ZskpOtherContent/getAdverts', cnt, callback)
}
export default api
