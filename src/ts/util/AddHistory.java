package ts.util;

import org.hibernate.criterion.Restrictions;
import ts.daoImpl.FlightDAO;
import ts.daoImpl.HistoryDao;
import ts.model.Flight;
import ts.model.History;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 每天自动往history表中插入预售期内的航班
 *
 */
public class AddHistory {
    private HistoryDao historyDao;
    private FlightDAO flightDAO;

    public HistoryDao getHistoryDao() {
        return historyDao;
    }

    public void setHistoryDao(HistoryDao historyDao) {
        this.historyDao = historyDao;
    }

    public FlightDAO getFlightDAO() {
        return flightDAO;
    }

    public void setFlightDAO(FlightDAO flightDAO) {
        this.flightDAO = flightDAO;
    }
    private int period = 30; //预售期

    public void init() {
        new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<Flight> flights = flightDAO.findBy("id", true, Restrictions.eq("status", Flight.STATUS.FLIGHT_NORMAL));
            Time delay = new Time(0, 0, 0); //默认航班延迟0
            flights.forEach(flight -> {
                java.util.Date now = null;
                try {
                    now = sdf.parse(sdf.format(new java.util.Date()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                now = DateProcess.getNext(now, -1 *period);
                Date date = new Date(now.getTime());
                for (int i = period; i >= 0; i--) { //把预售期内的全部的航班插入到history表中
                    History history = historyDao.queryHistory(flight.getId(), date);
                    if (history == null) { //history表中没有记录
                        history = new History();
                        history.setFlight(flight);
                        history.setDepartureDate(date);
                        history.setDelayTime(delay);
                        history.setStatus(History.STATUS.HISTORY_FLIGHT_NOMAL);
                        history.setBusinessNum(flight.getBusinessNum());
                        history.setEconomyNum(flight.getEconomyNum());
                        historyDao.save(history);
                        now = DateProcess.getNext(now, -1);
                        date = new Date(now.getTime());
                    }else {
                        continue;//后面的已经存在，前面的就一定存在
                    }
                }
            });
            System.out.println("history表初始化结束");
            while (true) {
                java.util.Date now = new java.util.Date();
                java.util.Date next = DateProcess.getNext(now, 1);
                try {
                    Thread.sleep(next.getTime() - now.getTime()); //每天更新history
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flights = flightDAO.findBy("id", true, Restrictions.eq("status", Flight.STATUS.FLIGHT_NORMAL));
                flights.forEach(flight -> {
                    History history = new History();
                    history.setFlight(flight);
                    history.setDepartureDate(new Date(next.getTime()));
                    history.setDelayTime(delay);
                    history.setStatus(History.STATUS.HISTORY_FLIGHT_NOMAL);
                    history.setBusinessNum(flight.getBusinessNum());
                    history.setEconomyNum(flight.getEconomyNum());
                    historyDao.save(history);
                });
            }
        }).start();
    }
}
