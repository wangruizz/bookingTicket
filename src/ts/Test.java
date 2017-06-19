package ts;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ts.daoImpl.AirCompanyDAO;
import ts.daoImpl.AirportDAO;
import ts.model.Airport;
import ts.model.Company;
import ts.model.History;
import ts.serviceException.PassengerNotExistException;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wr on 2017/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Test {
    @Resource
    AirportDAO airportDAO;
//    @Resource
//    History historyDAO;

    @Resource
    AirCompanyDAO airCompanyDAO;

    @org.junit.Test
    public void main() throws PassengerNotExistException {
        // Test t = new Test();

        List<Airport> list = airportDAO.getAll();
//        System.out.println(list);

//        Company company = airCompanyDAO.login("王思懿", "12");
//        System.out.println(airCompanyDAO.checkHasExist("name").toString());
//        System.out.println(airCompanyDAO.checkHasExist("ne").toString());

//        System.out.println(airCompanyDAO.register());
    // throw new PassengerNotExistException();

    }
}
