package zyxhj.zskp.repository;

import zyxhj.utils.data.rds.RDSRepository;
import zyxhj.zskp.domain.UserFavorites;

public class UserFavoritesRepository extends RDSRepository<UserFavorites>{
	public  UserFavoritesRepository() {
		super(UserFavorites.class);
	}

}
