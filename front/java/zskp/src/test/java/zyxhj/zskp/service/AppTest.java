package zyxhj.zskp.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import zyxhj.utils.api.ServerException;
import zyxhj.utils.data.DataSource;

public class AppTest {
	private static DruidPooledConnection conn;
	private static ZskpUserService userService;
	private static ZskpOtherContent contentService;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			conn = DataSource.getDruidDataSource("rdsDefault.prop").getConnection();
			userService = new ZskpUserService(null);
			contentService = new ZskpOtherContent(null);
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
		
		
		try {
			System.out.println(contentService.getVersion());
		} catch (ServerException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			
			JSONArray json = contentService.getReplyListByUser("1180", null, 402490032471742L, null, true, null, 7, 10, 0);
	        System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
