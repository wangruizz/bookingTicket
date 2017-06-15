package ts.daoImpl;

import org.hibernate.criterion.Restrictions;
import ts.daoBase.BaseDao;
import ts.model.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BookDAO extends BaseDao<Book,Integer> {
    BookDAO() {
        super(Book.class);
    }


    //打印机票
    public Book printTicket(int bookID, String idcard) {
        Book book = super.get(bookID);
//        if (null == book || book.get) {
//            return null;
//        }
        return book;
    }

    //通过电话查询
    public List<Book> query(String phone) {

        return new ArrayList<>();
    }

    //通过旅行社ID，订单状态查询
    public List<Book> query(int agencyID, int status) {

        return new ArrayList<>();
    }

    //通过航班ID查询
    public List<Book> query(int flightID) {

        return new ArrayList<>();
    }

    //付款
    public Boolean pay(int bookID) {

        return true;
    }

    //取消订单
    public Book cancel(int bookID) {

        return new Book();
    }

    //创建订单
    public Book create(int passengerID, int seatType, int flightID, Date date) {

        return new Book();
    }
}
