package ts.daoImpl;

import ts.daoBase.BaseDao;
import ts.model.Airport;

/**
 * Created by wr on 2017/6/13.
 */
public class AirportDAO extends BaseDao<Airport,String>{
    public AirportDAO(){
        super(Airport.class);
    }
//    public List<Airport> getByID(int id){
//        List<Airport> list;
//        list = findBy("id",id,"id",true);
//        return list;
//    }

}
