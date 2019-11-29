package zyxhj.zskp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONObject;

import zyxhj.utils.Singleton;
import zyxhj.utils.api.Controller;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.EXP;
import zyxhj.zskp.domain.Appraise;
import zyxhj.zskp.repository.AppraiseRepository;

public class AppraiseService extends Controller {

	private static Logger log = LoggerFactory.getLogger(AppraiseService.class);
	private AppraiseRepository appraiseRepository;
	private DruidDataSource ds;

	public AppraiseService(String node) {
		super(node);
		try {
			ds = DataSource.getDruidDataSource("rdsDefault.prop");
			appraiseRepository = Singleton.ins(AppraiseRepository.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@POSTAPI(//
			path = "createAppraise", //
			des = "创建点赞或踩(0:赞 1:踩)", //
			ret = "Appraise实例" //
	)
	public Appraise createAppraise(
			@P(t = "内容编号") Long ownerId, //
			@P(t = "用户编号") Long userId, //
			@P(t = "状态") Byte value //
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()) {
			Appraise appraise = new Appraise();
			if (!judgeAppraise(ownerId, userId)) {
				appraise.ownerId = ownerId;
				appraise.userId = userId;
				appraise.value = value;
				appraiseRepository.insert(conn, appraise);
				return appraise;
			} else {
				appraise.value = 10;// 10表示已经点过赞
				return appraise;
			}
		}
	}

	@POSTAPI(//
			path = "delAppraise", //
			des = "删除点赞或踩" //
	)
	public void delAppraise(
			@P(t = "内容编号") Long ownerId, 
			@P(t = "用户编号") Long userId
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()) {
			appraiseRepository.delete(conn, EXP.INS().key("owner_id", ownerId).andKey("user_id", userId));
		}
	}

	@POSTAPI(//
			path = "editAppraise", //
			des = "修改状态(0:赞 1:踩)" //
	)
	public void editAppraise(
			@P(t = "内容编号") Long ownerId, 
			@P(t = "用户编号") Long userId, 
			@P(t = "状态 0赞 ，1踩") String value
	) throws Exception {
		try(DruidPooledConnection conn = ds.getConnection()) {
			appraiseRepository.update(conn, EXP.INS().key("value", value), EXP.INS().key("owner_id", ownerId).andKey("user_id", userId));
		}
	}

	@POSTAPI(//
			path = "getAppraiseCount", //
			des = "根据内容编号，用户id，状态获取数据,没有用户编号则获取内容下的所有赞，有用户则获取此内容，此用户的赞，以此类推" //
	)
	public JSONObject getAppraiseCount(
			@P(t = "内容编号") Long ownerId, 
			@P(t = "用户编号", r = false) Long userId,
			@P(t = "状态", r = false) String value
	) throws Exception {
		try (DruidPooledConnection conn = ds.getConnection()) {
			Long count = appraiseRepository.getAppraiseCount(conn, ownerId);
			   Appraise appraise = appraiseRepository.get(conn,
			     EXP.INS().key("owner_id", ownerId).andKey("user_id", userId));
			   JSONObject jo = new JSONObject();
			   jo.put("appraiseCount", count);
			   jo.put("isAppraise", appraise!=null);
			   return jo;		
		}
	}

	//
	@POSTAPI(//
			path = "judgeAppraise", //
			des = "判断是否点过赞" //
	)
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


}