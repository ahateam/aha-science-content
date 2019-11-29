package zyxhj.zskp.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

import com.alibaba.druid.pool.DruidPooledConnection;

import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.Appraise;

public class AppraiseRepository extends RDSRepository<Appraise>{

	public AppraiseRepository() {
		super(Appraise.class);
	}
	public Long getAppraiseCount(DruidPooledConnection conn, Long ownerId) throws Exception {
		  String sql = "select count(1) from tb_zskp_appraise where owner_id=?";
		  PreparedStatement ps = AppraiseRepository.prepareStatement(conn, sql, Arrays.asList(ownerId));
		  ResultSet rs = ps.executeQuery();
		  if(rs.next()) {
		   return (long) rs.getInt(1);
		  }
		  return 0L;
		 }
}
