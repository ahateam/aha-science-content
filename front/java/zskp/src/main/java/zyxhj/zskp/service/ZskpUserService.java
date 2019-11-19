package zyxhj.zskp.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zyxhj.cms.domian.Content;
import zyxhj.cms.repository.ContentRepository;
import zyxhj.utils.CacheCenter;
import zyxhj.utils.IDUtils;
import zyxhj.utils.Singleton;
import zyxhj.utils.api.APIResponse;
import zyxhj.utils.api.Controller;
import zyxhj.utils.api.RC;
import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.EXP;
import zyxhj.zskp.domain.ApplyAuthority;
import zyxhj.zskp.domain.UserFavorites;
import zyxhj.zskp.domain.ZskpUser;
import zyxhj.zskp.repository.ApplyAuthorityRepository;
import zyxhj.zskp.repository.UserFavoritesRepository;
import zyxhj.zskp.repository.UserRepository;

public class ZskpUserService extends Controller{
	private UserRepository userRepository;
	private ApplyAuthorityRepository applyAuthorityRepository;
	private UserFavoritesRepository favoritesRepository;
	private ContentRepository contentRepository;
	private DruidDataSource ds;
	public ZskpUserService(String node) {
		super(node);
		try {
			ds = DataSource.getDruidDataSource("rdsDefault.prop");
			userRepository = Singleton.ins(UserRepository.class);
			applyAuthorityRepository = Singleton.ins(ApplyAuthorityRepository.class);
			favoritesRepository = Singleton.ins(UserFavoritesRepository.class);
			contentRepository = Singleton.ins(ContentRepository.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 登录
	 * @throws ServerException 
	 * @throws SQLException 
	 */
	@POSTAPI(//
		path = "login", 
		des = "登录", 
		ret = "" 
	)
	public APIResponse login(
		@P(t = "模块编号") String moduleId,
		@P(t = "手机号") String phone,
		@P(t = "密码") String pwd
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			ZskpUser user = userRepository.get(conn, EXP.INS().key("phone", phone).andKey("pwd", pwd).andKey("module_id",moduleId));
			if(user != null) {
				user.pwd ="***";
				return APIResponse.getNewSuccessResp(user);
			}else {
				return APIResponse.getNewFailureResp(new RC("fail", "登录失败"));
			}
		}
	}
	@POSTAPI(//
			path = "setNewPwd", 
			des = "设置新密码", 
			ret = "" 
		)
		public APIResponse setNewPwd(
			@P(t = "模块编号") String moduleId,
			@P(t = "手机号") String phone,
			@P(t = "原密码") String pwd,
			@P(t = "新密码") String newPwd
		) throws ServerException, SQLException {
			try(DruidPooledConnection conn = ds.getConnection()){
				ZskpUser user = userRepository.get(conn, EXP.INS().key("phone", phone).andKey("pwd", pwd).andKey("module_id",moduleId));
				if(user != null) {
					ZskpUser userPwd = new ZskpUser();
					userPwd.pwd = newPwd;
					userRepository.update(conn, EXP.INS().key("phone", phone).andKey("module_id",moduleId), userPwd,true);
					return APIResponse.getNewSuccessResp(user);
				}else {
					return APIResponse.getNewFailureResp(new RC("fail", "失败,没有找到此用户信息"));
				}
			}
		}
	@POSTAPI(//
			path = "loginByCode", 
			des = "手机验证码登录", 
			ret = "" 
		)
		public APIResponse loginByCode(
			@P(t = "模块编号") String moduleId,
			@P(t = "手机号") String phone,
			@P(t = "验证码") String code
		) throws ServerException, SQLException {
			try(DruidPooledConnection conn = ds.getConnection()){
				JSONObject json = CacheCenter.PHONE_CODE.getIfPresent(phone);
				if(json !=null) {
					if(phone.equals(json.getString("phone")) && code.equals(json.getString("code"))) {
						ZskpUser user = userRepository.get(conn, EXP.INS().key("phone", phone).andKey("module_id",moduleId));
						if(user != null) {
							user.pwd ="***";
							return APIResponse.getNewSuccessResp(user);
						}else {
							return APIResponse.getNewFailureResp(new RC("fail", "没有此用户信息，登录失败"));
						}
					}else {
						return APIResponse.getNewFailureResp(new RC("fail", "验证码错误"));
					}
				}else {
					return APIResponse.getNewFailureResp(new RC("fail", "手机验证码过期"));
				}
			}
		}
	
	@POSTAPI(//
			path = "forgetPwd", 
			des = "手机验证码找回密码", 
			ret = "" 
		)
		public APIResponse forgetPwd(
			@P(t = "模块编号") String moduleId,
			@P(t = "手机号") String phone,
			@P(t = "密码") String pwd,
			@P(t = "验证码") String code
		) throws ServerException, SQLException {
			try(DruidPooledConnection conn = ds.getConnection()){
				JSONObject json = CacheCenter.PHONE_CODE.getIfPresent(phone);
				if(json !=null) {
					if(phone.equals(json.getString("phone")) && code.equals(json.getString("code"))) {
						ZskpUser user = userRepository.get(conn, EXP.INS().key("phone", phone).andKey("module_id",moduleId));
						if(user != null) {
							ZskpUser userPwd = new ZskpUser();
							userPwd.pwd = pwd;
							userRepository.update(conn, EXP.INS().key("phone", phone).andKey("module_id",moduleId), userPwd,true);
							return APIResponse.getNewSuccessResp(user);
						}else {
							return APIResponse.getNewFailureResp(new RC("fail", "没有此用户信息"));
						}
					}else {
						return APIResponse.getNewFailureResp(new RC("fail", "验证码错误"));
					}
				}else {
					return APIResponse.getNewFailureResp(new RC("fail", "手机验证码过期"));
				}
			}
		}
	@POSTAPI(//
			path = "wxlogin", 
			des = "微信登录", 
			ret = "" 
		)
		public APIResponse wxlogin(
			@P(t = "模块编号") String moduleId,
			@P(t = "openid") String openid,
			@P(t = "用户名") String name,
			@P(t = "头像") String head
		) throws ServerException, SQLException {
			try(DruidPooledConnection conn = ds.getConnection()){
				ZskpUser user = userRepository.get(conn, EXP.INS().key("open_id", openid).andKey("module_id",moduleId));
				if(user != null) {
					return APIResponse.getNewSuccessResp(user);						
				}else {
					ZskpUser loginUser = new ZskpUser();
					loginUser.moduleId = moduleId;
					loginUser.id = IDUtils.getSimpleId();
					loginUser.name = name;
					loginUser.status = ZskpUser.STATUS_NORMAL;
					loginUser.head = head;
					loginUser.openId = openid;
					loginUser.authority = ZskpUser.AUTHORITY_ONE;
					loginUser.createTime = new Date();
					loginUser.updateTime = new Date();
					userRepository.insert(conn, loginUser);
					return APIResponse.getNewSuccessResp(loginUser);
				}
			}
		}
	
	/**
	 * 注册
	 * @throws ServerException 
	 * @throws SQLException 
	 */
	@POSTAPI(//
		path = "register", 
		des = "注册", 
		ret = "" 
	)
	public APIResponse register(
		@P(t = "模块编号") String moduleId,
		@P(t = "昵称") String name,
		@P(t = "手机号") String phone,
		@P(t = "验证码") String code,
		@P(t = "密码") String pwd
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			ZskpUser user = new ZskpUser();
			user.moduleId = moduleId;
			user.id = IDUtils.getSimpleId();
			user.name = name;
			user.phone = phone;
			user.pwd = pwd;
			user.status = ZskpUser.STATUS_NORMAL;
			user.head = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571131055185&di=4fc467c4531fc69f310817c26c4457dd&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F69ad7a731f43d4b8729f1a2fbe65c43801ca0f033250-EV1vMf_fw658";
			user.authority = ZskpUser.AUTHORITY_ONE;
			user.createTime = new Date();
			user.updateTime = new Date();
			ZskpUser temp = userRepository.get(conn, EXP.INS().key("phone", phone).andKey("module_id",moduleId));
			if(temp != null) {
				return APIResponse.getNewFailureResp(new RC("fail", "你注册的手机号已存在"));
			}
			JSONObject json = CacheCenter.PHONE_CODE.getIfPresent(phone);
			if(json !=null) {
				if(phone.equals(json.getString("phone")) && code.equals(json.getString("code"))) {
					userRepository.insert(conn, user);
					user.pwd = "**";
					return APIResponse.getNewSuccessResp(user);
				}else {
					return APIResponse.getNewFailureResp(new RC("fail", "验证码错误"));
				}
			}else {
				return APIResponse.getNewFailureResp(new RC("fail", "手机验证码过期"));
			}
		}
	}

	/**
	 * 绑定手机
	 */
	
	@POSTAPI(//
		path = "bindingPhone", 
		des = "绑定手机", 
		ret = "" 
	)
	public APIResponse bindingPhone(
		@P(t = "模块编号") String moduleId,
		@P(t = "用户id") Long id,
		@P(t = "手机号") String phone
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			ZskpUser user = new ZskpUser();
			user.phone = phone;
			user.updateTime = new Date();
			ZskpUser temp = userRepository.get(conn, EXP.INS().key("phone", phone).andKey("module_id",moduleId));
			System.out.println(temp != null);
			if(temp != null) {
				return APIResponse.getNewFailureResp(new RC("fail", "手机号重复"));
			}
			userRepository.update(conn, EXP.INS().key("id",id).andKey("module_id",moduleId), user,true);
			ZskpUser returnUser = userRepository.get(conn, EXP.INS().key("id", id).andKey("module_id",moduleId));
			return APIResponse.getNewSuccessResp(returnUser);
		}
	}
	@POSTAPI(//
			path = "bindingwx", 
			des = "绑定微信", 
			ret = "" 
		)
		public APIResponse bindingwx(
			@P(t = "模块编号") String moduleId,
			@P(t = "用户id") Long id,
			@P(t = "openid") String openId
		) throws ServerException, SQLException {
			try(DruidPooledConnection conn = ds.getConnection()){
				ZskpUser user = new ZskpUser();
				user.openId = openId;
				ZskpUser temp = userRepository.get(conn, EXP.INS().key("open_id", openId).andKey("module_id",moduleId));
				if(temp != null) {
					return APIResponse.getNewFailureResp(new RC("fail", "微信号重复"));
				}
				userRepository.update(conn, EXP.INS().key("id",id).andKey("module_id",moduleId), user,true);
				ZskpUser returnUser = userRepository.get(conn, EXP.INS().key("id", id).andKey("module_id",moduleId));
				return APIResponse.getNewSuccessResp(returnUser);
			}
		}
		
	
	
