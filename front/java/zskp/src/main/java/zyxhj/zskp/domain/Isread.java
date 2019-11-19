package zyxhj.zskp.domain;


import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

/**
 * 基地景点
 */
@RDSAnnEntity(alias = "tb_zskt_isread")
public class Isread {
	@RDSAnnField(column = RDSAnnField.ID)
	public Long userId;
	
	/**
	 * 评论id
	 */
	@RDSAnnField(column = RDSAnnField.ID)
	public Long replyId;
	/**
	 * 回复
	 */
	@RDSAnnField(column = RDSAnnField.ID)
	public Long commId;

}
