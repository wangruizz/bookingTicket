package ts;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ts.daoImpl.AirCompanyDAO;
import ts.daoImpl.AirportDAO;
import ts.daoImpl.FlightDAO;
import ts.daoImpl.HistoryDao;
import ts.model.Book;
import ts.util.DateProcess;

import javax.annotation.Resource;


/**
 * Created by wr on 2017/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Test {
    @Resource
    AirportDAO airportDAO;

    @Resource
    HistoryDao historyDAO;

    @Resource
    FlightDAO flightDAO;

    @Resource
    AirCompanyDAO airCompanyDAO;

    @org.junit.Test
    public void main() {
//        /**
//         * 机场
//         */
//        Airport airport = airportDAO.get("AAT");
//        System.out.println(airport.toString());
//        /**
//         * 航班
//         */
//        Flight flight  = flightDAO.get("2");
//        System.out.println(flight.toString());
//        /**
//         * 公司
//         */
//        Company company = airCompanyDAO.get("name");
//        System.out.println(company.toString());
//
//







        // Test t = new Test();
//        List<Airport> list = airportDAO.getAll();
//        System.out.println(list);

//        System.out.println("************是否存在***************");
//        System.out.println(airCompanyDAO.checkHasExist("name"));
//        System.out.println(airCompanyDAO.checkHasExist("d"));
//
//        System.out.println("*************register success****************");
//        Company company = new Company();
//        company.setName("公司3");
//        company.setPhone("15632825785");
//        company.setPwd("123");
//        company.setUsername("ww");
//        System.out.println(airCompanyDAO.register(company).toString());
//        System.out.println("************airCompanyDAO*************");
//        System.out.println("************airCompanyDAO getall*************");
//        System.out.println(flightDAO.getAll().toString());
//        System.out.println("************airCompanyDAO cancel*************");
//        System.out.println(flightDAO.cancel("1"));
//        System.out.println("************airCompanyDAO resume*************");
//        System.out.println(flightDAO.resumeFlight("1"));
//        System.out.println("************airCompanyDAO check*************");
//        System.out.println(flightDAO.CheckHasExist("1"));
//        System.out.println(flightDAO.CheckHasExist("2"));

//        flight.setId("12");
//        System.out.println(flightDAO.add("name",flight));
//        System.out.println(flightDAO.query("王思懿"));
//        flightDAO.resumeCompany("王思懿");
//        historyDAO.add("2", DateProcess.NowSqlDate());
//        historyDAO.delay("2",DateProcess.NowSqlDate(),java.sql.Time.valueOf("02:44:53"));
//        System.out.println(historyDAO.queryHistory("2"));
//        System.out.println(historyDAO.queryHistory("2",DateProcess.NowSqlDate()));
//        System.out.println(historyDAO.queryID("2",DateProcess.NowSqlDate()));
        /*!!!!!!!!!测试到这里出现问题*/
        System.out.println("商务做剩余"+historyDAO.queryRemain("2",DateProcess.NowSqlDate(), Book.SEAT_TYPE.BUSINESS_SEAT));
        System.out.println("经济做剩余"+historyDAO.queryRemain("2"+"2",DateProcess.NowSqlDate(), Book.SEAT_TYPE.ECONOMY_SEAT));
        System.out.println("************airCompanyDAO*************");
        System.out.println("************airCompanyDAO*************");
        System.out.println("************airCompanyDAO*************");
        System.out.println("************airCompanyDAO*************");
        System.out.println("************airCompanyDAO*************");

    }
}
