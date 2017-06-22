package ts.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateProcess {

    public static  Date NowSqlDate() {
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-ddE HH:mm:ss");
        System.out.println("时间格式转换类中获得当前日期："+sdf.format(date));
        Date sqlDate = new Date(date.getTime());
        return sqlDate;
    }

    public static java.util.Date sqlToUtil(Date sqlDate){
        return null;
    }

    public static Date utilToSql(Date utilDate){
        java.sql.Date sqlDate=new java.sql.Date(utilDate.getTime());
        return sqlDate;
    }

    public static java.util.Date getNext(java.util.Date now, int delta) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        delta *= 24 * 60 * 60 * 1000; //一天的毫秒数
        try {
            now = sdf.parse(sdf.format(now)); //去除时分秒
            return new java.util.Date(now.getTime() + delta);
        } catch (ParseException e) { }
        return null;
    }
}
