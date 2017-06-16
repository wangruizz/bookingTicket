package ts.daoImpl;

import org.hibernate.criterion.Restrictions;
import ts.daoBase.BaseDao;
import ts.model.Flight;

import java.util.Date;
import java.util.List;

/**
 * Created by 12556 on 2017/6/15.
 */
public class FlightDAO extends BaseDao<Flight, String> {

    public FlightDAO() {
        super(Flight.class);
    }

    /**
     * 旅行社根据当前时间（服务器获取）、出发日期、出发机场和目标机场ID，查询航班
     *
     * @return
     */
    public List<Flight> query(Date departureDate, int startPortID, int arrivePortID) {
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
        List<Flight> flights;
        if (currentDate.equals(departureDate)){//出发日期是今天
            long timestamp = System.currentTimeMillis();
            timestamp +=30*60*1000;//当前时间加上半小时
            flights = findBy("id", true,
                    Restrictions.eq("startAirport", startPortID),
                    Restrictions.eq("status",Flight.STATUS.FLIGHT_NORMAL),
                    Restrictions.eq("arriveAirport", arrivePortID),
                    Restrictions.ge("startTime",timestamp));
        }else {
            flights = findBy("id", true,
                    Restrictions.eq("status",Flight.STATUS.FLIGHT_NORMAL),
                    Restrictions.eq("startAirport", startPortID),
                    Restrictions.eq("arriveAirport", arrivePortID));
        }
        return flights;
    }



    /**航班恢复，这里只在航班表里改了状态
     * @return
     */
    public boolean resumeFlight(String flightID) {
        Flight flight = get(flightID);
        if (flight == null) {
            return false;
        } else {
            flight.setStatus(Flight.STATUS.FLIGHT_NORMAL);
            update(flight);
            return true;
        }
    }

    /**
     * 航班取消，这里只在航班表里改了状态
     *
     * @return
     */
    public boolean cancel(String flightID) {
        Flight flight = get(flightID);
        if (flight == null) {
            return false;
        } else {
            flight.setStatus(Flight.STATUS.FLIGHT_CANCEL);
            update(flight);
            return true;
        }
    }

    /**
     * 航空公司给自己增加航班
     *服务层传过来的flight里已经有了companyUName
     * @param companyUName
     * @param flight
     * @return
     */
    public Flight add(String companyUName, Flight flight) {
      save(flight);
      return flight;
    }

    /**
     * 航空公司查询自己的全部航班
     *
     * @param companyUName
     * @return
     */
    public List<Flight> query(String companyUName) {
        return  findBy("companyUName",companyUName,"id",true);
    }

    /**
     * 取消一个航空公司的全部航班
     *
     * @param companyUName
     * @return
     */
    public boolean cancelCompany(String companyUName) {
        List<Flight> flights =  query(companyUName);
        if (flights != null && flights.size() > 0){
            for (Flight i:flights){
                i.setStatus(Flight.STATUS.FLIGHT_CANCEL);
                update(i);
            }
            return  true;
        }else{
            return false;
        }
    }

    public Flight CheckHasExist(String flightID){
        return  get(flightID);
    }
}
