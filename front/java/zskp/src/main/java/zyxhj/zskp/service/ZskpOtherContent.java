package zyxhj.zskp.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zyxhj.cms.domian.Content;
import zyxhj.cms.repository.ContentRepository;
import zyxhj.utils.IDUtils;
import zyxhj.utils.Singleton;
import zyxhj.utils.api.APIResponse;
import zyxhj.utils.api.Controller;
import zyxhj.utils.api.RC;
import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.EXP;
import zyxhj.zskp.domain.AdvertInfo;
import zyxhj.zskp.domain.ApplyAuthority;
import zyxhj.zskp.domain.Appraise;
import zyxhj.zskp.domain.Comment;
import zyxhj.zskp.domain.Enroll;
import zyxhj.zskp.domain.Reply;
import zyxhj.zskp.domain.TourBases;
import zyxhj.zskp.domain.ZskpUser;
import zyxhj.zskp.repository.AdvertInfoRepository;
import zyxhj.zskp.repository.ApplyAuthorityRepository;
import zyxhj.zskp.repository.AppraiseRepository;
import zyxhj.zskp.repository.CommentRepository;
import zyxhj.zskp.repository.EnrollRepository;
import zyxhj.zskp.repository.IsreadRepository;
import zyxhj.zskp.repository.ReplyRepository;
import zyxhj.zskp.repository.TourBasesRepository;
import zyxhj.zskp.repository.UserRepository;

public class ZskpOtherContent extends Controller{
	
