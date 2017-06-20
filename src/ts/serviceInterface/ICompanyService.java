package ts.serviceInterface;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ts.model.*;

import java.sql.Time;
import java.util.Date;
import java.util.List;


@Path("/Company")
public interface ICompanyService {
    //======================================公司操作========================================
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/doLogin/{username}/{pwd}")
    Response doLogin(@PathParam("username") String username, @PathParam("pwd") String pwd);

    @POST
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/register")
    Company register(Company company);

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/checkUName/{companyUName}")
    Response checkUName(@PathParam("companyUName") String companyUName);

    @POST
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/modifyCompany")
    Company modifyCompany(Company company);

    //======================================航班延误取消恢复、取消整个公司的航班========================================
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/flightDelay/{flightID}/{departureDate}/{delayTime}")
    Response delay(@PathParam("flightID")String flightID,@PathParam("departureDate") Date departureDate, @PathParam("delayTime")Time delayTime);

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/flightCancel/{flightID}")
    Response flightCancel(@PathParam("flightID") String flightID);

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/flightResume/{flightID}")
    Response flightResume(@PathParam("flightID") String flightID);

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/cancelCompany/{companyUName}")
    Response cancelCompany(@PathParam("companyUName") String companyUName);

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/resumeCompany/{companyUName}")
    Response resumeCompany(@PathParam("companyUName") String companyUName);

    //======================================航班增删改查========================================
//这个不太会
    @POST
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/addFlight/{companyUName}")
    Response addFlight(@PathParam("companyUName") String companyUName, Flight flight);

    @POST
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/modifyFlight")
    Flight modifyFlight(Flight flight);

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/queryFlight/{companyUName}")
    List<Flight> queryFlight(@PathParam("companyUName") String companyUName);
}
