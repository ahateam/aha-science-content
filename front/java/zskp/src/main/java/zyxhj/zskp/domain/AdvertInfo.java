package zyxhj.zskp.domain;

import java.util.Date;

import zyxhj.utils.data.AnnDicField;
import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

/**
 * 广告
 */
@RDSAnnEntity(alias = "tb_zskt_advertInfo")
public class AdvertInfo {
	
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long moduleId;
	
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long id;
	/**
	 * 状态
	 */
	@RDSAnnField(column = RDSAnnField.BYTE)
	public Byte status;
	/**
	 * 图片地址
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String imgSrc;
	/**
	 * 详情链接
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String linkSrc;
	/**
	 * 创建时间
	 */
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date createTime;
	/**
	 * 备注
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String remark;
	
	@RDSAnnField(column = RDSAnnField.INTEGER)
	public Integer sortSize;
	
	@AnnDicField(alias = "启用")
	public static final Byte STATUS_OPEN = 0;
	@AnnDicField(alias = "禁用")
	public static final Byte STATUS_CLOSE = 1;
}
