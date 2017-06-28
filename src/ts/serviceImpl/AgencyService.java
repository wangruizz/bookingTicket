package ts.serviceImpl;

import org.hibernate.criterion.Restrictions;
import ts.daoImpl.*;
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
                return Response.ok(passenger1).header("EntityClass", "Passenger").build();
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
        return passengerDAO.queryByID(agencyID);
    }

    /**
     * 查找乘客
     *
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
            case "name":
                passengers = passengerDAO.queryByName(parameter, agencyID);
                break;
            case "phone":
                passengers = passengerDAO.queryByPhone(parameter, agencyID);
                break;
            case "idcard":
                Passenger passenger = passengerDAO.queryByIDCard(parameter, agencyID);
                if (passenger == null) {
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
     *
     * @param passenger
     * @return
     */
    @Override
    public Response modifyPassenger(int agencyId, Passenger passenger) {
        Agency agency = agencyDAO.get(agencyId);
        if (agency == null) {
            return Response.ok(new Message(Message.CODE.AGENCY_NOT_EXISTED)).header("EntityClass", "Message").build();
        }
        Passenger passenger1 = passengerDAO.get(passenger.getId());
        if (passenger1.getAgency().getId() != agencyId) {
            return Response.ok(new Message(Message.CODE.PASSENGER_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        passenger1.setSex(passenger.getSex());
        if (passenger.getName() != null) {
            passenger1.setName(passenger.getName());
        }
        if (passenger.getIdcard() != null) {
            passenger1.setIdcard(passenger.getIdcard());
        }
        if (passenger.getPhone() != null) {
            passenger1.setPhone(passenger.getPhone());
        }
        passengerDAO.update(passenger1);
        return Response.ok(passenger1).header("EntityClass", "Passenger").build();
    }

    /**
     * 不能为空的属性为空时，会显示添加失败
     *
     * @param passenger
     * @return
     */
    @Override
    public Response addPassenger(int agencyId, Passenger passenger) {
        Agency agency = agencyDAO.get(agencyId);
        if (agency == null) {
            return Response.ok(new Message(Message.CODE.AGENCY_NOT_EXISTED)).header("EntityClass", "Message").build();
        }
        passenger.setAgency(agency);
        if (!passengerDAO.complete(passenger)) {//如果乘客信息不完整
            return Response.ok(new Message(Message.CODE.PASSENGER_INCOMPLICT)).header("EntityClass", "Message").build();
        } else {
            passengerDAO.save(passenger);
            return Response.ok(passenger).header("EntityClass", "Passenger").build();
        }
    }

    /**
     * 删除乘客
     *
     * @param id
     * @return
     */
    @Override
    public Response deletePassenger(int agencyId, int id) {
        Passenger passenger = passengerDAO.get(id);
        if (passenger == null) {
            return Response.ok(new Message(Message.CODE.PASSENGER_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        if (passenger.getAgency().getId() != agencyId) {
            return Response.ok(new Message(Message.CODE.PASSENGER_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        passengerDAO.remove(passenger);
        return Response.ok(passenger).header("EntityClass", "Passenger").build();
    }

    /**
     * 旅行社登录
     * 当电话号或者是密码错误的时候，显示登陆失败
     *
     * @param phone
     * @param pwd
     * @return
     */
    @Override
    public Response AgencyLogin(String phone, String pwd) {
        Passenger passenger = new Passenger();
        passenger.setAgency(agencyDAO.get(14));
        passenger.setName("1");
        passenger.setPhone("1");
        passenger.setIdcard("1");
        passenger.setSex(0);
        passengerDAO.save(passenger);
        new Thread(() -> {
            Passenger passenger1 = new Passenger();
            passenger1.setAgency(agencyDAO.get(14));
            passenger1.setName("2");
            passenger1.setPhone("2");
            passenger1.setIdcard("2");
            passenger1.setSex(1);
            passenger1.setName("2");
            passengerDAO.save(passenger1);
        }).start();

        Agency agency = agencyDAO.login(phone, pwd);
        if (agency == null) {
            return Response.ok(new Message(Message.CODE.AGENCY_LOGIN_FAILED)).header("EntityClass", "Message").build();
        } else {
            String name = agency.getName();
            String id = Integer.toString(agency.getId());
            agency.setToken(JwtUtils.createJWT(name, phone, id));//id作为个人签名
            return Response.ok(agency).header("EntityClass", "Agency").build();
        }
    }

    /**
     * 旅行社注册
     * 注册失败原因是电话号码注册过，无法对相同电话号码进行重复注册
     *
     * @param agency
     * @return
     * @throws RegisterException
     */
    @Override
    public Agency AgencyRegister(Agency agency) throws RegisterException, PhoneWrongException {
        String phone = agency.getPhone();
        if (!agencyDAO.checkPhone(phone)) {
            throw new RegisterException();//因为手机号已经注册过，所以显示注册失败
        } else {
            if (!passengerDAO.match(phone)) {
                throw new PhoneWrongException();//电话号码格式不对
            } else {
                agencyDAO.save(agency);
                return agency;
            }

        }
    }

    /**
     * 旅行社信息修改
     * 修改失败情况是电话号码为空或者是姓名为空
     *
     * @param agency
     * @return
     */
    @Override
    public Response modifyAgency(Agency agency) throws PhoneWrongException {
        if (!agencyDAO.complete(agency)) {
            return Response.ok(new Message(Message.CODE.AGENCY_MODIFY_FAILED)).header("EntityClass", "Message").build();
        } else {
            if (!passengerDAO.match(agency.getPhone())) {
                throw new PhoneWrongException();//电话号码格式不对
            } else {
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
     */
    @Override
    public Response BookingTicket(int agencyId, int historyId, int passengerId, int type) {
        /*Agency agency = agencyDAO.get(agencyId);
        if (agency == null) {
            return Response.ok(new Message(Message.CODE.AGENCY_NOT_EXISTED)).header("EntityClass", "Message").build();
        }
        History history = historyDao.get(historyId);
        if (history == null) {
            return Response.ok(new Message(Message.CODE.FLIGHT_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        Passenger passenger = passengerDAO.get(passengerId);
        if (passenger == null) {
            return Response.ok(new Message(Message.CODE.PASSENGER_NOT_EXIST)).header("EntityClass", "Message").build();
        }
        if (type != 0 && type != 1) {
            return Response.ok(new Message(Message.CODE.FLIGHT_SEAT_TYPE_ERROR)).header("EntityClass", "Message").build();
        }
        if (bookDAO.findBy("id", true, Restrictions.eq("history", history), Restrictions.eq("passenger", passenger)).size() > 0) {
            return Response.ok(new Message(Message.CODE.TICKET_HAS_EXIST)).header("EntityClass", "Message").build();
        }
        Book book = new Book();
        book.setPassenger(passenger);
        book.setOrderTime(new Timestamp(new Date().getTime()));
        book.setStatus(Book.BOOK_STATUS.BOOK_UNPAID);
        book.setSeatType(type);
        synchronized (bookDAO) {
            history = historyDao.get(historyId);
            book.setHistory(history);
            if (type == Book.SEAT_TYPE.BUSINESS_SEAT) {
                if (history.getBusinessNum() > 0) {
                    book.setSeatNum(history.getBusinessNum());
                    history.setBusinessNum(history.getBusinessNum() - 1);
                    historyDao.update(history);
                    bookDAO.save(book);
                    book = bookDAO.findBy("id", false, Restrictions.eq("passenger", passenger)).get(0);
                    return Response.ok(book).header("EntityClass", "Book").build();
                } else {
                    return Response.ok(new Message(Message.CODE.BOOK_FAILED)).header("EntityClass", "Message").build();
                }
            } else {
                if (history.getEconomyNum() > 0) {
                    book.setSeatNum(history.getEconomyNum());
                    history.setEconomyNum(history.getEconomyNum() - 1);
                    historyDao.update(history);
                    bookDAO.save(book);
                    book = bookDAO.findBy("id", false, Restrictions.eq("passenger", passenger)).get(0);
                    return Response.ok(book).header("EntityClass", "Book").build();
                } else {
                    return Response.ok(new Message(Message.CODE.BOOK_FAILED)).header("EntityClass", "Message").build();
                }
            }
        }*/
        return Response.ok(bookDAO.bookTicket(agencyId, historyId, passengerId, type)).header("EntityClass", "Message").build();
    }

    //取消订单
    @Override
    public Response cancelBook(int agencyId, int id) {
//        Book book = bookDAO.get(id);
//        if (book == null) {
//            return Response.ok(new Message(Message.CODE.TICKET_NOT_EXIST)).header("EntityClass", "Message").build();//取消订单失败
//        }
//        if (book.getPassenger().getAgency().getId() != agencyId) {
//            return Response.ok(new Message(Message.CODE.TICKET_NOT_EXIST)).header("EntityClass", "Message").build();//取消订单失败
//        }
        Book book = bookDAO.cancel(agencyId, id);
        if (book == null) {
            return Response.ok(new Message(Message.CODE.BOOK_CANCEL_FAILED)).header("EntityClass", "Message").build();//取消订单失败
        } else {
            return Response.ok(book).header("EntityClass", "Book").build();
        }
    }

    //付款
    @Override
    public Response payTicket(int agencyId, int id) throws TicketPayException {
        Book book = bookDAO.get(id);
        if (book == null) {
            return Response.ok(new Message(Message.CODE.TICKET_NOT_EXIST)).header("EntityClass", "Message").build();//取消订单失败
        }
        if (book.getPassenger().getAgency().getId() != agencyId) {
            return Response.ok(new Message(Message.CODE.TICKET_NOT_EXIST)).header("EntityClass", "Message").build();//取消订单失败
        }
        if (book.getStatus() != Book.BOOK_STATUS.BOOK_UNPAID) {
            return Response.ok(new Message(Message.CODE.TICKET_NOT_EXIST)).header("EntityClass", "Message").build();//取消订单失败
        }
        book = bookDAO.pay(id);
        if (book == null) {
            return Response.ok(new Message(Message.CODE.BOOK_PAY_FAILED)).header("EntityClass", "Message").build();
        } else {
            return Response.ok(book).header("EntityClass", "Book").build();
        }
    }

    //打印机票
    @Override
    public Response printTicket(int id, String IDCard) {
        Book book = bookDAO.get(id);
        if (book == null || !book.getPassenger().getIdcard().equals(IDCard)) {
            return Response.ok(new Message(Message.CODE.TICKET_NOT_EXIST)).header("EntityClass", "Message").build();
        } else if (book.getStatus() != Book.BOOK_STATUS.BOOK_SUCCESS) {
            return Response.ok(new Message(Message.CODE.REPEAT_PRINT)).header("EntityClass", "Message").build();
        } else {
            book.setStatus(Book.BOOK_STATUS.BOOK_PRINT);
            bookDAO.update(book);
            return Response.ok(book).header("EntityClass", "Book").build();
        }
    }

    @Override
    public List<Book> queryBookByPhone(String phone) {
        return bookDAO.query(phone);
    }

    @Override
    public List<Book> queryBookByAID(int agencyID, int status) {
        return bookDAO.query(agencyID, status);
    }

    @Override
    public List<Book> queryBookByFID(String flightID, int start, int end) {
        Date date1 = new Date(start);
        Date date2 = new Date(end);
        return bookDAO.query(flightID, date1, date2);
    }

    @Override
    public List<Book> queryBookByHID(int historyID) {
        return bookDAO.queryByHistoryID(historyID);
    }

    /**
     * 检验手机号注册时是否存在
     *
     * @param phone
     * @return
     */
    @Override
    public Response checkPhone(String phone) {
        if (!agencyDAO.checkPhone(phone)) {
            return Response.ok(new Message(Message.CODE.AGENCY_REGISTER_FAILED)).header("EntityClass", "Message").build();//因为手机号已经注册过，所以显示注册失败
        } else {
            return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
        }
    }

    /**
     * 剩余机票查询
     */
    @Override
    public List<History> queryBook(String startAirport, String endAirport, String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date time = sdf.parse(date);
        Date now = sdf.parse(sdf.format(new Date()));
        if (time.getTime() < now.getTime()) {
            return new ArrayList<>();
        }
        return historyDao.TicketQuery(startAirport, endAirport, time);
    }

    @Override
    public Response modifyPwd(String phone, String pwd1, String pwd2) {
        List<Agency> list = agencyDAO.findBy("phone", phone, "id", true);
        Agency agency = list.get(0);
        if (agency.getPwd().equals(pwd1)) {
            agency.setPwd(pwd2);
            agencyDAO.update(agency);
            return Response.ok(agency).header("EntityClass", "Agency").build();
        } else {
            return Response.ok(new Message(Message.CODE.PWD_IS_WRONG)).header("EntityClass", "Message").build();
        }
    }
}