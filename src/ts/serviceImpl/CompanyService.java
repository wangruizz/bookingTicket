package ts.serviceImpl;

import ts.daoImpl.AirportDAO;
import ts.model.Company;
import ts.serviceInterface.ICompanyService;

import javax.ws.rs.core.Response;


public class CompanyService implements ICompanyService {
    private AirportDAO airportDAO;

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
    public Response doLogin(String username, String pwd) {
        return null;
    }

    @Override
    public Company register(Company company) {
        return null;
    }


}
