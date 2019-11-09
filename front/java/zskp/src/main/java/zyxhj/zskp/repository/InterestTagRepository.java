package zyxhj.zskp.repository;

import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

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
	public void addKeyword(Long id) throws SQLException, ServerException {
		try (DruidPooledConnection conn = ds.getConnection()) {
			String sql = "UPDATE `tb_zskt_interesttag` set `page_view` = `page_view` +1 WHERE  `user_id`  = "+id;
			this.executeUpdateSQL(conn, sql, null);			
		}
	}
}
