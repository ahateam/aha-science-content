package zyxhj.zskp.service;

import java.sql.SQLException;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

import zyxhj.utils.CacheCenter;
import zyxhj.utils.api.APIResponse;
import zyxhj.utils.api.Controller;


public class SmsServer extends Controller{
	public SmsServer(String node) {
		super(node);
		try {
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	@POSTAPI(//
		path = "sendSms", 
		des = "发送验证码", 
		ret = "" 
	)
	public APIResponse getEnrolls(
		@P(t = "模块编号") Long moduleId,
		@P(t = "手机号") String phone,
		@P(t = "类型",r = false) String type
	)  throws SQLException, ClientException {
		int num = (int)(Math.random()*(999999-100000+1)+100000);
		JSONObject json = new JSONObject();
		json.put("phone", phone);
		json.put("code", num);
		json.put("date", new Date());
		String TemplateCode = "";
		if("loginByCode".equals(type)) {//登录模板
			TemplateCode = "SMS_177547862";
		}else if("register".equals(type)) {//注册模板
			TemplateCode = "SMS_177537922";
		}else if("forgetPwd".equals(type)) {//重置密码模板
			TemplateCode = "SMS_177542871";
		}else {
			TemplateCode = "SMS_177537938";//身份验证模板
		}
		SendSmsResponse res = SendSms.sendSms(phone,num,TemplateCode);
		System.out.println("sms"+res.getCode());
		CacheCenter.PHONE_CODE.put(phone, json);
		return APIResponse.getNewSuccessResp();
		
	}
	
}
