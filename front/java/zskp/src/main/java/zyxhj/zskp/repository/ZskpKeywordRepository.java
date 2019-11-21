package zyxhj.zskp.repository;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.pool.DruidPooledConnection;

import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.ZskpKeyword;

public class ZskpKeywordRepository extends RDSRepository<ZskpKeyword>{
	
	public ZskpKeywordRepository() {
		super(ZskpKeyword.class);
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void addKeywordPageview(DruidPooledConnection conn ,String key) throws ServerException {
		String sql  = "update tb_zskt_Keyword SET page_view = page_view+1 where keyword = ?";
		List<Object> list = new ArrayList<Object>();
		list.add(key);
		this.executeUpdateSQL(conn, sql,list);
	}
}
