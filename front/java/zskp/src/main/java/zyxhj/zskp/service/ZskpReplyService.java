package zyxhj.zskp.service;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import zyxhj.cms.domian.Content;
import zyxhj.cms.repository.ContentRepository;
import zyxhj.cms.service.ReplyService;
import zyxhj.core.domain.Comment;
import zyxhj.core.domain.Reply;
import zyxhj.utils.Singleton;
import zyxhj.utils.api.APIResponse;
import zyxhj.utils.api.Controller;
import zyxhj.utils.api.RC;
import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.EXP;
import zyxhj.zskp.domain.Isread;
import zyxhj.zskp.domain.ZskpUser;
import zyxhj.zskp.repository.IsreadRepository;
import zyxhj.zskp.repository.UserRepository;

public class ZskpReplyService  extends Controller{
	private static Logger log = LoggerFactory.getLogger(ReplyService.class);

	private DruidDataSource ds;

	private UserRepository userRepository;
	private ContentRepository contentRepository;
	private IsreadRepository isreadRepository;

	private ReplyService replyService;
	public ZskpReplyService(String node) {
		super(node);
		try {
			ds = DataSource.getDruidDataSource("rdsDefault.prop");
			userRepository = Singleton.ins(UserRepository.class);
			contentRepository = Singleton.ins(ContentRepository.class);
			replyService = Singleton.ins(ReplyService.class);
			isreadRepository = Singleton.ins(IsreadRepository.class);
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
			if("5".equals(c.status.toString())) {
				return APIResponse.getNewFailureResp(new RC("fail","该内容下已被设置为不可评论"));
			}else if ("1".equals(user.id.toString())) {
				return APIResponse.getNewFailureResp(new RC("fail","你已被禁言"));
			}
			Reply re =  replyService.createReply(ownerId, upUserId, atUserId, atUserName, title, text, ext);
			Isread t = new Isread();
			t.userId = upUserId;
			t.replyId = re.sequenceId;
			isreadRepository.insert(conn, t);
			return APIResponse.getNewSuccessResp(re);
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
			@P(t = "目标用户编号",r = false) Long toUserId, //
			@P(t = "目标用户昵称",r = false) String toUserName //
	) throws ServerException, SQLException {
		try (DruidPooledConnection conn = ds.getConnection()) {
			ZskpUser user = userRepository.get(conn, EXP.INS().key("id", upUserId));
			if ("1".equals(user.id.toString())) {
				return APIResponse.getNewFailureResp(new RC("fail", "你已被禁言"));
			}
			return replyService.createComment(replyId, upUserId, upUserHead, upUserName, text, toUserId, toUserName);
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
			return isreadRepository.getList(conn, EXP.INS().key("user_id",userId), 99,0).size();
		}
	}
}
