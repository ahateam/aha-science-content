package zyxhj.zskp.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
			List<InterestTag> list = interestTagRepository.getList(conn, EXP.INS(false).andKey("user_id", userId).append("order by page_view desc"), 3, 0);
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
	public Content getConntent(
			Long id,
			@P(t = "用户id",r = false)Long userId
		) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			if(userId == null) {
				return contentService.getConntent(id);
			}
			Content c = contentService.getConntent(id);
			JSONArray ja = c.tags.getJSONArray("homeCotent");
			List<InterestTag> list = interestTagRepository.getList(conn, EXP.INS().key("user_id",userId), 300, 0);
			int index = list.size();
			if(list != null && index>0) {
				for(int j=0;j<ja.size();j++) {
					boolean temp = false;
					for(int i =0;i<index;i++) {
						if(ja.getString(j).equals(list.get(i).keyword)) {//如果已存在，加1
							interestTagRepository.addInteresttagPageview(userId);
							break;
						}
						temp = true;
					}
					if(temp) {
						createInterestTag(userId,ja.getString(j));
					}
				}
			}else {
				createInterestTagByfirst(userId, ja);
			}
			return c;
		}
	}
	/**
	 * 创建用户兴趣标签
	 */
	@POSTAPI(//
		path = "createInterestTagByfirst", 
		des = "第一次创建用户兴趣标签",
		ret = "" 
	)
	public void createInterestTagByfirst(
		@P(t = "用户id") Long userId,
		@P(t = "标签数组") JSONArray tagArray
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			InterestTag it = null;
			List<InterestTag> list = new LinkedList<InterestTag>();
			for(int i=0,index = tagArray.size();i<index;i++) {
				it = new InterestTag();
				it.id = IDUtils.getSimpleId();
				it.userId  = userId;
				it.keyword = tagArray.getString(i);
				it.pageView = 0;
				list.add(it);
			}
			interestTagRepository.insertList(conn, list);
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
}