	/**
	 * 封禁用户评论功能
	 */
	@POSTAPI(//
		path = "closeUser", 
		des = "封禁用户评论功能", 
		ret = "" 
	)
	public ZskpUser closeUser(
		@P(t = "模块编号") String moduleId,
		@P(t = "用户id") Long id,
		@P(t = "是否true：feng")Boolean  bool
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			ZskpUser user = new ZskpUser();
			if(bool) {
				System.out.println("zhe shi true");
				user.status = ZskpUser.STATUS_CLOSE;				
			}else {
				user.status = ZskpUser.STATUS_NORMAL;	
			}
			userRepository.update(conn, EXP.INS().key("id",id).andKey("module_id",moduleId), user,true);
			return user;			
		}
	}
	
	/**
	 * 查询用户详情
	 */
	@POSTAPI(//
		path = "getUser", 
		des = "查询用户详情", 
		ret = "" 
	)
	public ZskpUser getUser(
		@P(t = "模块编号") String moduleId,
		@P(t = "用户id") Long id
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			ZskpUser user = userRepository.get(conn, EXP.INS().key("id", id).andKey("module_id",moduleId));
			return user;			
		}
	}
	/**
	 * 修改用户详情
	 */
	@POSTAPI(//
		path = "updateUserInfo", 
		des = "修改用户信息", 
		ret = "" 
	)
	public ZskpUser updateUserInfo(
		@P(t = "模块编号") String moduleId,
		@P(t = "用户id") Long id,
		@P(t = "昵称",r = false) String name,
		@P(t = "头像",r = false) String head,
		@P(t = "单位",r = false) String company,
		@P(t = "权限",r = false) Byte authority,
		@P(t = "状态",r = false) Byte status,
		@P(t = "扩展",r = false) String ext
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			ZskpUser user = new ZskpUser();
			user.name = name;
			user.head = head;
			user.company = company;
			user.authority = authority;
			user.status = status;
			user.ext = ext;
			user.updateTime = new Date();
			userRepository.update(conn, EXP.INS().key("id",id).andKey("module_id",moduleId), user,true);
			return user;
		}
	}

	/**
	 * 创建用户申请
	 */
	@POSTAPI(//
		path = "createApplyAuthority", 
		des = "创建用户申请", 
		ret = "" 
	)
	public APIResponse createApplyAuthority(
		@P(t = "模块编号") Long moduleId,
		@P(t = "用户id") Long userId
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			ApplyAuthority a = new ApplyAuthority();
			a.moduleId = moduleId;
			a.id = IDUtils.getSimpleId();
			a.userId = userId;
			a.status = ApplyAuthority.STATUS_WAIT;
			a.createTime = new Date();
			ApplyAuthority apply = applyAuthorityRepository.get(conn, EXP.INS().key("user_id", userId).andKey("module_id",moduleId));//查询该用户是否已提交过申请
			if(apply != null) {
				return APIResponse.getNewFailureResp(new RC("fail", "已经申请过了"));
			}
			applyAuthorityRepository.insert(conn, a);
			return APIResponse.getNewSuccessResp();
		}
	}
	/**
	 * 查询用户信息
	 */
	@POSTAPI(//
		path = "getUsers", 
		des = "查询用户信息", 
		ret = "" 
	)
	public List<ZskpUser> getUsers(
		@P(t = "模块编号") String moduleId,
		@P(t = "权限",r= false) Byte authority,
		int count,
		int offset
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			List<ZskpUser> userList = userRepository.getList(conn, EXP.INS(false).key("module_id", moduleId).andKey("authority", authority), count, offset);
			return userList;
		}
	}
	/**
	 * 创建用户关注
	 */
	@POSTAPI(//
		path = "createUserFavorite", 
		des = "创建用户关注", 
		ret = "" 
	)
	public APIResponse createUserFavorite(
			@P(t = "模块编号") Long moduleId,
			@P(t = "被关注用户id") Long concernId,
			@P(t = "用户id") Long userId
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			UserFavorites uf = new UserFavorites();
			uf.moduleId = moduleId;
			uf.id = IDUtils.getSimpleId();
			uf.concernId = concernId;
			uf.userId = userId;
			uf.createTime = new Date();
			UserFavorites temp = favoritesRepository.get(conn, EXP.INS().key("concern_id", concernId).andKey("user_id", userId).andKey("module_id",moduleId));
			if(temp != null) {//已关注过
				return APIResponse.getNewFailureResp(new RC("fail", "已经关注了"));
			}
			favoritesRepository.insert(conn, uf);
			return APIResponse.getNewSuccessResp(uf);
		}
	}
	/**
	 * 删除用户关注
	 */
	@POSTAPI(//
		path = "delUserFavorite", 
		des = "删除用户关注", 
		ret = "" 
	)
	public void delUserFavorite(
		@P(t = "模块编号") String moduleId,
		@P(t = "id") Long id
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			favoritesRepository.delete(conn, EXP.INS().key("id", id).andKey("module_id",moduleId));
		}
	}
	
	
	/**
	 * 查询用户关注信息
	 */
	@POSTAPI(//
		path = "getAUserFavorite", 
		des = "查询用户关注内容", 
		ret = "" 
	)
	public JSONArray getAUserFavorite(
		@P(t = "模块编号") String moduleId,
		@P(t = "用户id") Long userId,
		int count,
		int offset
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			List<UserFavorites> list = favoritesRepository.getList(conn, EXP.INS().key("user_id", userId).andKey("module_id",moduleId), count, 0);
			if(list.size()<=0) {
				return null;
			}
			List<Long> ids = new LinkedList<Long>();
			for(UserFavorites u:list) {
				ids.add(u.concernId);
			}
			List<Content> conlist = contentRepository.getList(conn, EXP.INS().key("org_module", moduleId).and(EXP.IN_ORDERED("up_user_id", ids.toArray())), count, offset);
			String temp = JSON.toJSON(conlist).toString();
			JSONArray json = JSONArray.parseArray(temp);
			for(int i=0;i<conlist.size();i++) {
				System.out.println(conlist.get(i).upUserId);
				ZskpUser user = userRepository.get(conn, EXP.INS().key("module_id", moduleId).andKey("id", conlist.get(i).upUserId));
				json.getJSONObject(i).put("user",user);
			}
			return json;
		}
	}
	/**
	 * 查询用户关注信息
	 */
	@POSTAPI(//
		path = "getFavoriteUser", 
		des = "查询用户关注的用户", 
		ret = "" 
	)
	public JSONArray getFavoriteUser(
		@P(t = "模块编号") String moduleId,
		@P(t = "用户id") Long userId,
		int count,
		int offset
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			List<UserFavorites> list = favoritesRepository.getList(conn, EXP.INS().key("user_id", userId).andKey("module_id",moduleId), count, offset);
			String temp = JSON.toJSON(list).toString();
			JSONArray json = JSONArray.parseArray(temp);
			for(int i=0;i<list.size();i++) {
				ZskpUser user = userRepository.get(conn, EXP.INS().key("module_id", moduleId).andKey("id", list.get(i).concernId));
				json.getJSONObject(i).put("user",user);
			}
			return json;
		}
	}
	/**
	 * 查询用户是否关注
	 */
	@POSTAPI(//
		path = "getBoolFavoriteUser", 
		des = "查询用户是否关注", 
		ret = "" 
	)
	public JSONObject getBoolFavoriteUser(
		@P(t = "模块编号") String moduleId,
		@P(t = "用户id") Long userId,
		@P(t = "被关注用户id") Long concernId,
		int count,
		int offset
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			UserFavorites temp = favoritesRepository.get(conn, EXP.INS().key("concern_id", concernId).andKey("user_id", userId).andKey("module_id",moduleId));
			JSONObject json = new JSONObject();
			if(temp != null) {//已关注过
				json.put("info", temp);
				json.put("bool", true);
				return json;
			}
			json.put("bool", false);
			return json;
		}
	}
//	/**
//	 * 微信登录
//	 */
//	@POSTAPI(//
//		path = "wxLogin", 
//		des = "微信登录", 
//		ret = "" 
//	)
//	public void wxLogin(
//	) {
//		
//	}
	
}
