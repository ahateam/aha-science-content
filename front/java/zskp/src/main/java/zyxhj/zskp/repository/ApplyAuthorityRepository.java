package zyxhj.zskp.repository;

import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.ApplyAuthority;

public class ApplyAuthorityRepository extends RDSRepository<ApplyAuthority>{
	public  ApplyAuthorityRepository() {
		super(ApplyAuthority.class);
	}

}
