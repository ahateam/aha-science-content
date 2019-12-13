package zyxhj.zskp.domain;

import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

@RDSAnnEntity(alias = "tb_zskt_sensitiveWord")
public class SensitiveWord {
	/*
	 * 敏感词id
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long wordId;
	/*
	 * 敏感词
	 */
	@RDSAnnField(column = RDSAnnField.TEXT_TITLE)
	public String badword;
	/*
	 * 类型
	 * 0:过滤*
	 * 1：人工审核
	 */
	@RDSAnnField(column = RDSAnnField.BYTE)
	public Byte type;
	
}
