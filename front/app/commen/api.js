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
/************************************模板相关 */
//获取模板列表 
api.getTemplates = function(cnt, callback) {
	util.call(baseUrl + '/task/getTemplates', cnt, callback)
}

//获取模板
api.getTemplate = function(cnt, callback) {
	util.call(baseUrl + '/task/getTemplate', cnt, callback)
}

/* 支付相关 ****************************************/
//支付宝小程序支付 
api.creatAlipayAppletOrder = function(cnt, callback) {
	util.call(baseUrl + '/alipay/creatAlipayAppletOrder', cnt, callback)
}

//支付宝app支付
api.creatAlipayOrder = function(cnt, callback) {
	util.call(baseUrl + '/alipay/creatAlipayOrder', cnt, callback)
}

//微信小程序支付 
api.doUnifiedOrder = function(cnt, callback) {
	util.call(baseUrl + '/wxpay/doUnifiedOrder', cnt, callback)
}

//购买课程 
api.PayChannelContentTag = function(cnt, callback) {
	util.call(baseUrl + '/channel/PayChannelContentTag', cnt, callback)
}

//获取订单信息 
api.getPayOrderByUserId = function(cnt, callback) {
	util.call(baseUrl + '/task/getPayOrderByUserId', cnt, callback)
}

//查询用户购买的课程 
api.getPayChannel = function(cnt, callback) {
	util.call(baseUrl + '/task/getPayChannel', cnt, callback)
}

//更新订单信息  
api.updatePayOrder = function(cnt, callback) {
	util.call(baseUrl + '/task/updatePayOrder', cnt, callback)
}
export default api
