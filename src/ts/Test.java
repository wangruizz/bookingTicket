package ts;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ts.daoImpl.AirCompanyDAO;
import ts.daoImpl.AirportDAO;
import ts.daoImpl.PassengerDAO;
import ts.model.*;
import ts.serviceException.PassengerNotExistException;
import ts.serviceException.RegisterException;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by wr on 2017/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Test {
    @Resource
    AirportDAO airportDAO;
    @Resource
    PassengerDAO passengerDAO;

    @Resource
    AirCompanyDAO airCompanyDAO;

    @org.junit.Test
    public void main() throws PassengerNotExistException, RegisterException {

        List<Airport> list = airportDAO.getAll();
//        throw new PassengerNotExistException();
     Company company = new Company();
     company.setName("哈哈哈");
     company.setPhone("123456");
     company.setPwd("11");
     company.setUsername("哈哈哈");
     airCompanyDAO.save(company);
     System.out.println(new Message(Message.CODE.AGENCY_MOTIFY_FAILED));
     throw new RegisterException();
    }
}
