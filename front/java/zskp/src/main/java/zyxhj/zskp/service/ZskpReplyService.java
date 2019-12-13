package zyxhj.zskp.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONArray;

import zyxhj.cms.domian.Content;
import zyxhj.cms.repository.ContentRepository;
import zyxhj.cms.service.ReplyService;
import zyxhj.utils.IDUtils;
import zyxhj.utils.Singleton;
import zyxhj.utils.api.APIResponse;
import zyxhj.utils.api.Controller;
import zyxhj.utils.api.RC;
import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.EXP;
import zyxhj.zskp.domain.Comment;
import zyxhj.zskp.domain.Isread;
import zyxhj.zskp.domain.Reply;
import zyxhj.zskp.domain.SensitiveWord;
import zyxhj.zskp.domain.ZskpUser;
import zyxhj.zskp.repository.CommentRepository;
import zyxhj.zskp.repository.IsreadRepository;
import zyxhj.zskp.repository.ReplyRepository;
import zyxhj.zskp.repository.SensitiveWordRepository;
import zyxhj.zskp.repository.UserRepository;
import zyxhj.zskp.util.SensitivewordFilter;

public class ZskpReplyService extends Controller {
	private static Logger log = LoggerFactory.getLogger(ReplyService.class);

	private DruidDataSource ds;

