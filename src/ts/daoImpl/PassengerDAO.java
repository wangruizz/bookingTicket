package ts.daoImpl;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import ts.daoBase.BaseDao;
import ts.model.Agency;
import ts.model.Passenger;

import java.util.ArrayList;
import java.util.List;


public class PassengerDAO extends BaseDao<Passenger, Integer> {
    PassengerDAO() {
        super(Passenger.class);
    }
    private  AgencyDAO agencyDAO;

    public AgencyDAO getAgencyDAO() {
        return agencyDAO;
    }

    public void setAgencyDAO(AgencyDAO agencyDAO) {
        this.agencyDAO = agencyDAO;
    }
    /**
     * 通过旅行社ID查询
     * 已经测试
     * @param agencyID
     * @return
     */
    public List<Passenger> queryByID(int agencyID) {
        List<Agency> agency = agencyDAO.findBy("id",agencyID,"id",true);
        return findBy("id", true, Restrictions.eq("agency", agency.get(0)));
    }

    /**
     * 通过旅客姓名查询
     * 已经更改，并测试
     * @param name
     * @param agencyID
     * @return
     */
    public List<Passenger> queryByName(String name, int agencyID) {
        Agency agency = agencyDAO.get(agencyID);
        List<Passenger> passengers = findBy("id", true, Restrictions.eq("agency", agency), Restrictions.like("name", name, MatchMode.ANYWHERE));
        return passengers.size()>0?passengers:null;
    }

    /**
     * 通过旅客电话号和旅行社ID查询
     * 已经更改并测试
     * @param phone
     * @param agencyID
     * @return
     */
    public List<Passenger> queryByPhone(String phone, int agencyID) {
        Agency agency = agencyDAO.get(agencyID);
        List<Passenger> passengers = findBy("id", true, Restrictions.eq("agency", agency),Restrictions.eq("phone", phone));
        return passengers.size()>0?passengers:null;
    }

    /**
     * 通过旅客电话号查询
     * 已经更改并测试
     * @param phone
     * @return
     */
    public List<Passenger> queryByPhone(String phone) {
        List<Passenger> passengers = findBy("id", true, Restrictions.eq("phone", phone));
        return passengers.size()>0?passengers:null;
    }

    /**
     * 通过旅客身份证号查询
     * 已经更改并测试
     * @param idCard
     * @param agencyID
     * @return
     */
    public Passenger queryByIDCard(String idCard, int agencyID) {
        Agency agency = agencyDAO.get(agencyID);
        List<Passenger> passengers = findBy("idcard", true, Restrictions.eq("agency", agency), Restrictions.eq("idcard", idCard));
        return (passengers != null && passengers.size() > 0) ? passengers.get(0) : null;
    }

    /**
     *旅客信息是否完整
     * 已经测试
     */
    public Boolean complete(Passenger passenger){
        if(passenger.getAgency()==null||passenger.getId()==null||passenger.getIdcard()==null||passenger.getName()==null||passenger.getPhone()==null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 电话号码是否符合要求
     * 已经测试
     * @param phone
     * @return
     */
    public Boolean match(String phone){
        if(phone.matches("^((13[0-9])|(15[^4])|(18[0235-9])|(17[0-8])|(147))\\d{8}$") ||
                phone.matches("^(5|6|8|9)\\d{7}$")){
            return true;
        }else{
            return false;
        }
    }
}
