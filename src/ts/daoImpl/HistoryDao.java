package ts.daoImpl;

import org.hibernate.criterion.Restrictions;
import ts.daoBase.BaseDao;
import ts.model.Airport;
import ts.model.Book;
import ts.model.Flight;
import ts.model.History;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 12556 on 2017/6/15.
 */
public class HistoryDao extends BaseDao<History,Integer> {

    private BookDAO bookDAO;
    private FlightDAO flightDAO;
    private AirportDAO airportDAO;

    public AirportDAO getAirportDAO() {
        return airportDAO;
    }

    public void setAirportDAO(AirportDAO airportDAO) {
        this.airportDAO = airportDAO;
    }

    public FlightDAO getFlightDAO() {
        return flightDAO;
    }

    public void setFlightDAO(FlightDAO flightDAO) {
        this.flightDAO = flightDAO;
    }

    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public BookDAO getBookDAO() {
        return bookDAO;
    }

    public HistoryDao() {
        super(History.class);
    }
//    /**
//     * 某天某个飞机某个类型的剩余座位数
//     * @param flightID
//     * @param departureDate
//     * @param type
//     * @return
//     */
//    public int queryRemain(String flightID, Date departureDate,int type){
//        Flight flight = flightDAO.get(flightID);//获取对应的航班
//
//        if (type == Book.SEAT_TYPE.ECONOMY_SEAT){
//            return flight.getEconomyNum() - this.queryBookedEconomy(flightID,departureDate);
//        }else {
//            return flight.getEconomyNum() - queryBookedBusiness(flightID,departureDate);
////            return flight.getEconomyNum();
//        }
//    }
//
//    /**
//     * 查询某天某个航班预订成功的的经济舱座位
//     * @param flightID
//     * @param departureDate
//     * @return
//     */
//    private int queryBookedEconomy(String flightID, Date departureDate){
//
//        List<History> histories = findBy("id",true,
//                Restrictions.eq("flightID",flightID),
//                Restrictions.eq("departureDate",departureDate));
//        if (histories != null && histories.size() > 0){
//            List<Book> books = bookDAO.query(histories.get(0).getId(),Book.BOOK_STATUS.BOOK_SUCCESS);
//            int sum = 0;//经济舱总数
//            for(Book book: books) {
//                if (book.getSeatType() == Book.SEAT_TYPE.ECONOMY_SEAT){
//                    sum++;
//                }
//            }
//            return sum;
//        }
//        return 0;
//    }
//
//    /**
//     * 查询剩余的商务舱座位
//     * @param flightID
//     * @param departureDate
//     * @return
//     */
//    private int queryBookedBusiness(String flightID, Date departureDate){
//        Flight flight = flightDAO.get(flightID);
//        List<History> histories = findBy("id",true,
//                Restrictions.eq("flight",flight),
//                Restrictions.eq("departureDate",departureDate));
//        if (histories != null && histories.size() > 0){
//            List<Book> books = bookDAO.query(histories.get(0).getId(),Book.BOOK_STATUS.BOOK_SUCCESS);
//            int sum = 0;//商务舱总数
//            for(Book book: books) {
//                if (book.getSeatType() == Book.SEAT_TYPE.BUSINESS_SEAT){
//                    sum++;
//                }
//            }
//            return sum;
//        }
//        return 0;
//    }
    /**
     * 查询history ID
     * 已经测试
     * @return
     */
    public int queryID(String flightID,Date departureDate){
        Flight flight = flightDAO.get(flightID);
        List<History> histories  = findBy("id",true, Restrictions.eq("flight",flight),Restrictions.eq("departureDate",departureDate));
        if (histories.size() < 1){
            return 0;
        }else {
            return histories.get(0).getId();//返回第一条数据的ID
        }
    }

    /**
     * 已经测试
     * @param flightID
     * @return
     */
    public List<History> queryHistory(String flightID){
        Flight flight = flightDAO.get(flightID);
        return findBy("id",true, Restrictions.eq("flight",flight));
    }

    /**
     * 查询历史
     * 已经测试
     * @param flightID
     * @param departureDates
     * @return
     */
    public History queryHistory(String flightID,Date departureDates){
        Flight flight = flightDAO.get(flightID);
        List<History> histories;
        Date date = departureDates;
        histories = findBy("id",true, Restrictions.eq("flight",flight),Restrictions.eq("departureDate",date));

        if (histories.size() < 1){
            return null;
        }else {
            return histories.get(0);//返回第一条数据
        }
    }

    /**
     *增加一条历史
     * 已经测试
     * @return
     */
    public History add(String flightID, java.sql.Date departureDate){
        Flight flight = flightDAO.get(flightID);//获取对应的航班
        System.out.println("历史dao中增加历史方法——获取的航班信息是："+flight.toString());
        History history  = new History();
        history.setStatus(History.STATUS.HISTORY_FLIGHT_NOMAL);
        history.setDepartureDate(departureDate);
        history.setFlight(flight);
        history.setBusinessNum(flight.getBusinessNum());
        history.setEconomyNum(flight.getEconomyNum());
        Time time = java.sql.Time.valueOf("00:00:00");
        history.setDelayTime(time);
        System.out.println(history.toString());
        save(history);

        return history;
    }

    /**
     *修改历史信息
     * 已经测试
     * @return
     */
    public History modify(History history){
        update(history);
        return history;
    }


    /**
     * 航班延误
     * 已经测试
     * @param flightID
     * @return
     */
    public boolean delay(String flightID, Date departureDate, Time delayTime) {
        Flight flight = flightDAO.get(flightID);
        List<History> histories = findBy("id",true,Restrictions.eq("flight",flight),Restrictions.eq("departureDate",departureDate));
        if (histories != null && histories.size() >= 1){//有该天该航航班的信息
            History history = histories.get(0);
            history.setStatus(History.STATUS.HISTORY_FLIGHT_DELAY);
            history.setDelayTime(delayTime);
            update(history);
            return  true;
        }
        return  false;
    }
    /**
     * 查询剩余机票数量
     * 已经测试
     */
    public ArrayList<History> TicketQuery(String startAirport, String endAirport, Date date){
        Airport start = airportDAO.get(startAirport);
        Airport end = airportDAO.get(endAirport);
        if (start == null || end == null) {
            return new ArrayList<>();
        }
        List<Flight> list = flightDAO.findBy("id",true,Restrictions.eq("startAirport",start),Restrictions.eq("arriveAirport", end));
        ArrayList<History> list1 = new ArrayList<>();
        Time now = new Time(System.currentTimeMillis());
        for (Flight i:list) {
//            if (i.getStartTime().getTime() < now.getTime()) {
//                continue;
//            }
            List<History> list2 = findBy("id",true,Restrictions.eq("departureDate",date),Restrictions.eq("flight",i));
            if(list2.size()>0){
                list1.add(list2.get(0));
            }
        }
        return list1;
    }

    /**
     * 取消某天某个ID的航班
     * @param history 某天某个ID对应的历史表记录
     */
    public void cancelFlight(History history) {
        history.setStatus(History.STATUS.HISTORY_FLIGHT_CANCEL);
        update(history);
    }

    /**
     * 恢复某天某个ID的航班
     * @param history 某天某个ID对应的历史表记录
     */
    public void resumeFlight(History history) {
        history.setStatus(History.STATUS.HISTORY_FLIGHT_NOMAL);
        update(history);
    }


}
