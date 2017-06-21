package ts;

import org.hibernate.criterion.Restrictions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ts.daoImpl.*;
import ts.model.*;
import ts.serviceException.TicketPayException;
import ts.util.DateProcess;

import javax.annotation.Resource;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by wr on 2017/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Test {
    @Resource
    PassengerDAO passengerDAO;

    @Resource
    BookDAO bookDAO;

    @Resource
    FlightDAO flightDAO;

    @Resource
    AirCompanyDAO airCompanyDAO;
    @Resource
    AgencyDAO agencyDAO;
    @Resource
    AirportDAO airportDAO;
    @Resource
    HistoryDao historyDao;

    @org.junit.Test
    public void main() throws TicketPayException {
        List<Flight> list = flightDAO.query("王芮");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date time = sdf.parse("2017-06-21");
         //   History history = historyDao.add("2",new java.sql.Date(time.getTime()));
            System.out.println("19999"+historyDao.delay("2",new java.sql.Date(time.getTime()),Time.valueOf("00:10:00")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
