package zyxhj.zskp.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.Keyword;

import zyxhj.cms.domian.Content;
import zyxhj.cms.repository.ContentRepository;
import zyxhj.cms.service.ContentService;
import zyxhj.utils.IDUtils;
import zyxhj.utils.Singleton;
import zyxhj.utils.api.Controller;
import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.EXP;
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
			List<ZskpKeyword> list = keywordRepository.getList(conn, EXP.INS().key("userId", userId).append("order by sort_size desc"), 3, 0);
			int index = list.size();
			if(list == null || index<=0) {
				return otherContent.getContents(module, null, (byte)4, null, null, null, null, count, offset);
			}
			JSONArray json = new JSONArray();
			for(int i = 0;i<list.size();i++) {
				json.add(list.get(i).keyword);
			}
			JSONObject tag = new JSONObject();
			tag.put("homeCotent", json);
			return otherContent.getContents(module, null, (byte)4, null, null, null, tag, count, offset);
		}
	}
	@POSTAPI(//
		path = "getConntent", 
		des = "获取内容详情,添加用户兴趣标签", 
		ret = "" 
	)
	public Content getConntent(Long id,Long userId) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			Content c = contentService.getConntent(id);
			JSONArray ja = c.tags.getJSONArray("homeCotent");
			List<ZskpKeyword> list = keywordRepository.getList(conn, EXP.INS().key("user_id",userId), 300, 0);
			for(int j=0;j<ja.size();j++) {
				for(int i =0,index = list.size();i<index;i++) {
					 if(ja.getString(j).equals(list.get(i).keyword)) {
						interestTagRepository.addKeyword(userId);
					 }
				 }
				 createKeyword(userId, ja.getString(j));
			 }
			return c;
		}
	}	
}
