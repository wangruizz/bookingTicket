package ts.daoImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.dao.DataAccessException;
import ts.daoBase.BaseDao;
import ts.model.Book;
import ts.model.History;
import ts.model.Message;
import ts.model.Passenger;
import ts.serviceException.TicketPayException;
import ts.util.ShortMessage;

import javax.persistence.ParameterMode;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class BookDAO extends BaseDao<Book,Integer> {

    private PassengerDAO passengerDAO;
    private HistoryDao historyDao;
    private FlightDAO flightDAO;

    public FlightDAO getFlightDAO() {
        return flightDAO;
    }

    public void setFlightDAO(FlightDAO flightDAO) {
        this.flightDAO = flightDAO;
    }

    public void setHistoryDao(HistoryDao historyDao) {
        this.historyDao = historyDao;
    }

    public HistoryDao getHistoryDao() {
        return historyDao;
    }

    public PassengerDAO getPassengerDAO() {
        return passengerDAO;
    }

    public void setPassengerDAO(PassengerDAO passengerDAO) {
        this.passengerDAO = passengerDAO;
    }

    BookDAO() {
        super(Book.class);
    }

    public Message bookTicket(int agencyId, int historyId, int passengerId, int type) {
        Session session = this.getSessionFactory().openSession();
        ProcedureCall procedureCall = session.createStoredProcedureCall("book");
        procedureCall.registerParameter("aid", Integer.class, ParameterMode.IN).bindValue(agencyId);
        procedureCall.registerParameter("pid", Integer.class, ParameterMode.IN).bindValue(passengerId);
        procedureCall.registerParameter("hid", Integer.class, ParameterMode.IN).bindValue(historyId);
        procedureCall.registerParameter("type", Integer.class, ParameterMode.IN).bindValue(type);
        procedureCall.registerParameter("res", Integer.class, ParameterMode.OUT);
        int res;
        synchronized (this) {
            res = (int) procedureCall.getOutputs().getOutputParameterValue("res");
        }
        session.close();
        Message msg;
        switch (res) {
            case 0:  msg = new Message(Message.CODE.SUCCESS); break;
            case -1:  msg = new Message(Message.CODE.AGENCY_NOT_EXISTED); break;
            case -2:  msg = new Message(Message.CODE.PASSENGER_NOT_EXIST); break;
            case -3:  msg = new Message(Message.CODE.FLIGHT_NOT_EXIST); break;
            case -4:  msg = new Message(Message.CODE.FLIGHT_SEAT_TYPE_ERROR); break;
            case -5:  msg = new Message(Message.CODE.TICKET_HAS_EXIST); break;
            case -6:  msg = new Message(Message.CODE.FLIGHT_NOT_EMPTY); break;
            default: msg = new Message(Message.CODE.UNKNOWN_ERROR); break;
        }
        return  msg;
    }


    /**
     * 打印机票
     * 已经测试
     * @param bookID
     * @param idcard
     * @return
     */
    public Book printTicket(int bookID, String idcard) {
        Book book = super.get(bookID);
        if (null == book || book.getPassenger().getIdcard().equals(idcard)) {
            return null;
        }
        return book;
    }

    /**
     * 通过电话号码查询，已经验证
     * @param phone
     * @return
     */
    public List<Book> query(String phone) {
        List<Passenger> passengers = passengerDAO.queryByPhone(phone);
        List<Book> books = new ArrayList<>();
        passengers.forEach(passenger -> {
            List<Book> tmp = findBy("passenger", passenger, "id", true);
            books.addAll(tmp);
        });
        return books.size() == 0 ? null : books;
    }

    /**
     * 通过旅行社ID，订单状态查询
     * 已经验证
     * @param agencyID
     * @param status
     * @return
     */
    public List<Book> query(int agencyID, int status) {
        List<Passenger> passengers = passengerDAO.queryByID(agencyID);
        System.out.println("passengers"+passengers.size());
        List<Book> books = new ArrayList<>();
            passengers.forEach(passenger -> {
                List<Book> tmp = findBy("id", true,
                        Restrictions.eq("status", status),
                        Restrictions.eq("passenger", passenger));
                books.addAll(tmp);
            });
        return books.size() == 0 ? null : books;
    }

    /**
     * 通过航班ID和起止日期查询
     * dates数量如果为0则表示仅仅用flightID查询，为1表示单次记录查询，为2表示一段时间内查询
     *
     */
    public List<Book> query(String flightID, Date ... dates) {
        List<History> histories = new ArrayList<>();
        if (dates.length == 0) {
            histories = historyDao.queryHistory(flightID);
        } else
        if(dates.length == 1) {
            History history = historyDao.queryHistory(flightID, dates[0]);
            histories.add(history);
        } else
        if(dates.length == 2) {
            long startLong = Math.min(dates[0].getTime(), dates[1].getTime());
            long endLong = Math.max(dates[0].getTime(), dates[1].getTime());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(startLong));
            while (calendar.getTime().getTime() > endLong) {
                History tmp = historyDao.queryHistory(flightID, calendar.getTime());
                histories.add(tmp);
                calendar.add(Calendar.DATE, 1);
            }
        }
        List<Book> books = new ArrayList<>();
        histories.forEach(history -> {
            List<Book> tmp = findBy("historyID", history.getId(), "id", true);
            books.addAll(tmp);
        });
        return books;
    }

    /**
     * 通过历史表ID查询
     * 已经测试
     * @param historyID
     * @return
     */
    public List<Book> queryByHistoryID(int historyID) {
        History history = historyDao.get(historyID);
        return findBy("history", history, "id", true);
    }

    /**
     * 付款
     * 如果状态是已付款就抛出异常
     * @param bookID
     * @return
     */

    public Book pay(int bookID) throws TicketPayException {
        Book book = get(bookID);
        if (book == null) {
            return null;
        }
        if(book.getStatus()==Book.BOOK_STATUS.BOOK_SUCCESS){
                throw new TicketPayException();
            }else{
                book.setStatus(Book.BOOK_STATUS.BOOK_SUCCESS);
                update(book);
        }
//        由于免费条数有限，仅仅在必要测试时才取消注释代码
        ShortMessage shortMessage = ShortMessage.getInstance();
        String name = book.getPassenger().getName();
        Date date = book.getHistory().getDepartureDate();     //由于flightDAO还未获取，暂不实现此段代码
        String flightID = book.getHistory().getFlight().getId();
        String phone = book.getPassenger().getPhone();
        shortMessage.orderSuccess(name, date, flightID, bookID+"", phone);
        return book;
    }

    /**
     * 取消订单
     * @param bookID
     * @return
     */
    public Book cancel(int bookID) {
        Book book = get(bookID);
        book.setStatus(Book.BOOK_STATUS.BOOK_CANCEL);
        update(book);
        History history = book.getHistory();
        if (book.getSeatType() == Book.SEAT_TYPE.BUSINESS_SEAT) {
            history.setBusinessNum(history.getBusinessNum() + 1);
        } else {
            history.setEconomyNum(history.getEconomyNum() + 1);
        }
        historyDao.update(history);
        return book;
    }

    /**
     * 预订信息是否完整
     * 已经测试
     * @param book
     * @return
     */
    public Boolean complete(Book book){
        if(book.getHistory()==null||book.getSeatNum()==null||book.getSeatType()==null||book.getId()==null
                ||book.getOrderTime()==null||book.getPassenger()==null||book.getStatus()==null){
            return false;
        }else{
            return true;
        }
    }
}
