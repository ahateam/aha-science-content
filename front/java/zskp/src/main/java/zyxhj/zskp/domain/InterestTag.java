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
	public Long id;
	
	/**
	 * 用户id
	 */
	@RDSAnnField(column = RDSAnnField.ID)
	public Long userId;
	
	/**
	 * 用户浏览关键词
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String keyword;
	/**
	 * 浏览量
	 */
	@RDSAnnField(column = RDSAnnField.LONG)
	public Integer pageView;
}
