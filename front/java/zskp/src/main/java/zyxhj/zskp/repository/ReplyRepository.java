package zyxhj.zskp.repository;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.EXP;
import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.Reply;

public class ReplyRepository extends RDSRepository<Reply>{
	public ReplyRepository() {
		super(Reply.class);
	}
	public JSONArray getReplyList(DruidPooledConnection conn,Long id,Long upUserId,String status,Boolean orderDesc,Long toUserId,Integer count, Integer offset) throws ServerException {
		StringBuffer sql = new StringBuffer("SELECT reply.*,user.id,user.head,user.name,truncate(con.id,0) contentId,con.type,con.title contentTitle,(SELECT count(1) FROM tb_zskp_appraise WHERE owner_id = ");
		sql.append("(reply.sequence_id)");
		sql.append(") as appraiseCount FROM tb_zskt_reply reply INNER JOIN tb_zskt_user user ON reply.up_user_id = user.id INNER JOIN tb_cms_content con on reply.owner_id = con.id WHERE ");
		 
		EXP exp = EXP.INS(false).key("owner_id", id).andKey("reply.up_user_id", upUserId).andKey("reply.to_user_id", toUserId);
		if(orderDesc) {
			exp.append("ORDER BY create_time DESC");
		}else {
			exp.append("ORDER BY create_time asc");
		}
		List<Object> params = new ArrayList<Object>();
		exp.toSQL(sql, params);
		return this.sqlGetJSONArray(conn, sql.toString(), params, count, offset);
	}
	public JSONObject getReply(DruidPooledConnection conn,Long id) throws ServerException {
		StringBuffer sql = new StringBuffer("SELECT reply.*,user.id,user.head,user.name ,(SELECT count(1) FROM tb_zskp_appraise WHERE owner_id = ");
		sql.append("(reply.sequence_id)");
		sql.append(") as appraiseCount FROM tb_zskt_reply reply LEFT JOIN tb_zskt_user user ON reply.up_user_id = user.id WHERE reply.sequence_id = ");
		sql.append(id);
		List<Object> params = new ArrayList<Object>();
		return this.sqlGetJSONObject(conn, sql.toString(), params);
	}
	public JSONArray getReplyListByStatus(DruidPooledConnection conn,Integer count, Integer offset) throws ServerException {
		StringBuffer sql = new StringBuffer("SELECT  con.id conId,trim(con.title) contitle,re.*,user.id,user.name FROM tb_zskt_reply re LEFT JOIN tb_cms_content con ON re.owner_id = con.id LEFT JOIN tb_zskt_user user ON re.up_user_id = user.id WHERE re.status = 1");
		List<Object> params = new ArrayList<Object>();
		return this.sqlGetJSONArray(conn, sql.toString(), params, count, offset);
	}
}
