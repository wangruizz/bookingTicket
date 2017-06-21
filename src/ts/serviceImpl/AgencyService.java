package ts.serviceImpl;

import org.hibernate.criterion.Restrictions;
import ts.daoImpl.AgencyDAO;
import ts.daoImpl.BookDAO;
import ts.daoImpl.HistoryDao;
import ts.daoImpl.PassengerDAO;
import ts.model.*;
import ts.serviceException.PassengerNotExistException;
import ts.serviceException.PhoneWrongException;
import ts.serviceException.RegisterException;
import ts.serviceException.TicketPayException;
import ts.serviceInterface.IAgencyService;
import ts.util.JwtUtils;

import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public Response searchPassenger(int agencyID, int passenger) throws PassengerNotExistException {
        Passenger passenger1 = passengerDAO.get(passenger);
        if (passenger1 != null) {
            if (passenger1.getAgency().getId() == agencyID) {
                return Response.ok(passenger1).header("EntityClass","Passenger").build();
            }
            throw new PassengerNotExistException();
        }
        throw new PassengerNotExistException();
    }

    @Override
    public List<Passenger> searchPassenger(int agencyID) {
        Agency agency = agencyDAO.get(agencyID);
        if (agency == null) {
            return new ArrayList<>();
        }
        List<Passenger> ans = passengerDAO.queryByID(agencyID);
        return ans;
    }

    /**
     * 查找乘客
     * @param agencyID
     * @param function
     * @param parameter
     * @return
     * @throws PassengerNotExistException
     */
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
                if(passengers==null){
                    throw new PassengerNotExistException();
                }
                passengers.add(passenger);
                break;
        }
        return passengers;
    }
    /**
     * 修改失败的情况如下：
     * 1、信息不完整
     * @param passenger
     * @return
     */
    @Override
    public Response modifyPassenger(Passenger passenger) {
        if(!passengerDAO.complete(passenger)){
            return Response.ok(new Message(Message.CODE.PASSENGER_INCOMPLICT)).header("EntityClass","Message").build();
        }else{
            passengerDAO.save(passenger);
            return Response.ok(passenger).header("EntityClass","Passenger").build();
        }
    }

    /**
     * 不能为空的属性为空时，会显示添加失败
     * @param passenger
     * @return
     */
    @Override
    public Response addPassenger(Passenger passenger) {
        if(!passengerDAO.complete(passenger)){//如果乘客信息不完整
            return Response.ok(new Message(Message.CODE.PASSENGER_INCOMPLICT)).header("EntityClass","Message").build();
        }else{
            passengerDAO.save(passenger);
            return Response.ok(passenger).header("EntityClass","Passenger").build();
        }
    }

    /**
     * 删除乘客
     * @param id
     * @return
     */
    @Override
    public Response deletePassenger(int id){
        Passenger passenger;
        if(passengerDAO.queryByID(id).get(0)!=null){
            passenger = passengerDAO.queryByID(id).get(0);
            passengerDAO.remove(passenger);
            return Response.ok(passenger).header("EntityClass","Passenger").build();
        }else{
            return Response.ok(new Message(Message.CODE.PASSENGER_NOT_EXIST)).header("EntityClass","Message").build();
        }
    }
    /**
     * 旅行社登录
     * 当电话号或者是密码错误的时候，显示登陆失败
     * @param phone
     * @param pwd
     * @return
     */
    @Override
    public Response AgencyLogin(String phone, String pwd) {
        Agency agency = agencyDAO.login(phone,pwd);
        if(agency==null){
            return Response.ok(new Message(Message.CODE.AGENCY_LOGIN_FAILED)).header("EntityClass","Message").build();
        }else{
            String name = agency.getName();
            String id = Integer.toString(agency.getId());
            agency.setToken(JwtUtils.createJWT(name,phone,id));//id作为个人签名
            return Response.ok(agency).header("EntityClass","Agency").build();
        }
    }
    /**
     * 旅行社注册
     * 注册失败原因是电话号码注册过，无法对相同电话号码进行重复注册
     * @param agency
     * @return
     * @throws RegisterException
     */
    @Override
    public Agency AgencyRegister(Agency agency) throws RegisterException, PhoneWrongException {
        String phone = agency.getPhone();
        if(!agencyDAO.checkPhone(phone)){
            throw new RegisterException();//因为手机号已经注册过，所以显示注册失败
        }else{
            if(!passengerDAO.match(phone)){
                throw new PhoneWrongException();//电话号码格式不对
            }else{
                agencyDAO.save(agency);
                return agency;
            }

        }
    }

    /**
     * 旅行社信息修改
     * 修改失败情况是电话号码为空或者是姓名为空
     * @param agency
     * @return
     */
    @Override
    public Response modifyAgency(Agency agency) throws PhoneWrongException {
        if(!agencyDAO.complete(agency)){
            return Response.ok(new Message(Message.CODE.AGENCY_MODIFY_FAILED)).header("EntityClass","Message").build();
        }else{
            if(!passengerDAO.match(agency.getPhone())){
                throw new PhoneWrongException();//电话号码格式不对
            }else {
                Agency agency1 = agencyDAO.findBy("id", false, Restrictions.eq("phone", agency.getPhone())).get(0);
                if (agency.getPwd() != null) {
                    agency1.setPwd(agency.getPwd());
                }
                if (agency.getAddress() != null) {
                    agency1.setAddress(agency.getAddress());
                }
                if (agency.getContacts() != null) {
                    agency1.setContacts(agency.getContacts());
                }
                if (agency.getName() != null) {
                    agency1.setName(agency.getName());
                }
                agencyDAO.save(agency1);
                return Response.ok(agency1).header("EntityClass", "Agency").build();
            }
        }
    }
    /**
     * 预订车票
     * @param book
     * @return
     */
    @Override
    public Response BookingTicket(Book book) {
        if(!bookDAO.complete(book)){
            return Response.ok(new Message(Message.CODE.BOOK_NOT_ALL)).header("EntityClass","Message").build();
        }else{
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
    public Response payTicket(int id) throws TicketPayException {
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

    @Override
    public Response queryBookByPhone(String phone) {
        List<Book> list = bookDAO.query(phone);
        if(list == null){
            return Response.ok(new Message(Message.CODE.BOOK_QUERY_FAILED)).header("EntityClass","Message").build();
        }else{
            return Response.ok(list).header("EntityClass","Book").build();
        }

    }

    @Override
    public Response queryBookByAID(int agencyID, int status) {
        List<Book> list = bookDAO.query(agencyID,status);
        if(list == null){
            return Response.ok(new Message(Message.CODE.BOOK_QUERY_FAILED)).header("EntityClass","Message").build();
        }else{
            return Response.ok(list).header("EntityClass","Book").build();
        }
    }

    @Override
    public Response queryBookByFID(String flightID,  int start, int end) {
        Date date1 = new Date(start);
        Date date2 = new Date(end);
        List<Book> list = bookDAO.query(flightID,date1, date2);
        if(list == null){
            return Response.ok(new Message(Message.CODE.BOOK_QUERY_FAILED)).header("EntityClass","Message").build();
        }else{
            return Response.ok(list).header("EntityClass","Book").build();
        }
    }

//    @Override
//    public Response queryBookByFID(int historyID) {
//        List<Book> list = bookDAO.query(historyID);
//        if(list==null){
//            return Response.ok(new Message(Message.CODE.BOOK_QUERY_FAILED)).header("EntityClass","Message").build();
//        }else{
//            return Response.ok(list).header("EntityClass","Book").build();
//        }
//    }

    /**
     * 检验手机号注册时是否存在
     * @param phone
     * @return
     */
    @Override
    public Response checkPhone(String phone) {
        if(!agencyDAO.checkPhone(phone)){
           return Response.ok(new Message(Message.CODE.AGENCY_REGISTER_FAILED)).header("EntityClass","Message").build();//因为手机号已经注册过，所以显示注册失败
        }else{
            return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass","Message").build();
        }
    }
    /**
     * 剩余机票查询
     */
    @Override
    public List<History> queryBook(String startAirport, String endAirport, String date) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date time = sdf.parse(date);
        return historyDao.TicketQuery(startAirport,endAirport,time);
    }
}