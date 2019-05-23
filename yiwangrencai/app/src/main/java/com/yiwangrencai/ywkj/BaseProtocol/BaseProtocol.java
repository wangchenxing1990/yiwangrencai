package com.yiwangrencai.ywkj.BaseProtocol;
import com.yiwangrencai.ywkj.tools.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Administrator on 2017/4/8.
 */

public class BaseProtocol {

    /**
     * 获取本地缓存
     */
    public static String readLocal(){

        File dir=FileUtils.getCacheDir();// 获取缓存所在的文件夹
        File file = new File(dir, "hom_1");
        System.out.print("=============000000000000"+file);
        try {
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr);

            String str=null;
            StringWriter sw=new StringWriter();
            while((str=br.readLine())!=null){
                sw.write(str);
            }

            return sw.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 保存缓存
     */

        public static void saveLocal(String json) {
                BufferedWriter bw = null;
                try {
                    File dir= FileUtils.getCacheDir();
                    //在第一行写一个过期时间
                    System.out.println("+++++++++><><>>"+dir);
                    File file = new File(dir, "hom_1"); // /storage/emulated/0/yiwangkeji/cache/home_0
                    System.out.println("file的根路径"+file);

                   // System.out.println("+++++++++><><>>"+file);
                    FileWriter fw = new FileWriter(file);
                    System.out.println("fw1111111111"+fw);

                    bw = new BufferedWriter(fw);

                    System.out.println("bw1111111111"+bw);
                    bw.write(json);// 把整个json文件保存起来
                    bw.flush();
                    bw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{

                }

        }
}
