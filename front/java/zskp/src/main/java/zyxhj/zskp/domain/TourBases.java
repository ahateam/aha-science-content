package zyxhj.zskp.domain;

import java.util.Date;

import com.alibaba.fastjson.JSONArray;

import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

/**
 * 基地景点
 */
@RDSAnnEntity(alias = "tb_zskp_tourBase")
public class TourBases {
	
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long moduleId;
	/**
	 * 自增id
	 */
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long id;
	/**
	 * 名称
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String name;
	/**
	 * 地址
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String address;
	
	/**
	 * 地址经纬度坐标
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String coordinate;
	/**
	 * 数据
	 */
	@RDSAnnField(column = RDSAnnField.TEXT)
	public String data;
	
	/**
	 * 创建时间
	 */
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date createTime;
	/**
	 * 购票链接
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String buyTicketsLink;
	
}
