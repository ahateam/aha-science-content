package zyxhj.zskp.domain;

import java.util.Date;

import zyxhj.utils.data.AnnDicField;
import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

/**
 * 用户申请内部人员表
 */
@RDSAnnEntity(alias = "tb_zskt_applyAuthority")
public class ApplyAuthority {
	
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long moduleId;
	
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long id;
	/**
	 * 用户id
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long userId;
	/**
	 * 状态
	 */
	@RDSAnnField(column = RDSAnnField.BYTE)
	public Byte status;
	/**
	 * 创建时间
	 */
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date createTime;
	
	
	@AnnDicField(alias = "待审核")
	public static final Byte STATUS_WAIT = 0;
	@AnnDicField(alias = "审核通过")
	public static final Byte STATUS_SUSSESS = 1;
	@AnnDicField(alias = "审核不通过")
	public static final Byte STATUS_LOSE = 2;

}
