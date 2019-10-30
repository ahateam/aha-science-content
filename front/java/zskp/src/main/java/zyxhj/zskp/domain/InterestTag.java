package zyxhj.zskp.domain;

import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

/**
 * 用户感兴趣标签
 */
@RDSAnnEntity(alias = "tb_zskt_interestTag")
public class InterestTag {
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long moduleId;
	
	/**
	 * 用户id
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long userId;
	
	/**
	 * 标签数组
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String tagsArray;

}
