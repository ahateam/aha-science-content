package zyxhj.zskp.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zyxhj.cms.domian.Content;
import zyxhj.cms.service.ContentService;
import zyxhj.utils.IDUtils;
import zyxhj.utils.Singleton;
import zyxhj.utils.api.Controller;
import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.EXP;
import zyxhj.zskp.domain.InterestTag;
import zyxhj.zskp.domain.ZskpKeyword;
import zyxhj.zskp.repository.InterestTagRepository;
import zyxhj.zskp.repository.ZskpKeywordRepository;

public class ZskpKeywordService extends Controller{

	private DruidDataSource ds;
	private ZskpKeywordRepository keywordRepository;
	private ZskpOtherContent otherContent;
	private ContentService contentService;
	private InterestTagRepository interestTagRepository;
	public ZskpKeywordService(String node) {
		super(node);
		try {
			ds = DataSource.getDruidDataSource("rdsDefault.prop");
			keywordRepository = Singleton.ins(ZskpKeywordRepository.class);
			otherContent =  Singleton.ins(ZskpOtherContent.class);
			contentService = Singleton.ins(ContentService.class);
			interestTagRepository = Singleton.ins(InterestTagRepository.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@POSTAPI(//
		path = "createKeyword", 
		des = "创建关键词", 
		ret = "" 
	)
	public ZskpKeyword createKeyword(
		@P(t = "用户id") Long userId,
		@P(t = "关键词") String keyword
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			ZskpKeyword z = new ZskpKeyword();
			z.id = IDUtils.getSimpleId();
			z.userId = userId;
			z.keyword = keyword;
			z.pageView = 0;
			z.createTime = new Date();
			keywordRepository.insert(conn, z);
			return z;
		}
	}
	@POSTAPI(//
		path = "delKeyword", 
		des = "del关键词", 
		ret = "" 
	)
	public void delKeyword(Long id) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			keywordRepository.delete(conn, EXP.INS().key("id", id));
		}
	}
	@POSTAPI(//
		path = "getKeywords", 
		des = "get关键词", 
		ret = "" 
	)
	public List<ZskpKeyword> getKeywords(
		@P(t = "用户id",r = false) Long userId,
		@P(t = "关键词",r = false) String keyword,
		int count,
		int offset) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			return keywordRepository.getList(conn, null, count, offset);
		}
	}
	@POSTAPI(//
		path = "getKeywordContent", 
		des = "获取推荐内容", 
		ret = "" 
	)
	public JSONArray getKeywordContent(
		@P(t = "模块") String module,
		@P(t = "用户id",r = false) Long userId,
		int count,
		int offset) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()){
			if(userId ==null) {
				return otherContent.getContents(module, null, (byte)4, null, null, null, null, count, offset);
			}
			List<InterestTag> list = interestTagRepository.getList(conn, EXP.INS(false).andKey("user_id", userId).append("order by page_view desc"), 3, 0);
			int index = list.size();
			if(index <= 0 || list==null) {
				return otherContent.getContents(module, null, (byte)4, null, null, null, null, count, offset);
			}
			JSONArray json = null;
			JSONArray temp = null;
			Map<Long, Object> map = new HashMap<Long, Object>();
			JSONArray ja = new JSONArray();
			JSONObject tag = null;
			for(int i = 0;i<index;i++) {
				json = new JSONArray();
				tag = new JSONObject();
				temp = new JSONArray();
				json.add(list.get(i).keyword);
				tag.put("homeCotent", json);
				temp = otherContent.getContents(module, null, (byte)4, null, null, null, tag, 5, 0);
				for(int j=0;j<temp.size();j++) {
					map.put(temp.getJSONObject(j).getLong("id"),temp.get(j));
				}
			}
			for(Long key:map.keySet()) {
				ja.add(map.get(key));
			}
//			ja = otherContent.getContents(module, null, (byte)4, null, null, null, tag, 15, offset);
			return ja;
		}
	}
	public List<Content> getContentsByPageView(int count,
			int offset) throws ServerException, SQLException{
		return contentService.getContentsByPageView("1180",count,offset);
	}
	@POSTAPI(//
		path = "getConntent", 
		des = "获取内容详情,添加用户兴趣标签", 
		ret = "" 
	)
	public Content getConntent(
			Long id,
			@P(t = "用户id",r = false)Long userId
		) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			Content c = contentService.getConntent(id);
			if(userId == null || c == null) {
				return c;
			}
			JSONArray ja = c.tags.getJSONArray("homeCotent");
			List<InterestTag> list = interestTagRepository.getList(conn, EXP.INS().key("user_id",userId), 300, 0);
			List<String> tagList = new LinkedList<String>();
			for(int i=0,index = list.size();i<index;i++) {
				tagList.add(list.get(i).keyword);
			}
			for(int i=0,index = ja.size();i<index;i++) {
				keywordRepository.addKeywordPageview(conn, ja.getString(i));
				if(tagList.contains(ja.getString(i))) {//判断关键词是否存在与用户兴趣表中
					interestTagRepository.addInteresttagPageview(userId,ja.getString(i));//有就浏览量加一
				}else {
					if(ja.getString(i) != null && ja.getString(i).length()>0) {
						createInterestTag(userId,ja.getString(i));//没有就创建						
					}
				}
			}
			return c;
		}
	}
	@POSTAPI(//
			path = "createInterestTag", 
			des = "创建用户兴趣标签",
			ret = "" 
		)
		public void createInterestTag(
			@P(t = "用户id") Long userId,
			@P(t = "标签数组") String tag
		) throws ServerException, SQLException {
			try(DruidPooledConnection conn = ds.getConnection()){
				InterestTag it = new InterestTag();
				it.id = IDUtils.getSimpleId();
				it.userId  = userId;
				it.keyword = tag;
				it.pageView = 0;
				interestTagRepository.insert(conn, it);
			}
		}
	/**
	 * 删除用户兴趣标签
	 */
	@POSTAPI(//
		path = "delInterestTag", 
		des = "删除用户兴趣标签", 
		ret = "" 
	)
	public int delInterestTag(
		@P(t = "编号") Long id
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			return interestTagRepository.delete(conn, EXP.INS().key("id", id));
		}
	}
	
	/**
	 * 查询用户兴趣标签
	 */
	@POSTAPI(//
		path = "getInterestTags", 
		des = "查询用户兴趣标签", 
		ret = "" 
	)
	public List<InterestTag> getInterestTags(
		@P(t = "用户id",r = false) Long userId,
		int count,
		int offset
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			List<InterestTag> list = interestTagRepository.getList(conn, EXP.INS(false).andKey("user_id", userId), count, offset);
			return list;
		}
	}
	@POSTAPI(//
			path = "getAllInterestTag", 
			des = "查询用户兴趣标签", 
			ret = "" 
		)
		public JSONArray getAllInterestTag(
			int count,
			int offset
		) throws ServerException, SQLException {
			try(DruidPooledConnection conn = ds.getConnection()){
				return interestTagRepository.getAllInterestTag(count, offset);
			}
		}
}
