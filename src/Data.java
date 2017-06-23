import org.hibernate.criterion.Restrictions;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ts.daoImpl.AirCompanyDAO;
import ts.daoImpl.AirportDAO;
import ts.daoImpl.FlightDAO;
import ts.model.Airport;
import ts.model.Company;
import ts.model.Flight;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Time;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Data {
    // 简体中文的编码范围从B0A1（45217）一直到F7FE（63486）
    private int BEGIN = 45217;
    private int END = 63486;

    // 按照声 母表示，这个表是在GB2312中的出现的第一个汉字，也就是说“啊”是代表首字母a的第一个汉字。
    // i, u, v都不做声母, 自定规则跟随前面的字母
    private char[] chartable = {'啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈', '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然', '撒', '塌', '塌', '塌', '挖', '昔', '压', '匝',};

    // 二十六个字母区间对应二十七个端点
    // GB2312码汉字区间十进制表示
    private int[] table = new int[27];

    // 对应首字母区间表
    private char[] initialtable = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 't', 't', 'w', 'x', 'y', 'z',};

    // ------------------------public方法区------------------------
    // 根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串 最重要的一个方法，思路如下：一个个字符读入、判断、输出

    public String cn2py(String SourceStr) {
        String Result = "";
        int StrLength = SourceStr.length();
        int i;
        try {
            for (i = 0; i < StrLength; i++) {
                Result += Char2Initial(SourceStr.charAt(i));
            }
        } catch (Exception e) {
            Result = "";
            e.printStackTrace();
        }
        return Result;
    }

    // ------------------------private方法区------------------------

    /**
     * 输入字符,得到他的声母,英文字母返回对应的大写字母,其他非简体汉字返回 '0' 　　*
     */
    private char Char2Initial(char ch) {
        // 对英文字母的处理：小写字母转换为大写，大写的直接返回
        if (ch >= 'a' && ch <= 'z') {
            return (char) (ch - 'a' + 'A');
        }
        if (ch >= 'A' && ch <= 'Z') {
            return ch;
        }
        // 对非英文字母的处理：转化为首字母，然后判断是否在码表范围内，
        // 若不是，则直接返回。
        // 若是，则在码表内的进行判断。
        int gb = gbValue(ch);// 汉字转换首字母
        if ((gb < BEGIN) || (gb > END))// 在码表区间之前，直接返回
        {
            return ch;
        }
        int i;
        for (i = 0; i < 26; i++) {// 判断匹配码表区间，匹配到就break,判断区间形如“[,)”
            if ((gb >= table[i]) && (gb < table[i + 1])) {
                break;
            }
        }
        if (gb == END) {// 补上GB2312区间最右端
            i = 25;
        }
        return initialtable[i]; // 在码表区间中，返回首字母
    }

    /**
     * 取出汉字的编码 cn 汉字
     */
    private int gbValue(char ch) {// 将一个汉字（GB2312）转换为十进制表示。
        String str = new String();
        str += ch;
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < 2) {
                return 0;
            }
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        } catch (Exception e) {
            return 0;
        }
    }

    @Resource
    private FlightDAO flightDAO;

    @Resource
    private AirCompanyDAO airCompanyDAO;

    @Resource
    private AirportDAO airportDAO;

    @org.junit.Test
    public void test() throws IOException {
        for (int i = 0; i < 26; i++) {
            table[i] = gbValue(chartable[i]);// 得到GB2312码的首字母区间端点表，十进制。
        }
        table[26] = END;// 区间表结尾

        String url = "http://ws.webxml.com.cn/webservices/DomesticAirline.asmx/getDomesticAirlinesTime";
        List<Airport> airports = airportDAO.getAll();
        for (Airport airport : airports) {
            for (Airport airport1 : airports) {
                if (!airport.getName().equals(airport1.getName())) {
                    System.out.println("form " + airport.getName() + "   to  " + airport1.getName());
                    Connection connection = Jsoup.connect(url);
                    connection.data("startCity", airport.getName());
                    connection.data("lastCity", airport1.getName());
                    connection.data("theDate", "");
                    connection.data("userID", "");
                    Document document = null;
                    try {
                        document = connection.post();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Element body = document.body();
                    Elements elements = body.getElementsByTag("airlinestime");
                    elements.forEach(element -> {
                        try {
                            String companyName = element.getElementsByTag("Company").text();
                            if (!companyName.equals("没有航班")) {
                                List<Company> list = airCompanyDAO.findBy("username", true, Restrictions.eq("name", companyName));
                                Company company;
                                if (list.size() != 0) {
                                    company = list.get(0);
                                } else {
                                    company = new Company();
                                    company.setName(companyName);
                                    company.setUsername(cn2py(companyName));
                                    company.setPwd("1");
                                    company.setPhone("123456");
                                    airCompanyDAO.save(company);
                                }
                                String flightId = element.getElementsByTag("AirlineCode").text();
                                String StartTime = element.getElementsByTag("StartTime").text();
                                String ArriveTime = element.getElementsByTag("ArriveTime").text();
                                Flight flight = new Flight();
                                flight = flightDAO.get(flightId);
                                if (flight == null) {
                                    flight = new Flight();
                                    flight.setId(flightId);
                                    flight.setStartTime(str2time(StartTime));
                                    flight.setArriveTime(str2time(ArriveTime));
                                    flight.setStartAirport(airport);
                                    flight.setArriveAirport(airport1);
                                    flight.setBusinessPrice(1200.0);
                                    flight.setEconomyPrice(500.0);
                                    flight.setBusinessNum(20);
                                    flight.setEconomyNum(20);
                                    flight.setCompany(company);
                                    flight.setStatus(Flight.STATUS.FLIGHT_NORMAL);
                                    System.out.println(flight.toString());
                                    flightDAO.save(flight);
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("\n---------------------");
                            e.printStackTrace();
                            System.err.println("---------------------\n");
                        }
                    });
                }
            }
        }

    }

    private Time str2time(String v) {
        String[] s = v.split(":");
        return new Time(Integer.valueOf(s[0]), Integer.valueOf(s[1]), 0);
    }

    public static void main(String[] args) {
        String url = "http://ws.webxml.com.cn/webservices/DomesticAirline.asmx/getDomesticAirlinesTime";
        Connection connection = Jsoup.connect(url);
        connection.data("startCity", "郑州");
        connection.data("lastCity", "北京");
        connection.data("theDate", "");
        connection.data("userID", "");
        Document document = null;
        try {
            document = connection.post();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element body = document.body();
        Elements elements = body.getElementsByTag("airlinestime");
        elements.forEach(element -> {
            System.out.println("--------------------------");
            System.out.println("company  " + element.getElementsByTag("Company").text());
            System.out.println("AirlineCode  " + element.getElementsByTag("AirlineCode").text());
        });
    }
}