	private AdvertInfoRepository advertInfoRepository;
	private TourBasesRepository tourBasesRepository;
	private EnrollRepository enrollRepository;
	private ApplyAuthorityRepository applyAuthorityRepository;
	private UserRepository userRepository; 
	private ContentRepository contentRepository;
	private ReplyRepository replyRepository;
	private CommentRepository commentRepository;
	private IsreadRepository isreadRepository;
	private AppraiseRepository appraiseRepository;
	private DruidDataSource ds;
	public ZskpOtherContent(String node) {
		super(node);
		try {
			ds = DataSource.getDruidDataSource("rdsDefault.prop");
			advertInfoRepository = Singleton.ins(AdvertInfoRepository.class);
			tourBasesRepository = Singleton.ins(TourBasesRepository.class);
			enrollRepository = Singleton.ins(EnrollRepository.class);
			applyAuthorityRepository = Singleton.ins(ApplyAuthorityRepository.class);
			userRepository = Singleton.ins(UserRepository.class);
			contentRepository = Singleton.ins(ContentRepository.class);
			replyRepository = Singleton.ins(ReplyRepository.class);
			commentRepository = Singleton.ins(CommentRepository.class);
			isreadRepository = Singleton.ins(IsreadRepository.class);
			appraiseRepository = Singleton.ins(AppraiseRepository.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据条件查询内容 
	 * 
	 */
	@POSTAPI(
		path = "getContents", //
		des = "根据条件查询内容 获取用户发布内容 ", //
		ret = "所创建的对象"//
	)
	public JSONArray getContents(
		@P(t = "模块")String module, 
		@P(t = "类型",r = false)Byte type, 
		@P(t = "状态编号",r = false)Byte status,
		@P(t = "权力",r = false)Byte power,
		@P(t = "上传用户编号",r = false)Long upUserId,
		@P(t = "上传专栏编号",r = false)Long upChannelId,
		@P(t = "标签",r = false)JSONObject tags, 
		int count, 
		int offset
	)
	throws Exception {
		EXP exp = EXP.INS(false).key("org_module", module).andKey("type", type).andKey("status", status).andKey("power", power).andKey("up_user_id", upUserId).andKey("up_channel_id", upChannelId);
		if(tags !=null && tags.size()>0) {
			exp.and(EXP.JSON_CONTAINS_JSONOBJECT(tags, "tags"));
		}
		exp.append("ORDER BY page_view desc,create_time desc");
		try (DruidPooledConnection conn = ds.getConnection()) {			
			List<Content> list = contentRepository.getList(conn,exp, count, offset);
			if(list == null && list.size()<=0) {
				return null;
			}
			String temp = JSON.toJSONString(list);
			JSONArray json = JSONArray.parseArray(temp);
			int index = json.size();
			List<Long> ids = new LinkedList<Long>();
			for (int i = 0; i < index; i++) {
				ids.add(list.get(i).upUserId);
			}
			if(ids == null && ids.size()<=0) {
				return null;
			}
			return getUserInfo(module,ids,json);
		}
	}
	@POSTAPI(
			path = "searchContents", //
			des = "搜索内容", //
			ret = "所创建的对象"//
		)
		public JSONArray searchContents(
			@P(t = "模块")String module,
			@P(t = "类型",r = false)Byte type, 
			@P(t = "状态编号",r = false)Byte status,
			@P(t = "权力",r = false)Byte power,
			@P(t = "上传用户编号",r = false)Long upUserId,
			@P(t = "上传专栏编号",r = false)Long upChannelId,
			@P(t = "标签",r = false)JSONObject tags, 
			@P(t = "搜索内容")String keyword, 
			int count, 
			int offset
		)
		throws Exception {
			EXP exp = EXP.INS(false).key("org_module", module).andKey("type", type).andKey("status", status).
					andKey("power", power).andKey("up_user_id", upUserId).andKey("up_channel_id", upChannelId).and(EXP.LIKE("title", keyword));
			if(tags !=null && tags.size()>0) {
				exp.and(EXP.JSON_CONTAINS_JSONOBJECT(tags, "tags"));
			}
			try (DruidPooledConnection conn = ds.getConnection()) {			
				List<Content> list = contentRepository.getList(conn,exp, count, offset);
				String temp = JSON.toJSONString(list);
				JSONArray json = JSONArray.parseArray(temp);
				int index = json.size();
				List<Long> ids = new LinkedList<Long>();
				for (int i = 0; i < index; i++) {
					ids.add(list.get(i).upUserId);
				}
				return getUserInfo(module,ids,json);
			}
		}
	public JSONArray getUserInfo(String module,List<Long> list,JSONArray json) throws SQLException, ServerException {
		try (DruidPooledConnection conn = ds.getConnection()) {
			if(list.size()<=0) {
				return null;
			}
			ZskpUser zskpUser = null;
			List<ZskpUser> userList = userRepository.getList(conn, EXP.INS().key("module_id", module).and(EXP.INS().IN("id", list.toArray())),200,0);
			for (int i = 0,index = json.size(); i < index; i++) {
				for (int j = 0,index2 = userList.size(); j < index2; j++) {
					if(json.getJSONObject(i).getLong("upUserId").equals(userList.get(j).id)) {
						zskpUser = new ZskpUser();
						zskpUser.name = userList.get(j).name;
						zskpUser.head = userList.get(j).head;
						zskpUser.ext = userList.get(j).ext;
						json.getJSONObject(i).put("user",zskpUser);		
						break;
					}
				}
			}
			return json;
		}
	}
	
	/**
	 * 创建广告
	 */
	@POSTAPI(//
		path = "createAdvert", 
		des = "创建广告", 
		ret = "" 
	)
	public AdvertInfo createAdvert(
		@P(t = "模块编号") Long moduleId,
		@P(t = "图片地址") String imgSrc,
		@P(t = "详情链接") String linkSrc,
		@P(t = "排序大小",r= false) int sortSize,
		@P(t = "备注",r= false) String remake,
		@P(t = "类型",r= false) Byte type,
		@P(t = "所属专栏",r= false) Long channelId,
		@P(t = "状态",r= false) Byte status
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			if(type == AdvertInfo.TYPE_START && status == AdvertInfo.STATUS_OPEN) {
				//如果类型为APP启动图且设置为启用状态，启动图只应许一只为启用
				advertInfoRepository.updateAppStartAdvert(moduleId);
			}
			AdvertInfo t = new AdvertInfo();
			t.moduleId = moduleId;
			t.id = IDUtils.getSimpleId();
			t.imgSrc = imgSrc;
			t.linkSrc = linkSrc;
			t.createTime = new Date();
			t.remark = remake;
			t.sortSize = sortSize;
			if(status == null) {
				t.status = AdvertInfo.STATUS_OPEN;				
			}else {
				t.status = status;
			}
			t.type = type;
			t.channelId = channelId;
			advertInfoRepository.insert(conn, t);
			return t;
		}
		
	}
	/**
	 * 查询广告
	 */
	@POSTAPI(//
		path = "getAdverts", 
		des = "查询广告", 
		ret = "" 
	)
	public List<AdvertInfo> getAdverts(
		@P(t = "模块编号") Long moduleId,
		@P(t = "类型",r= false) Byte type,
		@P(t = "状态",r= false) Byte status,
		@P(t = "所属专栏",r= false) Long channelId,
		int count,
		int offset
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			return advertInfoRepository.getList(conn, EXP.INS(false).key("module_id", moduleId).andKey("type", type).andKey("channel_id", channelId)
					.andKey("status",status== null?AdvertInfo.STATUS_OPEN:status).append("order by sort_size desc"), count, offset);
		}
	}
	@POSTAPI(//
		path = "delAdvert", 
		des = "删除广告", 
		ret = "" 
	)
	public int delAdvert(
		@P(t = "模块编号") Long moduleId,
		@P(t = "id") Long id
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			return advertInfoRepository.delete(conn, EXP.INS().key("id", id).andKey("module_id", moduleId));
		}
	}
	@POSTAPI(//
		path = "updateAdvert", 
		des = "修改广告", 
		ret = "" 
	)
	public int updateAdvert(
		@P(t = "模块编号") Long moduleId,
		@P(t = "id") Long id,
		@P(t = "图片地址",r = false) String imgSrc,
		@P(t = "详情链接",r = false) String linkSrc,
		@P(t = "备注",r = false) String remake,
		@P(t = "状态",r = false) Byte status,
		@P(t = "排序大小",r = false) int sortSize,
		@P(t = "类型",r= false) Byte type,
		@P(t = "所属专栏",r= false) Long channelId
	) throws ServerException, SQLException {
		if(type == AdvertInfo.TYPE_START && status == AdvertInfo.STATUS_OPEN) {
			//如果类型为APP启动图且设置为启用状态，启动图只应许一只为启用
			advertInfoRepository.updateAppStartAdvert(moduleId);
		}
		AdvertInfo t = new AdvertInfo();
		t.imgSrc = imgSrc;
		t.linkSrc = linkSrc;
		t.remark = remake;
		t.status = status;
		t.sortSize = sortSize;
		t.type = type;
		t.channelId = channelId;
		try(DruidPooledConnection conn = ds.getConnection()){
			return advertInfoRepository.update(conn, EXP.INS().key("id", id).andKey("module_id", moduleId),t,true);
		}
	}
	@POSTAPI(//
			path = "getTourBases", 
			des = "查询科普基地景点", 
			ret = "" 
		)
		public List<TourBases> getTourBases(
			@P(t = "模块编号") String moduleId,
			@P(t = "用户定位经纬度") String userCoordinate,
			@P(t = "地理范围", r= false) String size,
			int count,
			int offset
		) throws Exception   {		
			try(DruidPooledConnection conn = ds.getConnection()) {
				EXP exp = EXP.INS().key("module_id", moduleId);
				List<TourBases> list = tourBasesRepository.getList(conn, exp, count, offset);
				return list;
			}
		}
	
