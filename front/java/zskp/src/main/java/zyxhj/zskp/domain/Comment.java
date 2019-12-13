package zyxhj.zskp.domain;

import java.util.Date;


import zyxhj.utils.data.AnnDicField;
import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;
@RDSAnnEntity(alias = "tb_zskt_comment")
public class Comment {
	/**
	 * 序列编号（id）
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long sequenceId;
	/**
	 * 回复replyid
	 */
	@RDSAnnField(column = RDSAnnField.ID)
	public Long replyId;
	/**
	 * 创建时间
	 */
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date createTime;
	/**
	 * 状态
	 */
	@RDSAnnField(column = RDSAnnField.BYTE)
	public Byte status;
	/**
	 * 回复用户 id
	 */
	@RDSAnnField(column = RDSAnnField.ID)
	public Long upUserId;
	/**
	 * 回复用户头像
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String upUserHead;
	/**
	 * 回复用户名称
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String upUserName;
	/**
	 * 目标用户 id
	 */
	@RDSAnnField(column = RDSAnnField.ID)
	public Long toUserId;
	/**
	 * 目标用户 名称
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String toUserName;
	/**
	 * 文本（可存html信息）
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String text;
	 
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String ext;

	/////////////////////////////////////////////

	
	@AnnDicField(alias = "已通过")
	public static final Byte STATUS_ACCEPT = 0;
	@AnnDicField(alias = "未审核")
	public static final Byte STATUS_UNEXAMINED = 1;
}
