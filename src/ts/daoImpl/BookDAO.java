package ts.daoImpl;

import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import ts.daoBase.BaseDao;
import ts.model.Book;
import ts.model.Passenger;
import ts.util.ShortMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BookDAO extends BaseDao<Book,Integer> {

    private PassengerDAO passengerDAO;


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
    public List<Book> query(int agencyID, int status) {
        List<Passenger> passengers = passengerDAO.query(agencyID);
        List<Book> books = new ArrayList<>();
        passengers.forEach(passenger -> {
            List<Book> tmp = findBy("id", true,
                    Restrictions.eq("status", status),
                    Restrictions.eq("passID", passenger));
            books.addAll(tmp);
        });
        return books.size() == 0 ? null : books;
    }

    //通过航班ID查询
    public List<Book> query(int flightID) {

        return new ArrayList<>();
    }

    //付款
    public Boolean pay(int bookID) {
        Book book = get(bookID);
        if (book == null) {
            return false;
        }
        try{
            book.setStatus(1);
            update(book);
        } catch (DataAccessException e) {
            return false;
        }
        //由于免费条数有限，仅仅在必要测试时才取消注释代码
//        ShortMessage shortMessage = ShortMessage.getInstance();
//        String name = book.getPassenger().getName();
//        Date date = new Date();     //由于flightDAO还未获取，暂不实现此段代码
//        String flightID = book.getHistory().getFlight().getId();
//        String phone = book.getPassenger().getPhone();
//        shortMessage.orderSuccess(name, date, flightID, bookID+"", phone);
        return true;
    }

    //取消订单
    public Book cancel(int bookID) {
        Book book = get(bookID);
        book.setStatus(-1);
        update(book);
        return book;
    }

    //创建订单
    public Book create(int passengerID, int seatType, int flightID, Date date) {

        return new Book();
    }
}
