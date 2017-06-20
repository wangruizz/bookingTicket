package ts.util;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by 12556 on 2017/6/17.
 */
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
}
