package zyxhj.zskp.domain;

import java.util.Date;

import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;
/**
 * 文章关键词
 */
@RDSAnnEntity(alias = "tb_zskt_Keyword")
public class ZskpKeyword {
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long id;
	
	/**
	 * 发布者用户id
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long userId;
	/**
	 * 关键词
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String keyword;
	/**
	 * 浏览量
	 */
	@RDSAnnField(column = RDSAnnField.LONG)
	public Integer pageView;
	/**
	 * 创建时间
	 */
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date createTime;
	
}
