package zyxhj.zskp.repository;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.Enroll;

public class EnrollRepository extends RDSRepository<Enroll>{
	public  EnrollRepository() {
		super(Enroll.class);
	}

	public JSONObject getCount(DruidPooledConnection conn, Long contenId) throws ServerException {
		StringBuffer sql = new StringBuffer("SELECT count(1) num FROM tb_zskt_enroll WHERE conten_id = ").append(contenId);
		List<Object> params = new ArrayList<Object>();
		return this.sqlGetJSONObject(conn, sql.toString(), params);
	}
	public JSONObject getNumberCount(DruidPooledConnection conn, Long contenId) throws ServerException {
		StringBuffer sql = new StringBuffer("SELECT sum(number) userNum FROM tb_zskt_enroll WHERE conten_id = ").append(contenId);
		List<Object> params = new ArrayList<Object>();
		return this.sqlGetJSONObject(conn, sql.toString(), params);
	}
	
}
