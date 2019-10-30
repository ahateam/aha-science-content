package zyxhj.zskp.domain;

import zyxhj.utils.data.AnnDicField;

public class Versions {

	@AnnDicField(alias = "版本号")
	public static final  int versions_number = 103;
	@AnnDicField(alias = "apk版本信息")
	public static final String versions_apk = "http://weapp.datanc.cn/science/app/"+versions_number+"/android/zskp.apk";
	@AnnDicField(alias = "ios版本信息")
	public static final String versions_ios = "http://weapp.datanc.cn/science/app/"+versions_number+"/ios/zskp.ipa";
}
