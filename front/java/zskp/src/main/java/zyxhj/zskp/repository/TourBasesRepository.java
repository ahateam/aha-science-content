package zyxhj.zskp.repository;

import zyxhj.utils.data.ts.TSRepository;
import zyxhj.zskp.domain.TourBases;

public class TourBasesRepository extends TSRepository<TourBases>{
	public   TourBasesRepository() {
		super(TourBases.class);
	}
}
