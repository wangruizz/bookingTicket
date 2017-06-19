package ts.serviceImpl;

import ts.daoImpl.*;
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
    public Response doLogin(String username, String pwd){
        Company company = airCompanyDAO.login(username, pwd);
        try {
            if (company != null) {
                company.setToken(JwtUtils.createJWT(company.getName(), company.getUsername(), ""));
                return Response.ok(company).header("EntityClass", "CompanyInfo").build();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return Response.ok(new Message(Message.CODE.LOGIN_FAILED)).header("EntityClass", "Message").build();
    }

    @Override
    public Company register(Company company) {
        try {
            if (!airCompanyDAO.checkHasExist(company.getUsername())) {
                Company company1 = new Company();
                company1.setUsername(company.getUsername());
                company1.setPwd(company.getPwd());
                company1.setPhone(company.getPhone());
                company1.setName(company.getName());
                airCompanyDAO.register(company1);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return company;
    }

    @Override
    public Response checkUName(String companyUName) {
        return !airCompanyDAO.checkHasExist(companyUName) ? Response.ok().build() : Response.ok(new Message(Message.CODE.COMPANY_HAS_EXIST)).header("EntityClass", "Message").build();
    }

    @Override
    public Company modifyCompany(Company company) {
        airCompanyDAO.modify(company);
        return company;
    }

    @Override
    public Response delay(String flightID, Date departureDate, Time delayTime) {
        if (delayTime == null) {//如果没设置延迟时间，则设置为0表示未知
            delayTime = java.sql.Time.valueOf("00:00:00");
        }
        if (historyDao.delay(flightID, departureDate, delayTime)) {
            return Response.ok().build();
        } else {
            return Response.serverError().build();
        }
    }

    @Override
    public Response flightCancel(String flightID) {
        if (flightDAO.get(flightID) == null){
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass","Message").build();
        }
        return flightDAO.cancelFlight(flightID) ? Response.ok().build() : Response.ok(new Message(Message.CODE.FLIGHT_HAS_CANCELLED)).header("EntityClass","Message").build();
    }

    @Override
    public Response flightResume(String flightID) {
        if (flightDAO.get(flightID) == null){
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass","Message").build();
        }
        return flightDAO.resumeFlight(flightID) ? Response.ok().build() : Response.ok(new Message(Message.CODE.FLIGHT_IS_NORMOL)).header("EntityClass","Message").build();
    }


    @Override
    public Response cancelCompany(String companyUName) {
        if (airCompanyDAO.get(companyUName) == null){
            return Response.ok(new Message(Message.CODE.COMPANY_HAS_EXIST)).header("EntityClass","Message").build();
        }
        return flightDAO.cancelCompany(companyUName) ? Response.ok().build() : Response.serverError().build();
    }

    @Override
    public Response resumeCompany(String companyUName) {
        if (airCompanyDAO.get(companyUName) == null){
            return Response.ok(new Message(Message.CODE.COMPANY_NOT_EXIST)).header("EntityClass","Message").build();
        }
        return flightDAO.resumeCompany(companyUName) ? Response.ok().build() : Response.serverError().build();
    }

    @Override
    public Response addFlight(String companyUName, Flight flight) {

        try {
            Company company = airCompanyDAO.get(companyUName);
            flight.setCompany(company);
            flightDAO.add(companyUName, flight);
            return Response.ok(flight).header("EntityClass", "EntityClass").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }


    @Override
    public Flight modifyFlight(Flight flight) {
        return flightDAO.modify(flight);
    }

    @Override
    public List<Flight> queryFlight(String companyUName) {
        List<Flight> flights = flightDAO.query(companyUName);
        return flights;
    }


}
