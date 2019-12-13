package zyxhj.zskp.repository;

import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.SensitiveWord;

public class SensitiveWordRepository extends RDSRepository<SensitiveWord>{

	public SensitiveWordRepository() {
		super(SensitiveWord.class);
	}
}
