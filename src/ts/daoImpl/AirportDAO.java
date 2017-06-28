package ts.daoImpl;

import ts.daoBase.BaseDao;
import ts.model.Airport;

public class AirportDAO extends BaseDao<Airport,String>{
    public AirportDAO(){
        super(Airport.class);
    }
}
