package ts;

import org.hibernate.criterion.Restrictions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ts.daoImpl.*;
import ts.model.Agency;
import ts.model.Book;
import ts.model.History;
import ts.model.Passenger;
import ts.serviceException.TicketPayException;
import ts.util.DateProcess;

import javax.annotation.Resource;
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

    @org.junit.Test
    public void main() throws TicketPayException {
       // Passenger list2 = passengerDAO.queryByIDCard("1234",13);
    //    Passenger passenger = list2.get(0);
      //  System.out.println("22222"+agencyDAO.login("1234","11"));
        Book list = bookDAO.pay(1);
        System.out.println("111111111111"+bookDAO.complete(list));
//           List<Agency> agency = agencyDAO.findBy("id",13,"id",true);
//        Agency agency1 = agency.get(0);
//        agency1.setName("王芮");
//        agencyDAO.modify(agency1);
//        bookDAO.printTicket(1,"1234");
    }
}
