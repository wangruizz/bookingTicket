package ts;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ts.daoImpl.AirportDAO;
import ts.model.Airport;

import javax.annotation.Resource;

/**
 * Created by wr on 2017/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Test {
    @Resource
    AirportDAO airportDAO;

    @org.junit.Test
    public  void main() {
        //Test t = new Test();
        Airport airport = airportDAO.get("AAT");
       // System.out.println(airport.toString());
     //   airportDAO.removeById(1);
    }
}
