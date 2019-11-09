package xhj.cn.start;


import com.alibaba.druid.pool.DruidDataSource;
import com.alicloud.openservices.tablestore.SyncClient;

import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.rds.RDSUtils;
import zyxhj.utils.data.ts.TSUtils;
import zyxhj.zskp.domain.AdvertInfo;
import zyxhj.zskp.domain.ApplyAuthority;
import zyxhj.zskp.domain.Enroll;
import zyxhj.zskp.domain.InterestTag;
import zyxhj.zskp.domain.TourBase;
import zyxhj.zskp.domain.TourBases;
import zyxhj.zskp.domain.UserFavorites;
import zyxhj.zskp.domain.ZskpKeyword;
import zyxhj.zskp.domain.ZskpUser;

public class Test {

	public static void main(String[] args) {

		testDB();

	}

	private static void testDB() {
		System.out.println("testDB");

		try {
			DruidDataSource dds = DataSource.getDruidDataSource("rdsDefault.prop");
			SyncClient client = DataSource.getTableStoreSyncClient("tsDefault.prop");
//			RDSUtils.dropTableByEntity(dds, InterestTag.class);
//			RDSUtils.createTableByEntity(dds, AdvertInfo.class);
//			RDSUtils.createTableByEntity(dds, ApplyAuthority.class);
//			RDSUtils.createTableByEntity(dds, Enroll.class);
//			RDSUtils.createTableByEntity(dds, InterestTag.class);
//			RDSUtils.createTableByEntity(dds, TourBase.class);
//			RDSUtils.createTableByEntity(dds, UserFavorites.class);
//			RDSUtils.createTableByEntity(dds, InterestTag.class);


//			TSUtils.drapTableByEntity(client,TourBases.class);
			TSUtils.createTableByEntity(client, TourBases.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 
}
