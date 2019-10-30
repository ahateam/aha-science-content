package zyxhj.zskp.repository;

import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.AdvertInfo;

public class AdvertInfoRepository extends RDSRepository<AdvertInfo>{
	public  AdvertInfoRepository() {
		super(AdvertInfo.class);
	}

}
