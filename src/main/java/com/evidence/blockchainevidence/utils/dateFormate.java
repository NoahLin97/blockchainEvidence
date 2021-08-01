package com.evidence.blockchainevidence.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class dateFormate {

    public static String getAddMonth(String cStartTime,String cValidMonth){
        String time=cStartTime.substring(0,10)+" "+cStartTime.substring(11,19);
        int year=Integer.parseInt(cStartTime.substring(0,4));
        int month=Integer.parseInt(cStartTime.substring(5,7));
        int month1=month+Integer.parseInt(cValidMonth);
        int monthr=0,yearR=0;
        if(month1<=12){
            monthr=month1;
            yearR=year;
        }else {
            monthr=month1 % 12;
            yearR=year+(month1 / 12);
        }
        String endT=Integer.toString(yearR)+"-"+Integer.toString(monthr)+time.substring(7);
        return endT;
    }
    /**
     38    * 获取现在时间
     39    *
     40    * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     41    */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
//          ParsePosition pos = new ParsePosition(0);
//          Date strtodate = formatter.parse(dateString, pos);
        return dateString;
    }
    /**
     49    *
     50    *
     51    * @return 返回日期格式yyyy-MM-dd HH:mm:ss
     52    */
    public static String getFormateDate(Date date) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static Date getDateByString(String str){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(str, pos);
        return strtodate;
    }

    public static Date getShortDateByString(String str){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(str, pos);
        return strtodate;
    }
    public static void main(String args[]){
        System.out.println(getStringDate());
    }
}
