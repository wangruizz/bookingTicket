package ts.serviceInterface;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ts.model.*;
/**
 * Created by wr on 2017/6/13.
 */
@Path("/Domain")
public interface IDomainService {
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getAirport/{id}")
    Response getAirport(@PathParam("id") int id);
}
