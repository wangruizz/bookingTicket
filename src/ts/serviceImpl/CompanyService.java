package ts.serviceImpl;

import org.hibernate.criterion.Restrictions;
import ts.daoImpl.*;
import ts.model.*;
import ts.serviceException.FlightNotExistException;
import ts.serviceInterface.ICompanyService;
import ts.util.JwtUtils;

import javax.ws.rs.core.Response;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


public class CompanyService implements ICompanyService {
    private AirportDAO airportDAO;
    private AirCompanyDAO airCompanyDAO;
    private BookDAO bookDAO;
    private FlightDAO flightDAO;
    private HistoryDao historyDao;

    public AirCompanyDAO getAirCompanyDAO() {
        return airCompanyDAO;
    }

    public void setAirCompanyDAO(AirCompanyDAO airCompanyDAO) {
        this.airCompanyDAO = airCompanyDAO;
    }

    public BookDAO getBookDAO() {
        return bookDAO;
    }

    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public FlightDAO getFlightDAO() {
        return flightDAO;
    }

    public void setFlightDAO(FlightDAO flightDAO) {
        this.flightDAO = flightDAO;
    }

    public HistoryDao getHistoryDao() {
        return historyDao;
    }

    public void setHistoryDao(HistoryDao historyDao) {
        this.historyDao = historyDao;
    }

    public AirportDAO getAirportDAO() {
        return airportDAO;
    }

    public void setAirportDAO(AirportDAO airportDAO) {
        this.airportDAO = airportDAO;
    }


