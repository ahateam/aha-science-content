package zyxhj.zskp.repository;

import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.Appraise;

public class AppraiseRepository extends RDSRepository<Appraise>{

	public AppraiseRepository() {
		super(Appraise.class);
	}
	
}
