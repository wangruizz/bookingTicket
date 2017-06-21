package ts.serviceImpl;

import ts.daoImpl.*;
import ts.model.Agency;
import ts.model.Company;
import ts.model.Flight;
import ts.model.Message;
import ts.serviceInterface.ICompanyService;
import ts.util.JwtUtils;

import javax.ws.rs.core.Response;
import java.sql.Time;
import java.util.Date;
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
        try {
            if (!airCompanyDAO.checkHasExist(company.getUsername())) {
                airCompanyDAO.save(company);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return company;
    }

    @Override
    public Company modifyCompany(Company company) {
        airCompanyDAO.update(company);
        return company;
    }

    @Override
    public Response delay(String flightID, Date departureDate, Time delayTime) {
        if (delayTime == null) {//如果没设置延迟时间，则设置为0表示未知
            delayTime = java.sql.Time.valueOf("00:00:00");
        }
        historyDao.delay(flightID, departureDate, delayTime);
        return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
    }

    @Override
    public Response flightCancel(String flightID) {
        if (flightDAO.get(flightID) == null) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        flightDAO.cancelFlight(flightID);
        return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
    }

    @Override
    public Response flightResume(String flightID) {
        if (flightDAO.get(flightID) == null) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        flightDAO.resumeFlight(flightID);
        return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
    }

    /**
     * 取消公司的航班
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
        Flight flight1 = flightDAO.get(flight.getId());
        if (flight1 == null) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        if (!flight1.getCompany().getUsername().equals(companyUName)) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        flight.setCompany(flight1.getCompany());
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
        List<Flight> flights = flightDAO.query(companyUName);
        return flights;
    }

    @Override
    public Response queryFlight(String companyUName, String flightId) {
        Flight flight = flightDAO.get(flightId);
        if (flight == null) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass","Message").build();
        }
        if (!flight.getCompany().getUsername().equals(companyUName)) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass","Message").build();
        }
        return Response.ok(flight).header("EntityClass","Flight").build();
    }

    /**
     * 检测用户名在注册的时候是否存在
     * @param name
     * @return
     */
    @Override
    public Response checkUserName(String name) {
        if(airCompanyDAO.checkHasExist(name)){
            return Response.ok(new Message(Message.CODE.COMPANY_HAS_EXIST)).header("EntityClass","Message").build();
        }else{
            return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass","Message").build();
        }
    }

    @Override
    public Response modifyPwd(String username, String pwd1, String pwd2) {
        List<Company> list = airCompanyDAO.findBy("username",username,"id",true);
        Company company = list.get(0);
        if(company.getPwd().equals(pwd1)){
            company.setPwd(pwd2);
            airCompanyDAO.update(company);
            return Response.ok(company).header("EntityClass","Company").build();
        }else{
            return Response.ok(new Message(Message.CODE.PWD_IS_WRONG)).header("EntityClass","Message").build();
        }
    }


}
