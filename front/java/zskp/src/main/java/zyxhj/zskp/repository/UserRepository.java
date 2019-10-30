package zyxhj.zskp.repository;

import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.ZskpUser;

public class UserRepository extends RDSRepository<ZskpUser>{
	public  UserRepository() {
		super(ZskpUser.class);
	}
}
