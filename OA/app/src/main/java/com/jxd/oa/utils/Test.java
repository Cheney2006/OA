package com.jxd.oa.utils;

import com.yftools.util.DateUtil;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * *****************************************
 * Description ：
 * Created by cy on 2014/9/11.
 * *****************************************
 */
public class Test {
    public static void main(String[] args) {
        Date now=new Date();
        System.out.println(now.getTime());;
        String dateStr="2013-08-22 18:54:13.980";
        Date date=DateUtil.stringToDate("yyyy-MM-dd HH:mm:ss.sss",dateStr);
        System.out.println(date.getTime());
        testRegex();
    }

    public static void testRegex(){
        Pattern pattern=Pattern.compile("23");
        Matcher matcher=pattern.matcher("1233232123");
        int i=0;
        while(matcher.find()){
            i++;
        }
        System.out.println(i);
    }

}
