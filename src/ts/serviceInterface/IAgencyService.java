package ts.serviceInterface;

import ts.model.Agency;
import ts.model.Book;
import ts.model.Passenger;
import ts.serviceException.PassengerNotExistException;
import ts.serviceException.RegisterException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
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
    @Path("/modifyPassenger")
    Response modifyPassenger(Passenger passenger);
    //增加乘客信息
    @POST
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Path("/addPassenger")
    Response addPassenger(Passenger passenger);
    //删除乘客信息
    @POST
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    @Path("/deletePassenger")
    Response deletePassenger(int id);
    //旅行社登录
    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Path("/doLogin/{phone}/{pwd}")
    Response AgencyLogin(@PathParam("phone") String phone,@PathParam("pwd") String pwd);
    //旅行社注册
    @POST
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Path("/AgencyRegister")
    Agency AgencyRegister(Agency agency) throws RegisterException;
    //旅行社信息修改
    @POST
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Path("/modifyAgency")
    Response modifyAgency(Agency agency);
    //预订车票
    @POST
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Path("/bookingTicket")
    Response BookingTicket(Book book);
    //取消预约
    @POST
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Path("/cancelBook")
    Response cancelBook(int id);
    //付款
    @POST
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Path("/payTicket")
    Response payTicket(int id);
    //打印机票
    @POST
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Path("/printTicket")
    Response printTicket(int id,String IDCard);
    //通过电话号码查询订单
    @POST
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Path("queryBookByPhone")
    Response queryBookByPhone(String phone);
    //通过旅行社ID，订单状态查询
    @POST
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Path("queryBookByAID")
    Response queryBookByAID(int agencyID,int ... status);
    //通过航班ID和起止日期查询
    @POST
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Path("queryBookByFID")
    Response queryBookByFID(String flightID,Date... dates);
    //通过历史表ID查询
    @POST
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Path("queryBookByHID")
    Response queryBookByFID(int historyID);
}
