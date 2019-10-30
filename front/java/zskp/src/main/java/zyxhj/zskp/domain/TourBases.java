package zyxhj.zskp.domain;

import java.util.Date;

import com.alicloud.openservices.tablestore.model.PrimaryKeyType;
import com.alicloud.openservices.tablestore.model.search.FieldType;

import zyxhj.utils.data.ts.TSAnnEntity;
import zyxhj.utils.data.ts.TSAnnField;
import zyxhj.utils.data.ts.TSAnnID;
import zyxhj.utils.data.ts.TSAnnIndex;
import zyxhj.utils.data.ts.TSEntity;


/**
 * 基地景点
 */
@TSAnnEntity(alias = "tb_zskp_tourBase", indexName = "index_zskp_tourBase")
public class TourBases  extends TSEntity {
	
	@TSAnnIndex(type = FieldType.LONG, enableSortAndAgg = true, store = true)
	@TSAnnID(key = TSAnnID.Key.PK1, type = PrimaryKeyType.INTEGER)
	public Long moduleId;
	/**
	 * 自增id
	 */
	@TSAnnIndex(type = FieldType.LONG, enableSortAndAgg = true, store = true)
	@TSAnnID(key = TSAnnID.Key.PK2, type = PrimaryKeyType.INTEGER, AUTO_INCREMENT = true)
	public Long id;
	/**
	 * 名称
	 */
	@TSAnnIndex(type = FieldType.TEXT, enableSortAndAgg = false, store = false)
	@TSAnnField(column = TSAnnField.ColumnType.STRING)
	public String name;
	/**
	 * 地址
	 */
	@TSAnnField(column = TSAnnField.ColumnType.STRING)
	public String address;
	
	/**
	 * 地址经纬度坐标
	 */
	@TSAnnIndex(type = FieldType.GEO_POINT, enableSortAndAgg = true, store = true)
	@TSAnnField(column = TSAnnField.ColumnType.STRING)
	public String coordinate;
	/**
	 * 数据
	 */
	@TSAnnField(column = TSAnnField.ColumnType.STRING)
	public String data;
	
	/**
	 * 创建时间
	 */
	@TSAnnIndex(type = FieldType.LONG, enableSortAndAgg = true, store = true)
	@TSAnnField(column = TSAnnField.ColumnType.INTEGER)
	public Date createTime;
	/**
	 * 购票链接
	 */
	@TSAnnField(column = TSAnnField.ColumnType.STRING)
	public String buyTicketsLink;
	

}
