package zyxhj.zskp.repository;

import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.InterestTag;
public class InterestTagRepository extends RDSRepository<InterestTag>{
	public  InterestTagRepository() {
		super(InterestTag.class);
	}

}
