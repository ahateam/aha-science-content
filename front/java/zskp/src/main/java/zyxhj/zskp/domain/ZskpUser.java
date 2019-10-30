package zyxhj.zskp.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

import zyxhj.utils.data.AnnDicField;
import zyxhj.utils.data.rds.RDSAnnEntity;
import zyxhj.utils.data.rds.RDSAnnField;
import zyxhj.utils.data.rds.RDSAnnID;

/**
 * 用户表
 */
@RDSAnnEntity(alias = "tb_zskt_user")
public class ZskpUser {
	
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public String moduleId;
	
	@RDSAnnID
	@RDSAnnField(column = RDSAnnField.ID)
	public Long id;
	/**
	 * 头像
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String head;
	/**
	 * 昵称
	 */
	@RDSAnnField(column = RDSAnnField.TEXT_NAME)
	public String name;
	/**
	 * 学校/单位
	 */
	@RDSAnnField(column = RDSAnnField.TEXT_TITLE)
	public String company;
	/**
	 * 手机号
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String phone;
	/**
	 * 微信openID
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String openId;
	/**
	 * 密码
	 */
	@RDSAnnField(column = RDSAnnField.TEXT_PWD)
	public String pwd;
	/**
	 * 权限
	 */
	@RDSAnnField(column = RDSAnnField.BYTE)
	public Byte authority;
	/**
	 * 状态
	 */
	@RDSAnnField(column = RDSAnnField.BYTE)
	public Byte status;
	/**
	 * 创建时间
	 */
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date createTime;
	/**
	 * 更改时间
	 */
	@RDSAnnField(column = RDSAnnField.TIME)
	public Date updateTime;
	/**
	 * 扩展
	 */
	@RDSAnnField(column = RDSAnnField.SHORT_TEXT)
	public String ext;
	
	
	@AnnDicField(alias = "权限等级1")
	public static final Byte AUTHORITY_ONE = 1;
	@AnnDicField(alias = "权限等级2")
	public static final Byte AUTHORITY_TWO = 2;
	@AnnDicField(alias = "权限等级3")
	public static final Byte AUTHORITY_THREE = 3;
	
	@AnnDicField(alias = "正常")
	public static final Byte STATUS_NORMAL = 0;
	@AnnDicField(alias = "评论封禁")
	public static final Byte STATUS_CLOSE = 1;
	
	
	
}
