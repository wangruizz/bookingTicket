package ts.serviceImpl;

import ts.daoImpl.*;
import ts.model.Company;
import ts.model.Flight;
import ts.model.History;
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

    public AirportDAO getAirportDAO() {
        return airportDAO;
    }

    public void setAirportDAO(AirportDAO airportDAO) {
        this.airportDAO = airportDAO;
    }

    @Override
    public Response getAirport(int id) {
        //   Airport airport = airportDAO.getByID(id).get(0);
        return Response.ok().header("EntityClass", "ExpressSheet").build();
    }

    @Override
    public Response doLogin(String username, String pwd){
        Company company = airCompanyDAO.login(username, pwd);
        if (company != null) {
            company.setToken(JwtUtils.createJWT(company.getName(), company.getUsername(), ""));
            return Response.ok(company).header("EntityClass", "CompanyInfo").build();
        }
        return Response.ok(new Message(Message.CODE.LOGIN_FAILED)).header("EntityClass", "Message").build();
    }

    @Override
    public Company register(Company company) {
        try {
            if (!airCompanyDAO.checkHasExist(company.getUsername())) {
                airCompanyDAO.register(company);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return company;
    }

    @Override
    public Response checkUName(String companyUName) {
        return airCompanyDAO.checkHasExist(companyUName) ? Response.ok().build() : Response.ok(new Message(Message.CODE.COMPANY_HAS_EXIST)).header("EntityClass", "Message").build();
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
        return flightDAO.cancelFlight(flightID) ? Response.ok().build() : Response.serverError().build();
    }

    @Override
    public Response flightResume(String flightID) {
        return flightDAO.resumeFlight(flightID) ? Response.ok().build() : Response.serverError().build();
    }


    @Override
    public Response cancelCompany(String companyUName) {
        return flightDAO.cancelCompany(companyUName) ? Response.ok().build() : Response.serverError().build();
    }

    @Override
    public Response resumeCompany(String companyUName) {
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
