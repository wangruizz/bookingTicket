package ts.daoImpl;

import org.hibernate.annotations.Source;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ts.model.Agency;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by 12556 on 2017/6/19.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AgencyDAOTest {

    @Resource
    AgencyDAO agencyDAO;

    public AgencyDAO getAgencyDAO() {
        return agencyDAO;
    }

    public void setAgencyDAO(AgencyDAO agencyDAO) {
        this.agencyDAO = agencyDAO;
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void checkExisted() throws Exception {
    }

    @Test
    public void login() throws Exception {
    }

    @Test
    public void modify() throws Exception {
    }

    @Test
    public  void save() throws Exception{
        Agency agency = new Agency();
        agency.setAddress("我我");
        agency.setContacts("12312");
//        agency.setId(122);
        agency.setName("博文");
        agency.setPwd("wer");
        agency.setPhone("123445");
        agencyDAO.save(agency);
    }

}