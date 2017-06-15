package ts.daoImpl;

import ts.daoBase.BaseDao;
import ts.model.Passenger;

import java.util.ArrayList;
import java.util.List;


public class PassengerDAO extends BaseDao<Passenger, Integer> {
    PassengerDAO() {
        super(Passenger.class);
    }

    //增加旅客
    public Passenger add(int agencyID, Passenger passenger) {

        return new Passenger();
    }

    //删除旅客
    public Boolean delete(int passengerID) {

        return true;
    }

    //修改旅客信息
    public Passenger modify(int passengerID) {

        return new Passenger();
    }

    //通过旅行社ID查询
    public List<Passenger> query(int agencyID) {

        return new ArrayList<>();
    }

    //通过旅客姓名查询
    public List<Passenger> query(String name) {

        return new ArrayList<>();
    }
}
