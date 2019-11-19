package zyxhj.zskp.domain;

import zyxhj.utils.data.AnnDicField;
import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

@RDSAnnEntity(alias = "tb_zskp_appraise")
public class Appraise {

	/**
	 * 回复所属对象编号
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long ownerId;

	/**
	 * 用户id
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long userId;

	/**
	 * 评价的值
	 */
	@RDSAnnField(column = RDSAnnField.BYTE)
	public Byte value;

	/////////////////////////////////////////////
	/////////////////////////////////////////////
	/////////////////////////////////////////////

	@AnnDicField(alias = "赞")
	public static final Byte VALUE_PRAISE = 0;

	@AnnDicField(alias = "踩")
	public static final Byte STATUS_DISS = 1;

}
