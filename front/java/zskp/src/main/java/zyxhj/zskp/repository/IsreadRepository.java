package zyxhj.zskp.repository;

import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.Isread;

public class IsreadRepository extends RDSRepository<Isread>{
	public  IsreadRepository() {
		super(Isread.class);
	}
}
