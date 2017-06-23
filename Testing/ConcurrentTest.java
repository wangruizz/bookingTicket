import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 并发测试
 */
public class ConcurrentTest {
    static int p = 13;
    public static void main(String[] args) {
        String s = "2017-06-23 15:38:30"; //发起请求的时间
        int cnt = 20; //并发线程数
        for (int i = 0; i < cnt; i++) {
            new Thread(() -> {
                int id = p++;
                System.out.println("线程" + id + "启动");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date start = null;
                try {
                    start = sdf.parse(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Connection connection = Jsoup.connect("http://127.0.0.1:8080/CXF/REST/Agency/bookingTicket/14/913/"+ id +"/0").timeout(3000);
                connection.header("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTgxOTUwMDAsInN1YiI6IntcIm5hbWVcIjpcIuWwj-m4oeeCluiYkeiPhzHlj7dcIixcInVzZXJJZFwiOlwiMTc4Mzk5MjM0NTRcIn0iLCJpc3MiOiJaWlUiLCJhdWQiOiIxNCIsImV4cCI6MTQ5ODI4MTQwMCwibmJmIjoxNDk4MTk1MDAwfQ.8bQWwdY0m8uob62vNhrVBOGTKGkeFuw7or3sJhzW_xw");
                connection.header("Accept", "application/json");
                connection.header("Host", "127.0.0.1:8080");
                connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.109 Safari/537.36");
                Date now = new Date();
                try {
                    Thread.sleep(start.getTime() - now.getTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程" + id + "开始发起请求");
                try {
                    Document document = connection.get();
                    System.out.println("线程"+id+"\n\n" + document.body() + "\n\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
