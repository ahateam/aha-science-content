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
	
	//注册用户添加3个兴趣标签
	//推荐：查询用户兴趣标签的3个标签内容
	//点击内容：
	//没有：在用户兴趣标签里添加当前内容的关键词
	//有：在用户兴趣标签里关键词浏览量加1
	//第二次
	//查询用户的兴趣标签里排序为前3的

}
