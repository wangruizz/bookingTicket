package ts.daoImpl;

import ts.daoBase.BaseDao;
import ts.model.Book;
import ts.model.History;

import java.util.Date;
import java.util.List;

/**
 * Created by 12556 on 2017/6/15.
 */
public class HistoryDao extends BaseDao<History,Integer> {
    /**
     * 某天某个飞机某个类型的剩余座位数
     * @param flightID
     * @param departureDate
     * @param type
     * @return
     */
    public int queryRemain(String flightID, Date departureDate,int type){
        List<History> histories = findBy()
    }

    /**
     * 查询history ID
     * @return
     */
    public int queryID(String flightID,Date departureDate){

    }

    /**
     *
     * @return
     */
    public History add(String flightID,Date departureDate){

    }

    /**
     *
     * @return
     */
    public History modify(History history){

    }





}
