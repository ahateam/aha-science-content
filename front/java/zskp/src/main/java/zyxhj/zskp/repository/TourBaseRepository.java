package zyxhj.zskp.repository;

import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.TourBase;

public class TourBaseRepository extends RDSRepository<TourBase>{
	public  TourBaseRepository() {
		super(TourBase.class);
	}

}
