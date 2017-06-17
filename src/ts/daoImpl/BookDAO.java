package ts.daoImpl;

import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import ts.daoBase.BaseDao;
import ts.model.Book;
import ts.model.History;
import ts.model.Passenger;
import ts.util.ShortMessage;

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


    //打印机票
    public Book printTicket(int bookID, String idcard) {
        Book book = super.get(bookID);
        if (null == book || book.getPassenger().getIdcard() != idcard) {
            return null;
        }
        return book;
    }

    //通过电话查询
    public List<Book> query(String phone) {
        List<Passenger> passengers = passengerDAO.queryByPhone(phone);
        List<Book> books = new ArrayList<>();
        passengers.forEach(passenger -> {
            List<Book> tmp = findBy("passID", passenger, "id", true);
            books.addAll(tmp);
        });
        return books.size() == 0 ? null : books;
    }

    //通过旅行社ID，订单状态查询
    public List<Book> query(int agencyID, int ... status) {
        List<Passenger> passengers = passengerDAO.queryByID(agencyID);
        List<Book> books = new ArrayList<>();
        if (status.length == 1) {
            passengers.forEach(passenger -> {
                List<Book> tmp = findBy("id", true,
                        Restrictions.eq("status", status[0]),
                        Restrictions.eq("passID", passenger));
                books.addAll(tmp);
            });
        } else {
            passengers.forEach(passenger -> {
                List<Book> tmp = findBy("id", true,
                        Restrictions.eq("passID", passenger));
                books.addAll(tmp);
            });
        }

        return books.size() == 0 ? null : books;
    }

    /**
     * 通过航班ID和起止日期查询
     * dates数量如果为0则表示仅仅用flightID查询，为1表示单次记录查询，为2表示一段时间内查询
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

    //通过历史表ID查询
    public List<Book> queryByHistoryID(int historyID) {
        History history = historyDao.get(historyID);
        List<Book> books = findBy("historyID", history, "id", true);
        return books;
    }

    //付款
    public Boolean pay(int bookID) {
        Book book = get(bookID);
        if (book == null) {
            return false;
        }
        try{
            book.setStatus(Book.BOOK_STATUS.BOOK_SUCCESS);
            update(book);
        } catch (DataAccessException e) {
            return false;
        }
        //由于免费条数有限，仅仅在必要测试时才取消注释代码
//        ShortMessage shortMessage = ShortMessage.getInstance();
//        String name = book.getPassenger().getName();
//        Date date = historyDao.get(book.getId()).getDepartureDate();     //由于flightDAO还未获取，暂不实现此段代码
//        String flightID = book.getHistory().getFlight().getId();
//        String phone = book.getPassenger().getPhone();
//        shortMessage.orderSuccess(name, date, flightID, bookID+"", phone);
        return true;
    }

    //取消订单
    public Book cancel(int bookID) {
        Book book = get(bookID);
        book.setStatus(Book.BOOK_STATUS.BOOK_CANCEL);
        update(book);
        return book;
    }

//    save
//    //创建订单
//    public Book create(int passengerID, int seatType, int flightID, Date date) {
//
//        return new Book();
//    }

//    public static int test(int ... sum) {
//        if (sum == null) {
//            return 1;
//        } else if (sum.length == 0) {
//            return 2;
//        }
//        return 3;
//    }
//
//    public static void main(String[] args) {
//        System.out.println(test());
//    }
}
