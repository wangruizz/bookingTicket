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

    @org.junit.Test
    public void main() throws TicketPayException {
       // Passenger list2 = passengerDAO.queryByIDCard("1234",13);
    //    Passenger passenger = list2.get(0);
      //  System.out.println("22222"+agencyDAO.login("1234","11"));
       // Book list = bookDAO.pay(1);
//        System.out.println("111111111111"+flightDAO.cancelFlight("1"));
      //     List<Flight> agency = flightDAO.query(new Date(),"AAT","AKA");
      //  System.out.println(agency.toString());
//        Agency agency1 = agency.get(0);
//        agency1.setName("王芮");
//        agencyDAO.modify(agency1);
//        bookDAO.printTicket(1,"1234");
        Flight flight = new Flight();
        flight.setStartTime(Time.valueOf("09:00:00"));
        flight.setArriveTime(Time.valueOf("22:00:00"));
        flight.setStartAirport(airportDAO.get("AAT"));
        flight.setArriveAirport(airportDAO.get("AKA"));
        flight.setBusinessNum(1200);
        flight.setEconomyNum(200);
        flight.setBusinessPrice(120000.00);
        flight.setEconomyPrice(122222.00);
        flight.setStatus(0);
        flightDAO.add("王芮",flight);
    }
}
