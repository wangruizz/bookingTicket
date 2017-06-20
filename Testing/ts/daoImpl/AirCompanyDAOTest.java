package ts.daoImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ts.model.Company;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by 12556 on 2017/6/19.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AirCompanyDAOTest {
    @Resource
    AirCompanyDAO airCompanyDAO;


    public AirCompanyDAO getAirCompanyDAO() {
        return airCompanyDAO;
    }

    public void setAirCompanyDAO(AirCompanyDAO airCompanyDAO) {
        this.airCompanyDAO = airCompanyDAO;
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

//    @Test
//    public void register() throws Exception {
//        Company company = new Company();
//        company.setName("公司5");
//        company.setPhone("15632825785");
//        company.setPwd("20");
//        company.setUsername("120");
//        System.out.println(company.toString());
//        System.out.println(airCompanyDAO.register(company).toString());
//    }

    @Test
    public void login() throws Exception {
    }

    @Test
    public void checkHasExist() throws Exception {
    }

}