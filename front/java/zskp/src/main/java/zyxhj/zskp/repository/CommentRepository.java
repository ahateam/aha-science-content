package zyxhj.zskp.repository;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONArray;

import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.EXP;
import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.Comment;

public class CommentRepository extends RDSRepository<Comment>{
	public CommentRepository() {
		super(Comment.class);
	}

	public JSONArray getCommentList(DruidPooledConnection conn,Long id,Long upUserId,String status,Boolean orderDesc,Long toUserId,Integer count, Integer offset) throws ServerException {
		StringBuffer sql = new StringBuffer("SELECT comment.*, user.id,user.`name`,user.head ,(SELECT count(1) FROM tb_zskp_appraise WHERE owner_id = ");
		sql.append("(comment.sequence_id)");
		sql.append(") as appraiseCount from tb_zskt_comment comment left join tb_zskt_user user on comment.up_user_id = user.id where ");
		EXP exp = EXP.INS(false).key("reply_id", id).andKey("up_user_id", upUserId).andKey("to_user_id", toUserId);
		if(orderDesc) {
			exp.append("ORDER BY create_time DESC");
		}else {
			exp.append("ORDER BY create_time ASC");
		}
		List<Object> params = new ArrayList<Object>();
		exp.toSQL(sql, params);
		return this.sqlGetJSONArray(conn, sql.toString(), params, count, offset);
	}

	public JSONArray getCommentListByStatus(DruidPooledConnection conn,Integer count, Integer offset) throws ServerException {
		StringBuffer sql = new StringBuffer("SELECT com.*,us.id,us.name FROM tb_zskt_comment com LEFT JOIN tb_zskt_user us on com.up_user_id =us.id  WHERE com.status = 1 ");
		List<Object> params = new ArrayList<Object>();
		return this.sqlGetJSONArray(conn, sql.toString(), params, count, offset);
	}

	
}
