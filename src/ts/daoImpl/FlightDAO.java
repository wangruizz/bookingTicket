package ts.daoImpl;

import org.hibernate.criterion.Restrictions;
import ts.daoBase.BaseDao;
import ts.model.Airport;
import ts.model.Company;
import ts.model.Flight;

import java.util.Date;
import java.util.List;

/**
 * Created by 12556 on 2017/6/15.
 */
public class FlightDAO extends BaseDao<Flight, String> {

    AirCompanyDAO companyDAO;
    AirportDAO airportDAO;

    public AirportDAO getAirportDAO() {
        return airportDAO;
    }

    public void setAirportDAO(AirportDAO airportDAO) {
        this.airportDAO = airportDAO;
    }

    public AirCompanyDAO getCompanyDAO() {
        return companyDAO;
    }

    public void setCompanyDAO(AirCompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }

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
        Airport airport = airportDAO.get(startPortID);
        Airport airport1 = airportDAO.get(arrivePortID);
        List<Flight> flights;
        if (currentDate.equals(departureDate)) {//出发日期是今天
            long timestamp = System.currentTimeMillis();
            timestamp += 30 * 60 * 1000;//当前时间加上半小时
            flights = findBy("startTime", true,
                    Restrictions.eq("startAirport", airport),
                    Restrictions.eq("status", Flight.STATUS.FLIGHT_NORMAL),
                    Restrictions.eq("arriveAirport", airport1),
                    Restrictions.ge("startTime", timestamp));
        } else {
            flights = findBy("startTime", true,
                    Restrictions.eq("status", Flight.STATUS.FLIGHT_NORMAL),
                    Restrictions.eq("startAirport", airport),
                    Restrictions.eq("arriveAirport", airport1));
        }
        return flights;
    }


    /**
     * 航班恢复，这里只在航班表里改了状态
     *
     * @return
     */
    public boolean resumeFlight(String flightID) {
        Flight flight = get(flightID);
        if (flight.getStatus() != Flight.STATUS.FLIGHT_NORMAL) {
            flight.setStatus(Flight.STATUS.FLIGHT_NORMAL);
            update(flight);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 航班取消，这里只在航班表里改了状态
     *
     * @return
     */
    public boolean cancelFlight(String flightID) {
        Flight flight = get(flightID);
        if (flight.getStatus() != Flight.STATUS.FLIGHT_CANCEL) {//只有没被取消的航班才行
            flight.setStatus(Flight.STATUS.FLIGHT_CANCEL);
            update(flight);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 航空公司给自己增加航班
     * 服务层传过来的flight里已经有了companyUName,服务层需要先判断id是否存在
     *
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
        Company company = companyDAO.get(companyUName);
        return findBy("company", company, "id", true);
    }

    /**
     * 取消或恢复一个航空公司的全部航班（未经测试）
     *
     * @param companyUName
     * @return
     */
    public boolean changeCompany(String companyUName, int toStatus) {
        Company company = companyDAO.get(companyUName);
        List<Flight> flights = query(companyUName);
        if (flights != null && flights.size() > 0) {
            for (Flight i : flights) {
                if (toStatus == Flight.STATUS.FLIGHT_CANCEL) {
                    i.setStatus(Flight.STATUS.FLIGHT_CANCEL);
                } else if (toStatus == Flight.STATUS.FLIGHT_NORMAL) {
                    i.setStatus(Flight.STATUS.FLIGHT_NORMAL);
                }
                update(i);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 取消一个航空公司的全部航班
     *
     * @param companyUName
     * @return
     */
    public boolean cancelCompany(String companyUName) {
        List<Flight> flights = query(companyUName);
        if (flights != null && flights.size() > 0) {
            for (Flight i : flights) {
                i.setStatus(Flight.STATUS.FLIGHT_CANCEL);
                update(i);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 恢复一个航空公司的全部航班
     *
     * @param companyUName
     * @return
     */
    public boolean resumeCompany(String companyUName) {
        Company company = companyDAO.get(companyUName);
        List<Flight> flights = query(companyUName);
        if (flights != null && flights.size() > 0) {
            for (Flight i : flights) {
                i.setStatus(Flight.STATUS.FLIGHT_NORMAL);
                update(i);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean checkHasExist(String flightID) {
        return get(flightID) != null;
    }

}
