package zyxhj.zskp.repository;

import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.Enroll;

public class EnrollRepository extends RDSRepository<Enroll>{
	public  EnrollRepository() {
		super(Enroll.class);
	}
}