	@POSTAPI(//
			path = "getTourBase", 
			des = "查询科普基地详情", 
			ret = "" 
		)
		public JSONObject getTourBase(
			@P(t = "模块编号") Long moduleId,
			@P(t = "id") Long id,
			int count,
			int offset
		) throws Exception {
			try(DruidPooledConnection conn = ds.getConnection()){
				TourBases bases = tourBasesRepository.get(conn, EXP.INS().key("module_id", moduleId).andKey("id", id));
				JSONObject json = new JSONObject();
				JSONArray ja = new JSONArray();
				ja.add(bases.id);
				EXP exp = EXP.INS().key("org_module", moduleId.toString()).andKey("type", 8).and(EXP.JSON_CONTAINS_KEYS(ja, "data","place"));
				System.out.println(contentRepository.getList(conn, exp, count, offset));
				json.put("content",contentRepository.getList(conn, exp, count, offset));
				json.put("TourBases", bases);
				return json;
			}
		}
	@POSTAPI(//
			path = "updateTourBase", 
			des = "修改科普基地详情", 
			ret = "" 
		)
		public APIResponse updateTourBase(
			@P(t = "模块编号") Long moduleId,
			@P(t = "id") Long id,
			@P(t = "名称") String name,
			@P(t = "地址") String address,
			@P(t = "数据") String data,
			@P(t = "购票链接") String buyTicketsLink
		) throws Exception {
			try(DruidPooledConnection conn = ds.getConnection()) {
				TourBases t = new TourBases();
				t.name = name;
				t.address = address;
				t.data = data;
				t.createTime = new Date();
				t.coordinate = HttpClientGet(address);;
				t.buyTicketsLink = buyTicketsLink;
				tourBasesRepository.update(conn, EXP.INS().key("module_id", moduleId).andKey("id", id), t, true);
				return APIResponse.getNewSuccessResp();
			}
		}
	