	private UserRepository userRepository;
	private ContentRepository contentRepository;
	private IsreadRepository isreadRepository;
	private ReplyRepository replyRepository;
	private CommentRepository commentRepository;
	private SensitivewordFilter sensitivewordFilter;
	private SensitiveWordRepository sensitiveWordRepository;
	public ZskpReplyService(String node) {
		super(node);
		try {
			ds = DataSource.getDruidDataSource("rdsDefault.prop");
			userRepository = Singleton.ins(UserRepository.class);
			contentRepository = Singleton.ins(ContentRepository.class);
			isreadRepository = Singleton.ins(IsreadRepository.class);
			replyRepository = Singleton.ins(ReplyRepository.class);
			commentRepository = Singleton.ins(CommentRepository.class);
			sensitivewordFilter = Singleton.ins(SensitivewordFilter.class);
			sensitiveWordRepository = Singleton.ins(SensitiveWordRepository.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	@POSTAPI(//
		path = "createReply", //
		des = "创建回复", //
		ret = "Reply实例" //
	)
	public APIResponse createReply(//
		@P(t = "持有者编号") Long ownerId, //
		@P(t = "提交者编号") Long upUserId, //
		@P(t = "@对象编号") Long atUserId, //
		@P(t = "@对象名称") String atUserName, //
		@P(t = "标题") String title, //
		@P(t = "正文") String text, //
		@P(t = "扩展") String ext//
	) throws ServerException, SQLException {
		try (DruidPooledConnection conn = ds.getConnection()) {
			Content c = contentRepository.get(conn, EXP.INS().key("id", ownerId));
			ZskpUser user = userRepository.get(conn, EXP.INS().key("id", upUserId));
			if ("5".equals(c.status.toString())) {
				return APIResponse.getNewFailureResp(new RC("fail", "该内容下已被设置为不可评论"));
			} else if ("1".equals(user.status.toString())) {
				return APIResponse.getNewFailureResp(new RC("fail", "你已被禁言"));
			}
			Reply reply = new Reply();
			reply.sequenceId = IDUtils.getSimpleId();
			reply.ownerId = ownerId;
			reply.createTime = new Date();
			reply.status = Reply.STATUS_ACCEPT;
			reply.upUserId = upUserId;
			reply.atUserId = atUserId;
			reply.atUserName = atUserName;
			reply.title = title;
			String temp = sensitivewordFilter.StringFilterMain(text);
			if(temp.equals(text)) {//如果是争议敏感词
				reply.status = (byte)sensitivewordFilter.StringFilterMainByExamine(text);
			}
			reply.text = temp;
			reply.ext = "1";
			replyRepository.insert(conn, reply);
			return APIResponse.getNewSuccessResp(reply);
		}
	}

	@POSTAPI(//
			path = "createComment", //
			des = "创建二级回复", //
			ret = "Comment实例" //
	)
	public APIResponse createComment(//
			@P(t = "一级评论id") Long replyId, //
			@P(t = "提交者编号") Long upUserId, //
			@P(t = "提交者头像") String upUserHead, //
			@P(t = "提交者昵称") String upUserName, //
			@P(t = "正文") String text, //
			@P(t = "目标用户编号", r = false) Long toUserId, //
			@P(t = "目标用户昵称", r = false) String toUserName //
	) throws ServerException, SQLException {
		try (DruidPooledConnection conn = ds.getConnection()) {
			ZskpUser user = userRepository.get(conn, EXP.INS().key("id", upUserId));
			if ("1".equals(user.status.toString())) {
				return APIResponse.getNewFailureResp(new RC("fail", "你已被禁言"));
			}
			Comment c = new Comment();
			c.sequenceId = IDUtils.getSimpleId();
			c.replyId = replyId;
			c.createTime = new Date();
			c.status = Reply.STATUS_ACCEPT;
			c.upUserId = upUserId;
			c.upUserHead = upUserHead;
			c.upUserName = upUserName;
			String temp = sensitivewordFilter.StringFilterMain(text);
			if(temp.equals(text)) {//如果是争议敏感词
				c.status = (byte)sensitivewordFilter.StringFilterMainByExamine(text);
			}
			c.text = temp;
			c.ext = "0";
			if(toUserId != null && toUserName != null) {
				c.toUserId = toUserId;
				c.toUserName = toUserName;			
			}else {
				c.toUserId = 0L;
				c.toUserName = "no";	
			}
			Isread t = new Isread();
			t.userId = upUserId;
			t.replyId = c.sequenceId;
			isreadRepository.insert(conn, t);
			commentRepository.insert(conn, c);
			Reply re = new Reply();
			re.ext = "0";
			replyRepository.update(conn, EXP.INS().andKey("sequence_id", replyId),re,true);			
			return APIResponse.getNewSuccessResp(c);
		}
	}
	@POSTAPI(//
			path = "updateReply", //
			des = "修改评论", //
			ret = "" //
	)
	public void updateReply(//
			@P(t = "评论id") Long id //
	) throws ServerException, SQLException {
		try (DruidPooledConnection conn = ds.getConnection()) {
			Reply r = new Reply();
			r.status = Reply.STATUS_ACCEPT;
			replyRepository.update(conn, EXP.INS().key("sequence_id", id),r,true);
		}
	}

	@POSTAPI(//
			path = "getIsRead", //
			des = "查询是否有未读消息", //
			ret = "Comment实例" //
	)
	public int getIsRead(//
			@P(t = "用户id") Long userId //
	) throws ServerException, SQLException {
		try (DruidPooledConnection conn = ds.getConnection()) {
			return isreadRepository.getList(conn, EXP.INS().key("user_id", userId), 99, 0).size();
		}
	}
	
	@POSTAPI(//
			path = "delReply", //
			des = "删除回复" //
	)
	public void delReply(//
			@P(t = "持有者编号") Long ownerId, //
			@P(t = "序列编号") Long sequenceId, //
			@P(t = "评论编号",r = false) Long replyId,
			@P(t = "删除评论还是回复",r = false) String isReply //
	) throws ServerException, SQLException {
		try (DruidPooledConnection conn = ds.getConnection()) {
			if("reply".equals(isReply)) {				
				replyRepository.delete(conn, EXP.INS().key("owner_id", ownerId).andKey("sequence_id", sequenceId));
				commentRepository.delete(conn, EXP.INS().key("reply_id", sequenceId));
			}else if("comment".equals(isReply)) {
				commentRepository.delete(conn, EXP.INS().key("sequence_id", sequenceId));
			}
		}
	}
	@POSTAPI(//
			path = "getCommentListByStatus", //
			des = "状态获取敏感回复列表" //
	)
	public JSONArray getCommentListByStatus(//
			Integer count, Integer offset//
	) throws Exception {
		try (DruidPooledConnection conn = ds.getConnection()) {
			return commentRepository.getCommentListByStatus(conn,count,offset);
		}
	}
	@POSTAPI(//
			path = "updateComment", //
			des = "修改回复" //
	)
	public int updateComment(//
			Long id
	) throws Exception {
		try (DruidPooledConnection conn = ds.getConnection()) {
			Comment c = new Comment();
			c.status = Comment.STATUS_ACCEPT;
			return commentRepository.update(conn, EXP.INS().key("sequence_id", id),c,true);
		}
	}
	
	@POSTAPI(//
			path = "getFilterText", 
			des = "读取敏感词", 
			ret = "" 
		)
		public List<SensitiveWord> getFilterText(
				@P(t = "类型") Byte type,
				Integer count,
				Integer offset
		) throws ServerException, SQLException {
			try(DruidPooledConnection conn = ds.getConnection()){
				List<SensitiveWord> list =  sensitiveWordRepository.getList(conn, EXP.INS().key("type", type),count,offset);
				return list;
			}
		}
	
	@POSTAPI(//
		path = "createSensitiveWord", 
		des = "创建", 
		ret = "" 
	)
	public SensitiveWord createSensitiveWord(
			@P(t = "类型") Byte type,
			@P(t = "名称") String badword
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			SensitiveWord s = new SensitiveWord();
			s.wordId = IDUtils.getSimpleId();
			s.badword = badword;
			s.type = type;
			sensitiveWordRepository.insert(conn, s);
			return s;
		}
	}
	@POSTAPI(//
		path = "updateSensitiveWord", 
		des = "修改", 
		ret = "" 
	)
	public SensitiveWord updateSensitiveWord(
			@P(t = "id") JSONArray ids,
			@P(t = "名称", r= false) String badword,
			@P(t = "类型", r= false) Byte type
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			SensitiveWord s = new SensitiveWord();
			s.badword = badword;
			s.type = type;
			sensitiveWordRepository.update(conn, EXP.INS().IN("word_id", ids),s,true);
			return s;
		}
	}

	@POSTAPI(//
		path = "delSensitiveWord", 
		des = "删除", 
		ret = "" 
	)
	public int delSensitiveWord(
			@P(t = "id") JSONArray ids
	) throws ServerException, SQLException {
		try(DruidPooledConnection conn = ds.getConnection()){
			return sensitiveWordRepository.delete(conn, EXP.INS().IN("word_id", ids));
		}
	}
	@POSTAPI(//
			path = "searchSensitiveWord", 
			des = "搜索", 
			ret = "" 
		)
		public List<SensitiveWord> searchSensitiveWord(
				@P(t = "类型") Byte type,
				@P(t = "搜字符") String badword
		) throws ServerException, SQLException {
			try(DruidPooledConnection conn = ds.getConnection()){
				return sensitiveWordRepository.getList(conn, EXP.INS().key("type", type).and(EXP.INS().LIKE("badword", badword)),null,null);
			}
		}
}
