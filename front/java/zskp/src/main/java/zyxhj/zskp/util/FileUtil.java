package zyxhj.zskp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

import org.apache.commons.lang3.StringUtils;

public class FileUtil {

    /**
     * 按行读取文件
     */
    public static StringBuffer ReadFileByLine(String filename) {
        File file = new File(filename);
        InputStream is = null;
        Reader reader = null;
        StringBuffer sb = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            is = new FileInputStream(file);
            reader = new InputStreamReader(is);
            bufferedReader = new BufferedReader(reader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
            	sb.append(line+"\t\t"+",");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedReader)
                    bufferedReader.close();
                if (null != reader)
                    reader.close();
                if (null != is)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb;
    }

    /**
     * 按字节读取文件
     * 
     * @param filename
     */
    public static void ReadFileByBytes(String filename) {
        File file = new File(filename);
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            int index = 0;
            while (-1 != (index = is.read())) {
                System.out.write(index);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("-----------------------------------");
        try {
            is = new FileInputStream(file);
            byte[] tempbyte = new byte[1000];
            int index = 0;
            while (-1 != (index = is.read(tempbyte))) {
                System.out.write(tempbyte, 0, index);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 按字符读取文件
     * 
     * @param filename
     */
    public static void ReadFileByChar(String filename) {
        File file = new File(filename);
        InputStream is = null;
        Reader isr = null;
        try {
            is = new FileInputStream(file);
            isr = new InputStreamReader(is);
            int index = 0;
            while (-1 != (index = isr.read())) {
                System.out.print((char) index);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
                if (null != isr)
                    isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过OutputStreamWriter写文件
     * 
     * @param filename
     */
    public static void Write2FileByOutputStream(String filename) {
        File file = new File(filename);
        FileOutputStream fos = null;
        // BufferedOutputStream bos = null;
        OutputStreamWriter osw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);
            osw.write("Write2FileByOutputStream");
            // bos = new BufferedOutputStream(fos);
            // bos.write("Write2FileByOutputStream".getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != osw) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 通过BufferedWriter写文件
     * 
     * @param filename
     */
    public static void Write2FileByBuffered(String filename,String str) {
        File file = new File(filename);
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);
            bw.write("\r\n"+str);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bw) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != osw) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 清空已有的文件内容，以便下次重新写入新的内容
    public static void clearInfoForFile(String fileName) {
        File file =new File(fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void fileChaseFW(String filePath, String content) {
        try {
            //构造函数中的第二个参数true表示以追加形式写文件
            FileWriter fw = new FileWriter(filePath,true);
            fw.write("\r\n"+content);
            fw.close();
        } catch (IOException e) {
            System.out.println("文件写入失败！" + e);
        }
    }
    public static void fileChaseFW2(String filePath, String content) {
        try {
            //构造函数中的第二个参数true表示以追加形式写文件
            FileWriter fw = new FileWriter(filePath,false);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            System.out.println("文件写入失败！" + e);
        }
    }
    
}
