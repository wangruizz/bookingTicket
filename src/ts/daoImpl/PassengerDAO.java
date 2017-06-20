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
    public List<Passenger> queryByID(int agencyID) {
        List<Passenger> passengers = findBy("id", true, Restrictions.eq("id", agencyID));
        return passengers;
    }

    //通过旅客姓名查询
    public List<Passenger> queryByName(String name, int agencyID) {
        List<Passenger> passengers = findBy("id", true, Restrictions.eq("id", agencyID), Restrictions.like("name", name, MatchMode.ANYWHERE));
        return new ArrayList<>();
    }

    //通过旅客电话号和旅行社ID查询
    public List<Passenger> queryByPhone(String phone, int agencyID) {
        List<Passenger> passengers = findBy("id", true, Restrictions.eq("id", agencyID), Restrictions.eq("phone", phone));
        return passengers;
    }

    //通过旅客电话号查询
    public List<Passenger> queryByPhone(String phone) {
        List<Passenger> passengers = findBy("id", true, Restrictions.eq("phone", phone));
        return passengers;
    }

    //通过旅客身份证号查询
    public Passenger queryByIDCard(String idCard, int agencyID) {
        List<Passenger> passengers = findBy("idcard", true, Restrictions.eq("id", agencyID), Restrictions.eq("idcard", idCard));
        return (passengers != null && passengers.size() > 0) ? passengers.get(0) : null;
    }
    //乘客信息是否完整(性别有默认？为男？)
    public Boolean complete(Passenger passenger){
        if(passenger.getAgency()==null||passenger.getId()==null||passenger.getIdcard()==null||passenger.getName()==null||passenger.getPhone()==null){
            return false;
        }else{
            return true;
        }
    }
    public Boolean match(String phone){
        if(phone.matches("^((13[0-9])|(15[^4])|(18[0235-9])|(17[0-8])|(147))\\d{8}$") ||
                phone.matches("^(5|6|8|9)\\d{7}$")){
            return true;
        }else{
            return false;
        }
    }
}