    @Override
    public Response doLogin(String username, String pwd) {
        Company company = airCompanyDAO.login(username, pwd);
        try {
            if (company != null) {
                company.setToken(JwtUtils.createJWT(company.getName(), company.getUsername(), ""));
                return Response.ok(company).header("EntityClass", "CompanyInfo").build();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return Response.ok(new Message(Message.CODE.LOGIN_FAILED)).header("EntityClass", "Message").build();
    }

    @Override
    public Company register(Company company) {
        if (!airCompanyDAO.checkHasExist(company.getUsername())) {
            airCompanyDAO.save(company);
        }
        return company;
    }

    @Override
    public Company modifyCompany(Company company) {
        airCompanyDAO.update(company);
        return company;
    }

    @Override
    public Response delay(String companyUName, String flightID, String departureDate, String delayTime) throws ParseException {
        String[] v = delayTime.split(":");
        Time time = new Time(Integer.valueOf(v[0]), Integer.valueOf(v[1]), 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        historyDao.delay(flightID, sdf.parse(departureDate), time);
        return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
    }

    @Override
    public Response flightCancel(String companyUName, String flightID) {
        Flight flight = flightDAO.get(flightID);
        if (flight == null) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        if (!flight.getCompany().getUsername().equals(companyUName)) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        if (flight.getStatus() != Flight.STATUS.FLIGHT_NORMAL) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        flightDAO.remove(flight);
        return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
    }

    @Override
    public Response flightResume(String companyUName, String flightID) {
        Flight flight = flightDAO.get(flightID);
        if (flight == null) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        if (!flight.getCompany().getUsername().equals(companyUName)) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        if (flight.getStatus() != Flight.STATUS.FLIGHT_CANCEL) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        flightDAO.resumeFlight(flightID);
        return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
    }

    /**
     * 取消公司的航班
     *
     * @param companyUName
     * @return
     */
    @Override
    public Response cancelCompany(String companyUName) {
        if (airCompanyDAO.get(companyUName) == null) {
            return Response.ok(new Message(Message.CODE.COMPANY_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        flightDAO.cancelCompany(companyUName);
        return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
    }

    /**
     * 恢复整个公司的航班
     *
     * @param companyUName
     * @return
     */
    @Override
    public Response resumeCompany(String companyUName) {
        if (airCompanyDAO.get(companyUName) == null) {
            return Response.ok(new Message(Message.CODE.COMPANY_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        flightDAO.resumeCompany(companyUName);
        return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
    }

    /**
     * 给某公司增加航班
     *
     * @param companyUName
     * @param flight
     * @return
     */
    @Override
    public Response addFlight(String companyUName, Flight flight) {
        try {
            Company company = airCompanyDAO.get(companyUName);
            flight.setCompany(company);
            flight.setStatus(Flight.STATUS.FLIGHT_NORMAL);
            flightDAO.add(companyUName, flight);
            return Response.ok(flight).header("EntityClass", "Flight").build();
        } catch (Exception e) {
            return Response.ok().entity(new Message(Message.CODE.UNKNOWN_ERROR)).header("EntityClass", "Message").build();
        }
    }

    /**
     * 修改航班信息
     *
     * @param flight
     * @return
     */
    @Override
    public Response modifyFlight(String companyUName, Flight flight) {
        flight.setCompany(airCompanyDAO.get(companyUName));
        flightDAO.update(flight);
        return Response.ok(flight).header("EntityClass", "Flight").build();
    }

    /**
     * 查询某公司的航班
     *
     * @param companyUName
     * @return
     */
    @Override
    public List<Flight> queryFlight(String companyUName) {
        return flightDAO.query(companyUName);
    }

    @Override
    public Response queryFlight(String companyUName, String flightId) {
        Flight flight = flightDAO.get(flightId);
        if (flight == null) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        if (!flight.getCompany().getUsername().equals(companyUName)) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        return Response.ok(flight).header("EntityClass", "Flight").build();
    }

    @Override
    public Response queryHistory(String companyUName, String flightId) {
        Flight flight = flightDAO.get(flightId);
        if (flight == null) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        if (!flight.getCompany().getUsername().equals(companyUName)) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        Response res = null;
        History history = null;
        try {
            history = historyDao.findBy("id", true, Restrictions.eq("flight", flight), Restrictions.eq("departureDate", new Date(new java.util.Date().getTime()))).get(0);
        } catch (Exception e) {
            res = Response.ok(flight).header("EntityClass", "Flight").build();
        }
        if (history != null) {
            res = Response.ok(history).header("EntityClass", "History").build();
        }
        return res;
    }

    @Override
    public List<History> form(String companyUName, String flightId) throws FlightNotExistException {
        Flight flight = flightDAO.get(flightId);
        if (flight == null) {
            throw new FlightNotExistException();
        }
        if (!flight.getCompany().getUsername().equals(companyUName)) {
            throw new FlightNotExistException();
        }
        List<History> ans;
        ans = historyDao.findBy("departureDate", false, Restrictions.eq("flight", flight));
        return ans;
    }

    /**
     * 检测用户名在注册的时候是否存在
     *
     * @param name
     * @return
     */
    @Override
    public Response checkUserName(String name) {
        if (airCompanyDAO.checkHasExist(name)) {
            return Response.ok(new Message(Message.CODE.COMPANY_HAS_EXIST)).header("EntityClass", "Message").build();
        } else {
            return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
        }
    }

    @Override
    public Response modifyPwd(String username, String pwd1, String pwd2) {
        Company company = airCompanyDAO.get(username);
        if (company.getPwd().equalsIgnoreCase(pwd1)) {
            company.setPwd(pwd2);
            airCompanyDAO.update(company);
            return Response.ok(company).header("EntityClass", "Company").build();
        } else {
            return Response.ok(new Message(Message.CODE.PWD_IS_WRONG)).header("EntityClass", "Message").build();
        }
    }


    @Override
    public Response cancelFlightSomeday(String companyUName, String flightID, String departureDate) throws ParseException {
        Flight flight = flightDAO.get(flightID);
        if (flight == null) {//没有这个航班
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        } else {
            if (flight.getStatus() == Flight.STATUS.FLIGHT_CANCEL) {//已经被取消
                return Response.ok(new Message(Message.CODE.FLIGHT_HAS_CANCELLED)).header("EntityClass", "Message").build();
            } else if (!flight.getCompany().getUsername().equals(companyUName)) {
                return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        History history = historyDao.queryHistory(flightID, sdf.parse(departureDate));
        if (history == null) {//还没这条记录，那就插一条
            history = historyDao.add(flightID, new java.sql.Date(sdf.parse(departureDate).getTime()));
        }
        historyDao.cancelFlight(history);
        return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
    }

    @Override
    public Response ResumeFlightSomeday(String companyUName, String flightID, String departureDate) throws ParseException {
        Flight flight = flightDAO.get(flightID);
        if (flight == null) {//没有这个航班
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        } else {
            if (flight.getStatus() == Flight.STATUS.FLIGHT_NORMAL) {//航班状态正常，不需要恢复
                return Response.ok(new Message(Message.CODE.FLIGHT_IS_NORMAL)).header("EntityClass", "Message").build();
            } else if (!flight.getCompany().getUsername().equals(companyUName)) {
                return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        History history = historyDao.queryHistory(flightID, sdf.parse(departureDate));
        if (history == null) {//还没这条记录，不需要恢复(因为默认插入的就是正常状态)
            return Response.ok(new Message(Message.CODE.FLIGHT_IS_NORMAL)).header("EntityClass", "Message").build();
        }
        historyDao.resumeFlight(history);
        return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
    }

    @Override
    public Response fdasfgasdfgasd() {
        historyDao.init();
        return null;
    }

}
