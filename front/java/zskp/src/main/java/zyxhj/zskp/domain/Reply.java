package zyxhj.zskp.domain;

import java.util.Date;
import zyxhj.utils.data.AnnDicField;
import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

@RDSAnnEntity(alias = "tb_zskt_reply")
public class Reply {

	/**
	 * 序列编号（id）
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long sequenceId;
	/**
	 * 回复所属对象编号
	 */
	@RDSAnnField(column = RDSAnnField.ID)
	public Long ownerId;
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
	 * 上传用户编号
	 */
	@RDSAnnField(column = RDSAnnField.ID)
	public Long upUserId;
	/**
	 * 被@的用户编号
	 */
	@RDSAnnField(column = RDSAnnField.ID)
	public Long atUserId;
	/**
	 * 被@的用户名称
	 */
	@RDSAnnField(column = RDSAnnField.TEXT_NAME)
	public String atUserName;
	/**
	 * 标题
	 */
	@RDSAnnField(column = RDSAnnField.TEXT_TITLE)
	public String title;
	/**
	 * 文本（可存html信息）
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String text;
	/**
	 * 扩展信息，可用JSON格式自行扩展
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String ext;

	/////////////////////////////////////////////
	/////////////////////////////////////////////
	/////////////////////////////////////////////

	@AnnDicField(alias = "已通过")
	public static final Byte STATUS_ACCEPT = 0;

	@AnnDicField(alias = "未审核")
	public static final Byte STATUS_UNEXAMINED = 1;

	@AnnDicField(alias = "已回绝")
	public static final Byte STATUS_REJECT = 2;
}
