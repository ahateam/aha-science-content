package zyxhj.zskp.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zyxhj.utils.Singleton;
import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.DataSource;
import zyxhj.zskp.util.SensitiveWordInit;

public class AppTest {
	private static DruidPooledConnection conn;
	private static ZskpUserService userService;
	private static ZskpOtherContent contentService;
	private static SensitiveWordInit sensitiveWordInit;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			conn = DataSource.getDruidDataSource("rdsDefault.prop").getConnection();
			userService = new ZskpUserService(null);
			contentService = new ZskpOtherContent(null);
			sensitiveWordInit = Singleton.ins(SensitiveWordInit.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		conn.close();
	}
	
	
	@Test
	public void login() {
		
		
	
	}
	@Test
	public void searchContentAndUser() {
		try {
			System.out.println(userService.bindingPhone("1180", 402343714608964L, "12345678901"));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	@Test
	public void test1() {
		try {
			
			Date d=new Date();
			GregorianCalendar gc =new GregorianCalendar();
			SimpleDateFormat sf  =new SimpleDateFormat("yyyy-MM-dd");
			gc.setTime(d);
			gc.add(5,-90);
			gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
			System.out.println(sf.format(gc.getTime()));
			
			System.out.println(System.currentTimeMillis());
	        System.out.println(gc.getTimeInMillis());
	        System.out.println(new Date().getTime());
//	        System.out.println(contentService.getTimeInMillis(7));
	        
		
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	@Test
	public void test2() {
		try {
			
			String reg = "^.*\\d{4}.*$";
			String s = "aa2aa11ab3333aa11";
			if(s.matches(reg)){
			    System.out.println(s.replaceAll("^.*\\\\d{4}.*$","****"));
			}
			
			
//			Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//			Matcher m = p.matcher("hfjkhjk18396979880");
//			System.out.println(m.matches()+"---");
//			　　return m.matches();
			
//			sensitiveWordInit.readSensitiveWordFile((byte)4);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
