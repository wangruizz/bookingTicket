package ts.daoImpl;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import ts.daoBase.BaseDao;
import ts.model.Passenger;

import java.util.ArrayList;
import java.util.List;


public class PassengerDAO extends BaseDao<Passenger, Integer> {
    PassengerDAO() {
        super(Passenger.class);
    }

//    //增加旅客
//    public Passenger add(int agencyID, Passenger passenger) {
//
//        return new Passenger();
//    }

//    //删除旅客
//    public Boolean delete(int passengerID) {
//        removeById(passengerID);
//        return true;
//    }
//
//    //修改旅客信息
//    public Passenger modify(int passengerID) {
//
//        return new Passenger();
//    }

    //通过旅行社ID查询
    public List<Passenger> query(int agencyID) {
        List<Passenger> passengers = findBy("id", true, Restrictions.eq("agencyID", agencyID));
        return passengers;
    }

    //通过旅客姓名查询
    public List<Passenger> queryByName(String name, int agencyID) {
        List<Passenger> passengers = findBy("id", true, Restrictions.eq("agencyID", agencyID), Restrictions.like("name", name, MatchMode.ANYWHERE));
        return new ArrayList<>();
    }

    //通过旅客电话号查询
    public List<Passenger> queryByPhone(String phone, int agencyID) {
        List<Passenger> passengers = findBy("id", true, Restrictions.eq("agencyID", agencyID), Restrictions.eq("phone", phone));
        return passengers;
    }

    //通过旅客身份证号查询
    public Passenger queryByIDCard(String idCard, int agencyID) {
        List<Passenger> passengers = findBy("idcard", true, Restrictions.eq("agencyID", agencyID), Restrictions.eq("idcard", idCard));
        return (passengers != null && passengers.size() > 0) ? passengers.get(0) : null;
    }
}
