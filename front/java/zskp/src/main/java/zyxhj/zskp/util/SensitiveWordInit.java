package zyxhj.zskp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import zyxhj.utils.IDUtils;
import zyxhj.utils.Singleton;
import zyxhj.utils.data.DataSource;
import zyxhj.utils.data.EXP;
import zyxhj.zskp.domain.SensitiveWord;
import zyxhj.zskp.repository.SensitiveWordRepository;

/**
 * @Description: 初始化敏感词库，将敏感词加入到HashMap中，构建DFA算法模型
 * @Project：test
 * @Author : zhangyuntao
 * @Date ： 2018年4月20日 下午2:27:06
 * @version 1.0
 */
public class SensitiveWordInit {
    private String ENCODING = "UTF-8";    //字符编码
    @SuppressWarnings("rawtypes")
    public HashMap sensitiveWordMap;
    private DruidDataSource ds;
    private SensitiveWordRepository sensitiveWordRepository;
    public SensitiveWordInit(){
        super();
        try {
			ds = DataSource.getDruidDataSource("rdsDefault.prop");
			sensitiveWordRepository = Singleton.ins(SensitiveWordRepository.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

 
    @SuppressWarnings("rawtypes")
    public Map initKeyWord(){
        try {
            //读取敏感词库
            Set<String> keyWordSet = readSensitiveWordFile((byte)0);
            //将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
            //spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }
    @SuppressWarnings("rawtypes")
    public Map initKeyWordByExamine(){
        try {
            //读取敏感词库
            Set<String> keyWordSet = readSensitiveWordFile((byte)1);
            //将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
            //spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
     * 中 = {
     *      isEnd = 0
     *      国 = {<br>
     *           isEnd = 1
     *           人 = {isEnd = 0
     *                民 = {isEnd = 1}
     *                }
     *           男  = {
     *                 isEnd = 0
     *                  人 = {
     *                       isEnd = 1
     *                      }
     *              }
     *           }
     *      }
     *  五 = {
     *      isEnd = 0
     *      星 = {
     *          isEnd = 0
     *          红 = {
     *              isEnd = 0
     *              旗 = {
     *                   isEnd = 1
     *                  }
     *              }
     *          }
     *      }
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while(iterator.hasNext()){
            key = iterator.next();    //关键字
            nowMap = sensitiveWordMap;
            for(int i = 0 ; i < key.length() ; i++){
                char keyChar = key.charAt(i);       //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取

                if(wordMap != null){        //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                }
                else{     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String,String>();
                    newWorMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if(i == key.length() - 1){
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
    }

//    public static void main(String[] args) {
//        Set set = new HashSet();
//        set.add("中国");
//        set.add("中国人民");
//        set.add("中国人");
//        new SensitiveWordInit().addSensitiveWordToHashMap(set);
//    }

    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     */
//    @SuppressWarnings("resource")
//    public Set<String> readSensitiveWordFile() throws Exception{
//        Set<String> set = null;
//		String ciname = StringUtils.join("configs/", "filterText.txt");
//        File file = new File(ciname);//读取文件
//        InputStreamReader read = new InputStreamReader(new FileInputStream(file),ENCODING);
//        try {
//            if(file.isFile() && file.exists()){//文件流是否存在
//                set = new HashSet<String>();
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String txt = null;
//                while((txt = bufferedReader.readLine()) != null){//读取文件，将文件内容放入到set中
//                    set.add(txt);
//                }
//            }
//            else{
//                throw new Exception("敏感词库文件不存在");
//            }
//        } catch (Exception e) {
//            throw e;
//        }finally{
//            read.close();//关闭文件流
//        }
//        return set;
//    }
//    public  Set<String>  readSensitiveWordFile(byte se) throws Exception{
//        List<SensitiveWord> set = null;
//		String ciname = StringUtils.join("configs/", "直接过滤敏感词.txt");
//        File file = new File(ciname);//读取文件
//        InputStreamReader read = new InputStreamReader(new FileInputStream(file),ENCODING);
//        try(DruidPooledConnection conn = ds.getConnection()) {
//            if(file.isFile() && file.exists()){//文件流是否存在
//                set = new ArrayList<SensitiveWord>();
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String txt = null;
//                Long num=91L;
//                while((txt = bufferedReader.readLine()) != null){//读取文件，将文件内容放入到set中
//                	SensitiveWord s = new SensitiveWord();
//                	num ++;
//                	s.wordId = num;
//                	s.badword = txt;
//                	s.type = 0;
//                    set.add(s);
//                }
//            }
//            else{
//                throw new Exception("敏感词库文件不存在");
//            }
//            sensitiveWordRepository.insertList(conn, set);
//        } catch (Exception e) {
//            throw e;
//        }finally{
//            read.close();//关闭文件流
//        }
//        return null;
//    }
    
    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     */
    public Set<String> readSensitiveWordFile(Byte type) throws Exception{
        Set<String> set = null;
        try(DruidPooledConnection conn = ds.getConnection()) {
        	List<SensitiveWord> list =  sensitiveWordRepository.getList(conn, EXP.INS().key("type", type),null,null);
            if(list != null && list.size()>0){//是否为空
                set = new HashSet<String>();
                for(SensitiveWord se:list)
                    set.add(se.badword);
            }
            else{
                throw new Exception("敏感词库为空");
            }
        } catch (Exception e) {
            throw e;
        }
        return set;
    }
}