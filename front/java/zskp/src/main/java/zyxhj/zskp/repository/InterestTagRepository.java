package zyxhj.zskp.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONArray;

import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.InterestTag;
public class InterestTagRepository extends RDSRepository<InterestTag>{
	private DruidDataSource ds;
	public  InterestTagRepository() {
		super(InterestTag.class);
		try {
			ds = DataSource.getDruidDataSource("rdsDefault.prop");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void addInteresttagPageview(Long id,String keyword) throws SQLException, ServerException {
		try (DruidPooledConnection conn = ds.getConnection()) {
//			String sql = "UPDATE `tb_zskt_ interesttag` set `page_view` = `page_view` +1 WHERE  `user_id` = "+id;
			this.update(conn, StringUtils.join("set page_view = page_view +1 "),
					null, "user_id = ? and keyword = ?", Arrays.asList(id,keyword));
//			this.executeUpdateSQL(conn, sql, null);			
		}
	}
	public JSONArray getAllInterestTag(Integer count,Integer offset) throws SQLException, ServerException {
		try (DruidPooledConnection conn = ds.getConnection()) {
			String sql = "SELECT inter.*,user.`name`,user.head FROM tb_zskt_interesttag inter LEFT JOIN tb_zskt_user user on inter.user_id = user.id";
			List<Object> params = new ArrayList<Object>();
			return this.sqlGetJSONArray(conn, sql, params, count, offset);
		}
	}
}
