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
    public void main() {
       // List<Passenger> list2 = passengerDAO.queryByID(13);
    //    Passenger passenger = list2.get(0);
        List<Book> list = bookDAO.query(13,1);
        System.out.println(list==null?0:list);
        List<Agency> agency = agencyDAO.findBy("id",13,"id",true);
        Agency agency1 = agency.get(0);
        agency1.setName("王芮");
        agencyDAO.modify(agency1);
    }
}
