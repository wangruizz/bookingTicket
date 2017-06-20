package ts.serviceImpl;

import ts.daoImpl.AgencyDAO;
import ts.daoImpl.BookDAO;
import ts.daoImpl.HistoryDao;
import ts.daoImpl.PassengerDAO;
import ts.model.*;
import ts.serviceException.PassengerNotExistException;
import ts.serviceException.RegisterException;
import ts.serviceInterface.IAgencyService;
import ts.util.JwtUtils;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class AgencyService implements IAgencyService {

    private PassengerDAO passengerDAO;
    private AgencyDAO agencyDAO;
    private BookDAO bookDAO;
    private HistoryDao historyDao;

    public PassengerDAO getPassengerDAO() {
        return passengerDAO;
    }

    public void setPassengerDAO(PassengerDAO passengerDAO) {
        this.passengerDAO = passengerDAO;
    }

    public AgencyDAO getAgencyDAO() {
        return agencyDAO;
    }

    public void setAgencyDAO(AgencyDAO agencyDAO) {
        this.agencyDAO = agencyDAO;
    }

    public BookDAO getBookDAO() {
        return bookDAO;
    }

    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public HistoryDao getHistoryDao() {
        return historyDao;
    }

    public void setHistoryDao(HistoryDao historyDao) {
        this.historyDao = historyDao;
    }

    //查找乘客
    @Override
    public List<Passenger> searchPassenger(int agencyID, String function, String parameter) throws PassengerNotExistException {
        List<Passenger> passengers = new ArrayList<>();
        switch (function) {
            case "name" :
                passengers = passengerDAO.queryByName(parameter, agencyID);
                if(passengers==null){
                    throw new PassengerNotExistException();
                }
                break;
            case "phone" :
                passengers = passengerDAO.queryByPhone(parameter, agencyID);
                if(passengers==null){
                    throw new PassengerNotExistException();
                }
                break;
            case "idcard" :
                Passenger passenger = passengerDAO.queryByIDCard(parameter, agencyID);
                passengers.add(passenger);
                if(passengers==null){
                    throw new PassengerNotExistException();
                }
                break;
        }
        return passengers;
    }
    /*
    * 修改失败的情况如下：
    * 1、信息不完整
    * */
    @Override
    public Response motifyPassenger(Passenger passenger) {
        if(passengerDAO.complete(passenger)==false){
            return Response.ok(new Message(Message.CODE.PASSENGER_INCOMPLICT)).header("EntityClass","Message").build();
        }else{
            passengerDAO.save(passenger);
            return Response.ok().header("EntityClass","Passenger").build();
        }
    }
    /*添加乘客
    * 不能为空的属性为空时，会显示添加失败*/
    @Override
    public Response addPassenger(Passenger passenger) {
        if(passengerDAO.complete(passenger)==false){//如果乘客信息不完整
            return Response.ok(new Message(Message.CODE.PASSENGER_INCOMPLICT)).header("EntityClass","Message").build();
        }else{
            passengerDAO.save(passenger);
            return Response.ok().header("EntityClass","Passenger").build();
        }
    }

    @Override
    public Response deletePassenger(int id){
        Passenger passenger;
        if(passengerDAO.queryByID(id).get(0)!=null){
            passenger = passengerDAO.queryByID(id).get(0);
            passengerDAO.remove(passenger);
            return Response.ok().header("EntityClass","Passenger").build();
        }else{
            return Response.ok(new Message(Message.CODE.PASSENGER_NOT_EXIST)).header("EntityClass","Message").build();
        }
    }
    /*旅行社登录
    * 当电话号或者是密码错误的时候，显示登陆失败
    * */
    @Override
    public Response AgencyLogin(String phone, String pwd) {
        Agency agency = agencyDAO.login(phone,pwd);
        if(agency==null){
            return Response.ok(new Message(Message.CODE.AGENCY_LOGIN_FAILED)).header("EntityClass","Message").build();
        }else{
            String name = agency.getName();
            String id = Integer.toString(agency.getId());
            agency.setToken(JwtUtils.createJWT(name,phone,id));//id作为个人签名
            return Response.ok(agency).header("Entityclass","Agency").build();
        }
    }
    /*旅行社注册
    * 注册失败原因是电话号码注册过，无法对相同电话号码进行重复注册
    * */
    @Override
    public Agency AgencyRegister(Agency agency) throws RegisterException {
        String phone = agency.getPhone();
        if(agencyDAO.findBy("phone",phone,"id",true)!=null){
            throw new RegisterException();//因为手机号已经注册过，所以显示注册失败
        }else{
            agencyDAO.save(agency);
            return agency;
        }
    }
    /*旅行社信息修改
    * 修改失败情况是电话号码为空或者是姓名为空
    * */
    @Override
    public Response motifyAgency(Agency agency) {
        if(agencyDAO.complete(agency)==false){
            return Response.ok(new Message(Message.CODE.AGENCY_MOTIFY_FAILED)).header("EntityClass","Message").build();
        }else{
            agencyDAO.save(agency);
            return Response.ok(agency).header("EntityClass","Agency").build();
        }
    }
    //预订车票
    @Override
    public Response BookingTicket(Book book) {
       synchronized (bookDAO){
           History history = book.getHistory();
           if(book.getSeatType()==Book.SEAT_TYPE.BUSINESS_SEAT&&history.getBusinessNum()>0){
               history.setBusinessNum(history.getBusinessNum()-1);
               book.setStatus(Book.BOOK_STATUS.BOOK_UNPAID);//等待付款
               historyDao.save(history);
               bookDAO.save(book);
               return Response.ok(history).header("EntityClass","History").build();
           }else{
               if(book.getSeatType()==Book.SEAT_TYPE.ECONOMY_SEAT&&history.getEconomyNum()>0){
                   history.setBusinessNum(history.getEconomyNum()-1);
                   book.setStatus(Book.BOOK_STATUS.BOOK_UNPAID);//等待付款
                   historyDao.save(history);
                   bookDAO.save(book);
                   return Response.ok(history).header("EntityClass","History").build();
               }else{
                   return Response.ok(new Message(Message.CODE.BOOK_FAILED)).header("EntityClass","Message").build();
               }
           }
       }
    }
    //取消订单
    @Override
    public Response cancelBook(int id) {
        Book book = bookDAO.cancel(id);
        if(book==null){
            return Response.ok(new Message(Message.CODE.BOOK_CANCEL_FAILED)).header("EntityClass","Message").build();//取消订单失败
        }else{
            return Response.ok(book).header("EntityClass","Book").build();
        }
    }
    //付款
    @Override
    public Response payTicket(int id) {
        Book book = bookDAO.pay(id);
        if(book==null){
            return Response.ok(new Message(Message.CODE.BOOK_PAY_FAILED)).header("EntityClass","Message").build();
        }else{
            return Response.ok(book).header("EntityClass","Book").build();
        }
    }
   //打印机票
    @Override
    public Response printTicket(int id,String IDCard) {
        Book book = bookDAO.printTicket(id,IDCard);
        if(book==null){
            return Response.ok(new Message(Message.CODE.BOOK_PRINT_FAILED)).header("EntityClass","Message").build();
        }else{
            return Response.ok(book).header("EntityClass","Book").build();
        }
    }


}
