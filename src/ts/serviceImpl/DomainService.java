package ts.serviceImpl;

import ts.daoImpl.AirportDAO;
import ts.serviceInterface.IDomainService;

import javax.ws.rs.core.Response;

/**
 * Created by wr on 2017/6/13.
 */

public class DomainService implements IDomainService {
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

}
