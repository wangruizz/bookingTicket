package ts.serviceInterface;

import ts.model.Passenger;
import ts.serviceException.PassengerNotExistException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/Agency")
public interface IAgencyService {
    //查询乘客
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/searchPassenger/{agencyID}/{function}/{parameter}")
    List<Passenger> searchPassenger(@PathParam("agencyID") int agencyID, @PathParam("function") String function, @PathParam("parameter") String parameter) throws PassengerNotExistException;
    //修改乘客信息
    @POST
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/motifyPassenger")
    Response motifyPassenger(Passenger passenger);
    //增加乘客信息
    @POST
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Path("/addPassenger")
    Response addPassenger(Passenger passenger);


}
