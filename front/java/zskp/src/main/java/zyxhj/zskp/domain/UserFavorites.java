package zyxhj.zskp.domain;

import java.util.Date;

import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

/*
 * 用户关注
 */
@RDSAnnEntity(alias = "tb_zskt_userFavorites")
public class UserFavorites {
	
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long moduleId;
	
	/**
	 * id
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long id;
	/**
	 * 被关注id
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long concernId;
	/**
	 * 关注id
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long userId;
	/**
	 * 创建时间
	 */
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date createTime;
}
