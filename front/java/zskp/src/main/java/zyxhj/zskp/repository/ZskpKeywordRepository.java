package zyxhj.zskp.repository;

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
}
