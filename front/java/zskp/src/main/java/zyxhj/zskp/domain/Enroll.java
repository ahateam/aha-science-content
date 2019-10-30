package zyxhj.zskp.domain;

import java.util.Date;

import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

/**
 * 报名
 */
@RDSAnnEntity(alias = "tb_zskt_enroll")
public class Enroll {
	
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long moduleId;
	
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long id;
	/**
	 * 内容id
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long contenId;
	/**
	 * 用户id
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long userId;
	/**
	 * 用户名
	 */
	@RDSAnnField(column = RDSAnnField.TEXT_NAME)
	public String name;
	
	/**
	 * 内容标题
	 */
	@RDSAnnField(column = RDSAnnField.TEXT_TITLE)
	public String contentName;
	/**
	 * 手机号
	 */
	@RDSAnnField(column = RDSAnnField.TEXT_TITLE)
	public String phone;
	/**
	 * 报名人数
	 */
	@RDSAnnField(column = RDSAnnField.TEXT_TITLE)
	public String number;
	
	/**
	 * 创建时间
	 */
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date createTime;

}