	@POSTAPI(//
			path = "createTourBase", 
			des = "创建科普基地景点", 
			ret = "" 
		)
		public APIResponse createTourBase(
			@P(t = "模块编号") Long moduleId,
			@P(t = "名称") String name,
			@P(t = "地址") String address,
			@P(t = "数据") String data,
			@P(t = "购票链接") String buyTicketsLink
		) throws Exception {
			try(DruidPooledConnection conn = ds.getConnection()) {
				TourBases t = new TourBases();
				t.moduleId = moduleId;
				t.id = IDUtils.getSimpleId();
				t.name = name;
				t.address = address;
				t.coordinate = HttpClientGet(address);
				if(data.length()<10000) {
				    t.data = data;
			   }else {
				   return APIResponse.getNewFailureResp(new RC("fail", "简介长度大于10000,当前长度"+data.length()));
			   }
				t.createTime = new Date();
				t.buyTicketsLink = buyTicketsLink;
				tourBasesRepository.insert(conn, t);
				return APIResponse.getNewSuccessResp();
			} 
		}
	
	@POSTAPI(//
			path = "delupdateTourBase", 
			des = "删除科普基地景点", 
			ret = "" 
		)
		public void delupdateTourBase(
			@P(t = "模块编号") Long moduleId,
			@P(t = "id") String id
		) throws Exception {
			try (DruidPooledConnection conn = ds.getConnection()){
				List<Content> list = contentRepository.getList(conn, EXP.INS().key("org_module", moduleId).andKey("type", 8), 400, 0);
				List<Long> ids = new LinkedList<Long>();
				for(Content c:list) {//基地下面的活动也请一起删除
					JSONObject json =JSONObject.parseObject(c.data);
					if(id.equals(json.getString("place"))) {
						ids.add(c.id);
					}
				}
				if(ids != null && ids.size()>0) {
					contentRepository.delete(conn, EXP.INS().key("org_module", moduleId).and(EXP.IN("id", ids.toArray())));					
				}
				tourBasesRepository.delete(conn, EXP.INS().key("module_id", moduleId).andKey("id", Long.parseLong(id)));
			}
		}
	@POSTAPI(//
			path = "searchTourBase", 
			des = " 搜索景点", 
			ret = "" 
		)
		public  List<TourBases>  searchTourBase(
			@P(t = "模块编号") String moduleId,
			@P(t = "关键字") String search,
			@P(t = "用户定位经纬度") String userCoordinate,
			int count,
			int offset
		) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()) {
			   List<TourBases> list = tourBasesRepository.getList(conn, EXP.INS().and(EXP.LIKE("name", search)).andKey("module_id", moduleId), count, offset);
			   return list;
		}
	}
	@POSTAPI(//
		path = "delContentById", 
		des = "删除内容", 
		ret = "" 
	)
	public void delContentById(
		@P(t = "模块编号") Long moduleId,
		@P(t = "id") Long id
	) throws Exception {
		try (DruidPooledConnection conn = ds.getConnection()) {
			contentRepository.delete(conn, EXP.INS().key("id", id));			
			replyRepository.delete(conn, EXP.INS().key("owner_id", id));
		}
	}
	/**
	 * 创建报名
	 */
	@POSTAPI(//
		path = "createEnroll", 
		des = "创建报名", 
		ret = "" 
	)
	public Enroll createEnroll(
		@P(t = "模块编号") Long moduleId,
		@P(t = "内容id") Long contenId,
		@P(t = "用户id") Long userId,
		@P(t = "用户名") String name,
		@P(t = "内容标题") String contentName,
		@P(t = "手机号") String phone,
		@P(t = "报名人数") String number
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			Enroll t = new Enroll();
			t.moduleId = moduleId;
			t.id = IDUtils.getSimpleId();
			t.contenId = contenId;
			t.userId = userId;
			t.name = name;
			t.contentName = contentName;
			t.phone = phone;
			t.number = number;
			t.createTime = new Date();
			Enroll temp = enrollRepository.get(conn, EXP.INS().key("conten_id", contenId).andKey("user_id",userId).andKey("module_id", moduleId));
			if(temp != null) {//重复报名
				return t;
			}
			enrollRepository.insert(conn, t);
			return t;
		}
	}
	/**
	 * 查询报名
	 */
	@POSTAPI(//
		path = "getEnrolls", 
		des = "查询报名", 
		ret = "" 
	)
	public List<Enroll> getEnrolls(
		@P(t = "模块编号") Long moduleId,
		@P(t = "用户id",r =false) Long userId,
		@P(t = "内容id",r =false) Long contenId,
		int count,
		int offset
	)  throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			return enrollRepository.getList(conn, EXP.INS(false).key("conten_id", contenId).
					andKey("module_id", moduleId).andKey("user_id", userId), count, offset);
		}
	}
	@POSTAPI(//
			path = "getEnrollsByAdmin", 
			des = "后台查询报名", 
			ret = "" 
		)
		public APIResponse getEnrollsByAdmin(
			@P(t = "模块编号") Long moduleId,
			@P(t = "用户id",r =false) Long userId,
			@P(t = "内容id",r =false) Long contenId,
			int count,
			int offset
		)  throws ServerException, SQLException {
			try(DruidPooledConnection conn = ds.getConnection()){
				JSONObject num = enrollRepository.getCount(conn,contenId);
				JSONObject userNum = enrollRepository.getNumberCount(conn,contenId);
				List<Enroll> list =  enrollRepository.getList(conn, EXP.INS(false).key("conten_id", contenId).
						andKey("module_id", moduleId).andKey("user_id", userId), count, offset);
				JSONArray json = new JSONArray();
				json.add(list);
				json.add(num);
				json.add(userNum);
				return APIResponse.getNewSuccessResp(json);
			}
		}
	@POSTAPI(//
			path = "searchEnrollsUserName", 
			des = "查询报名用户名称", 
			ret = "" 
		)
		public List<Enroll> getEnrolls(
			@P(t = "模块编号") Long moduleId,
			@P(t = "名称",r =false) String name,
			@P(t = "内容id",r =false) Long contenId,
			int count,
			int offset
		)  throws ServerException, SQLException {
			try(DruidPooledConnection conn = ds.getConnection()){
				return enrollRepository.getList(conn, EXP.INS().key("module_id", moduleId).andKey("conten_id", contenId).and(EXP.INS().LIKE("name", name)), count, offset);
			}
		}
	/**
	 * 删除报名
	 */
	
	@POSTAPI(//
		path = "delEnrolls", 
		des = "删除报名", 
		ret = "" 
	)
	public int delEnrolls(
			@P(t = "id") Long id
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			return enrollRepository.delete(conn, EXP.INS().key("id", id));
		}
	}
	/**
	 * 查询申请
	 */
	@POSTAPI(//
		path = "getApplyAuthoritys", 
		des = "查询申请", 
		ret = "" 
	)
	public JSONArray getApplyAuthoritys(
		@P(t = "模块编号") Long moduleId,
		@P(t = "状态",r = false) Long status,
		int count,
		int offset
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			List<ApplyAuthority> list = applyAuthorityRepository.getList(conn, EXP.INS(false).key("module_id", moduleId).andKey("status", status), count, offset);
			String temp = JSON.toJSON(list).toString();
			JSONArray json = JSONArray.parseArray(temp);
			for (int i = 0; i < json.size(); i++) {
				ZskpUser user = userRepository.get(conn, EXP.INS().key("module_id", moduleId).andKey("id", list.get(i).userId));
				json.getJSONObject(i).put("user",user);
			}
			return json;
		}
	}
	@POSTAPI(//
			path = "examineApplyAuthority", 
			des = "审核申请", 
			ret = "" 
		)
		public void examineApplyAuthority(
			@P(t = "模块编号") Long moduleId,
			@P(t = "id") Long id,
			@P(t = "用户id") Long userId,
			@P(t = "是否通过申请") boolean bool
		) throws ServerException, SQLException {
			try(DruidPooledConnection conn = ds.getConnection()){
				ApplyAuthority ap = new ApplyAuthority();
				ap.status = ApplyAuthority.STATUS_LOSE;	
				if(bool) {
					ap.status = ApplyAuthority.STATUS_SUSSESS;
					ZskpUser user = new ZskpUser();
					user.authority = ZskpUser.AUTHORITY_TWO;//申请通过后变更权限
					userRepository.update(conn, EXP.INS().key("id",userId).andKey("module_id", moduleId), user,true);
				}else {
					applyAuthorityRepository.delete(conn, EXP.INS().key("id", id).andKey("module_id", moduleId));
				}
				applyAuthorityRepository.update(conn, EXP.INS().key("id", id).andKey("module_id", moduleId), ap,true);
			}
		}
	/**
	 * 搜索内容与用户
	 */
	@POSTAPI(//
		path = "searchContentAndUser", 
		des = " 搜索内容与用户", 
		ret = "" 
	)
	public JSONObject searchContentAndUser(
		@P(t = "模块编号") Long moduleId,
		@P(t = "关键字") String search,
		int count,
		int offset
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			List<Content> contentList = contentRepository.getList(conn, EXP.INS().key("org_module", moduleId).and(EXP.INS().LIKE("title", search)), count, offset);
			List<ZskpUser> userList = userRepository.getList(conn, EXP.INS().key("module_id", moduleId).and(EXP.INS().LIKE("name", search)), count, offset);
			String temp = JSON.toJSON(contentList).toString();
			JSONArray json = JSONArray.parseArray(temp);
			for (int i = 0; i < json.size(); i++) {
				ZskpUser user = userRepository.get(conn, EXP.INS().key("module_id", moduleId).andKey("id", contentList.get(i).upUserId));
				json.getJSONObject(i).put("user",user);
			}
			JSONObject j = new JSONObject();
			j.put("contentSearch", json);
			j.put("userSearch", userList);
			return j;
		}
	}
	@POSTAPI(//
		path = "getVersion", 
		des = " 获取版本", 
		ret = "" 
	)
	public JSONObject getVersion(
	) throws ServerException, SQLException, FileNotFoundException, IOException {
		Properties p = new Properties();
		String ciname = StringUtils.join("configs/", "zskpVersion.prop");
		p.load(new FileInputStream(ciname));
		String version = p.getProperty("version");
		JSONObject json = new JSONObject();
		json.put("version", version);
		json.put("android", "http://weapp.datanc.cn/science/app/"+version+"/android/zskp.apk");
		json.put("ios", "http://weapp.datanc.cn/science/app/"+version+"/ios/zskp.ipa");
		return json;
	}
	
	@POSTAPI(//
		path = "getReplyListByUser", //
		des = "获取用户回复提问" //
	)
	public JSONArray getReplyListByUser(//
		@P(t = "模块编号",r= false) String moduleId, //
		@P(t = "持有者编号",r= false) Long ownerId, //
		@P(t = "提交者编号",r = false) Long upUserId, //
//		@P(t = "审核状态，不填表示全部，0未审核，1已通过",r = false) String status, //
		@P(t = "是否降序（较新的排前面）") Boolean orderDesc, //
		@P(t = "目标用户id",r = false) Long toUserId, //
		@P(t = "限定时间状态",r = false) Integer time, //
		@P(t = "是否加载二级评论",r = false) Boolean isComment, //
		Integer count, Integer offset//
	) throws Exception {
		if(moduleId == null) {
			moduleId = "1180";
		}
		try (DruidPooledConnection conn = ds.getConnection()) {
			JSONArray json = replyRepository.getReplyList(conn,ownerId, upUserId, null, orderDesc, toUserId,  count, offset);
			JSONArray returnJson = new JSONArray();
			Map<Long, Long> map = new LinkedHashMap<Long, Long>();
			int index = json.size();
			if(time != null) {//如果有时间，按照时间筛选返回
				for(int i= 0 ;i<index ;i++) {
					Long reolyTime = getTimeInMillis(time, json.getJSONObject(i).getString("createTime"));//时间转换为时间戳
					System.out.println(new Date().getTime());
					if(reolyTime >getTimeInMillis(time) && reolyTime < new Date().getTime()) {//查询创建时间为：大于一给定时间前，小于当前时间
						map.put(json.getJSONObject(i).getLong("ownerId"),json.getJSONObject(i).getLong("ownerId"));						
					}
				}
			}else {
				for(int i= 0 ;i<index ;i++) {
					map.put(json.getJSONObject(i).getLong("ownerId"),json.getJSONObject(i).getLong("ownerId"));
				}				
			}
			for(Long key:map.keySet()) {
				returnJson.add(getReplyList(moduleId,map.get(key),upUserId,orderDesc,toUserId,null,isComment==null?false:isComment,count,offset));
			}
			if(upUserId != null) {//删除未读记录
				isreadRepository.delete(conn, EXP.INS().key("user_id", upUserId));				
			}
			return returnJson;
		}
	}
	@POSTAPI(//
			path = "getReplyList", //
			des = "获取回复评论" //
	)
	public JSONArray getReplyList(//
			@P(t = "模块编号",r= false) String moduleId, //
			@P(t = "持有者编号",r= false) Long ownerId, //
			@P(t = "提交者编号",r = false) Long upUserId, //
//			@P(t = "审核状态0未审核，1已通过",r = false) String status, //
			@P(t = "是否降序（较新的排前面）") Boolean orderDesc, //
			@P(t = "目标用户id",r = false) Long toUserId, //
			@P(t = "当前用户id",r = false) Long userId, //
			@P(t = "是否加载二级评论",r = false) Boolean isComment, //
			Integer count, Integer offset//
	) throws Exception {
		try (DruidPooledConnection conn = ds.getConnection()) {
			if(moduleId == null) {
				moduleId = "1180";
			}
			JSONArray json = replyRepository.getReplyList(conn,ownerId, upUserId, null, orderDesc, toUserId, count, offset);			
			JSONArray returnJson = new JSONArray();
			JSONObject tempJson = null;
			int index = json.size();
			for(int i= 0 ;i<index ;i++) {
				tempJson = new JSONObject();
				tempJson = json.getJSONObject(i);
				Long relpyId = json.getJSONObject(i).getLong("sequenceId");//评论id
				//查询二级评论
				if(isComment == null || isComment ) {
					JSONArray comment =  commentRepository.getCommentList(conn,relpyId, upUserId, null, orderDesc, toUserId,  count, offset);
					tempJson.put("comment", comment);
				}
				if(judgeAppraise(relpyId, userId)) {
					tempJson.put("isAppraise", true);
				}else {
					tempJson.put("isAppraise", false);
				}
				returnJson.add(tempJson);
			}
			return returnJson;
		}
	}
	@POSTAPI(//
			path = "getReplyListByReplyId", //
			des = "获取二级评论" //
	)
	public JSONObject getReplyListByReplyId(//
			@P(t = "模块编号",r= false) String moduleId, //
			@P(t = "持有者编号") Long ownerId, //
			@P(t = "评论id") Long replyId, //
//			@P(t = "审核状态，不填表示全部，0未审核，1已通过",r = false) String status, //
			@P(t = "是否降序（较新的排前面）") Boolean orderDesc, //
			@P(t = "用户id") Long userId, //
			Integer count, Integer offset//
	) throws Exception {
		try (DruidPooledConnection conn = ds.getConnection()) {
			if(moduleId == null) {
				moduleId = "1180";
			}
			JSONObject rerurnJson = new JSONObject();
			JSONArray commentJson = new JSONArray();
			JSONObject tempJson = null;
			JSONArray json = commentRepository.getCommentList(conn, replyId, null, null, orderDesc, null, count, offset);
			JSONObject reply = replyRepository.getReply(conn, replyId);
			updateReply(reply.getLong("sequenceId"));//
			rerurnJson.put("reply", reply);
			//查询REPLY是否点过赞
			if(judgeAppraise(reply.getLong("sequenceId"), userId)) {
				rerurnJson.put("isAppraise",true);
			}else {
				rerurnJson.put("isAppraise",false);
			}
			for(int i=0,index = json.size();i<index;i++) {
				tempJson = new JSONObject();
				tempJson = json.getJSONObject(i);
				if(judgeAppraise(json.getJSONObject(i).getLong("sequenceId"), userId)) {
					tempJson.put("isAppraise",true);
				}else {
					tempJson.put("isAppraise",false);
				}
				commentJson.add(tempJson);
			}
			rerurnJson.put("comment", commentJson);
			return rerurnJson;
		}
				
	}
	@POSTAPI(//
			path = "getReplyListByStatus", //
			des = "状态获取敏感评论列表" //
	)
	public JSONArray getReplyListByStatus(//
			Integer count, Integer offset//
	) throws Exception {
		try (DruidPooledConnection conn = ds.getConnection()) {
			return replyRepository.getReplyListByStatus(conn,count,offset);
		}
				
	}
	/*
	 * 修改评论状态:用户提醒是否已读
	 */
	public void updateReply(Long id) throws ServerException, SQLException {
		Comment c = new Comment();
		c.ext = "1";
		Reply re = new Reply();
		re.ext = "1";
		try (DruidPooledConnection conn = ds.getConnection()) {
			replyRepository.update(conn, EXP.INS().andKey("sequence_id", id),re,true);			
			commentRepository.update(conn, EXP.INS().andKey("sequence_id", id), c, true);
		}
	}
	public boolean judgeAppraise(
			@P(t = "内容编号") Long ownerId, 
			@P(t = "用户编号") Long userId
	)throws Exception {
		try (DruidPooledConnection conn = ds.getConnection()) {
			Appraise appraise = appraiseRepository.get(conn,
					EXP.INS().key("owner_id", ownerId).andKey("user_id", userId));
			// true为已经点过赞
			return appraise != null;
		}
	}
		/**
		 * 活动编辑时修改科普基地或活动时间
		 */
		@POSTAPI( //
				path = "editData", //
				des = "活动编辑时修改科普基地或活动时间", //
				ret = "" //
		)
		public APIResponse editData(
				@P(t = "内容所属模块")String ModuleId, 
				@P(t = "内容编号")Long id, 
				@P(t = "基地编号", r = false)Long tourBasesId,
				@P(t = "活动时间", r = false)String time
		) throws Exception {
			try(DruidPooledConnection conn = ds.getConnection()) {
				Content content = contentRepository.get(conn, EXP.INS().key("org_module", ModuleId).andKey("id", id));
				JSONObject jo = JSONObject.parseObject(content.data);
				jo.put("place", tourBasesId);
				jo.put("time", time);
				content.data = jo.toJSONString();
				contentRepository.update(conn, EXP.INS().key("org_module", ModuleId).andKey("id", id), content, true);
				return APIResponse.getNewSuccessResp(jo);
			}
		}

	public String HttpClientGet(String address) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://api.map.baidu.com/geocoding/v3/?address="+address+"&output=json&ak=WHqoGrrCvXVIjhiZDuGIyBBK76imLNtE");
		CloseableHttpResponse Response = client.execute(httpGet);
		HttpEntity entity = Response.getEntity();
		String str = EntityUtils.toString(entity, "UTF-8");
		JSONObject json = (JSONObject) JSONObject.parseObject(str).get("result");
		Response.close();
		JSONObject j = (JSONObject) json.get("location");
		String sb = j.get("lat")+","+j.get("lng");
		return sb;
	}
	public Long getTimeInMillis(int day) throws Exception {
		Date d=new Date();
		GregorianCalendar gc =new GregorianCalendar();
		SimpleDateFormat sf  =new SimpleDateFormat("yyyy-MM-dd");
		gc.setTime(d);
		gc.add(5,-day);
		gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
        return gc.getTimeInMillis();
	}
	public Long getTimeInMillis(int day,String newTime) throws Exception {
		String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(newTime);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return Long.parseLong(res);
	}
}
