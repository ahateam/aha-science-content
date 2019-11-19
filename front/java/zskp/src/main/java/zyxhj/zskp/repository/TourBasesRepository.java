package zyxhj.zskp.repository;

import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.TourBases;

public class TourBasesRepository extends RDSRepository<TourBases>{
	public   TourBasesRepository() {
		super(TourBases.class);
	}
}
