package ts.daoImpl;

import org.hibernate.criterion.Restrictions;
import ts.daoBase.BaseDao;
import ts.model.Agency;

import java.util.List;


public class AgencyDAO extends BaseDao<Agency,Integer>{

    AgencyDAO() {
        super(Agency.class);
    }

    //检测旅行社是否存在
    public boolean checkExisted(String phone) {
        List<Agency> agencies = findBy("phone", true, Restrictions.eq("phone", phone));
        return (agencies == null || agencies.size() == 0) ? true : false;
    }

    //登陆
    public Agency login(String phone, String pwd) {
        List<Agency> agencies = findBy("phone", true, Restrictions.eq("phone", phone));
        return (agencies != null && agencies.size() > 0) ? agencies.get(0) : null;
    }

    /**
     * 修改内容
     * @param agency
     * @return
     */
    public Agency modify(Agency agency) {
        update(agency);
        return agency;
    }
    public Boolean complete(Agency agency){
        if(agency.getName()==null||agency.getPhone()==null){
            return false;
        }else{
            return true;
        }

    }
}
