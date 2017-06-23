package ts.serviceInterface;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ts.model.*;

import java.sql.Time;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


@Path("/Company")
public interface ICompanyService {
    //======================================公司操作========================================
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/doLogin/{username}/{pwd}")
    Response doLogin(@PathParam("username") String username, @PathParam("pwd") String pwd);

    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/register")
    Company register(Company company);

    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/modifyCompany")
    Company modifyCompany(Company company);

    //======================================航班延误取消恢复、取消整个公司的航班========================================
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/flightDelay/{companyUName}/{flightID}/{departureDate}/{delayTime}")
    Response delay(@PathParam("companyUName") String companyUName, @PathParam("flightID") String flightID, @PathParam("departureDate") String departureDate, @PathParam("delayTime") String delayTime) throws ParseException;

    //永久取消某个航班
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/flightCancel/{companyUName}/{flightID}")
    Response flightCancel(@PathParam("companyUName") String companyUName, @PathParam("flightID") String flightID);

    //恢复某一个航班
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/flightResume/{companyUName}/{flightID}")
    Response flightResume(@PathParam("companyUName") String companyUName, @PathParam("flightID") String flightID);

    //取消整个个公司的航班
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/cancelCompany/{companyUName}")
    Response cancelCompany(@PathParam("companyUName") String companyUName);

    //恢复整个公司的航班
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/resumeCompany/{companyUName}")
    Response resumeCompany(@PathParam("companyUName") String companyUName);

    //======================================航班增删改查========================================
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/addFlight/{companyUName}")
    Response addFlight(@PathParam("companyUName") String companyUName, Flight flight);

    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/modifyFlight/{companyUName}")
    Response modifyFlight(@PathParam("companyUName") String companyUName, Flight flight);

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/queryFlight/{companyUName}")
    List<Flight> queryFlight(@PathParam("companyUName") String companyUName);

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/queryFlight/{companyUName}/{flightId}")
    Response queryFlight(@PathParam("companyUName") String companyUName, @PathParam("flightId") String flightId);

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/checkUserName/{name}")
    Response checkUserName(@PathParam("name") String name);

    //修改密码
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/modifyPwd/{username}/{pwd1}/{pwd2}")
    Response modifyPwd(@PathParam("username") String username, @PathParam("pwd1") String pwd1, @PathParam("pwd2") String pwd2);

    //取消某一天的航班
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/cancelFlightSomeday/{companyUName}/{flightID}/{departureDate}")
    Response cancelFlightSomeday(@PathParam("companyUName") String companyUName, @PathParam("flightID") String flightID, @PathParam("departureDate") String departureDate) throws ParseException;

    //恢复某一天的航班
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/ResumeFlightSomeday/{companyUName}/{flightID}/{departureDate}")
    Response ResumeFlightSomeday(@PathParam("companyUName") String companyUName, @PathParam("flightID") String flightID, @PathParam("departureDate") String departureDate) throws ParseException;

}
