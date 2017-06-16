package ts.daoImpl;

import org.hibernate.criterion.Restrictions;
import ts.daoBase.BaseDao;
import ts.model.Company;

import java.util.List;

/**
 * Created by 12556 on 2017/6/15.
 */
public class AirCompanyDAO extends BaseDao<Company,String> {

    public AirCompanyDAO(Class<Company> type) {
        super(type);
    }

    public AirCompanyDAO() {
    }

    public Company register(Company company){
        Company companyExist = checkHasExist(company.getUsername());
        if (companyExist == null){
            save(company);
            return  company;
        }else {
            return companyExist;
        }
    }

    public Company login(String userName,String pwd){
        List<Company> users = findBy("username", true, Restrictions.eq("username", userName),Restrictions.eq("pwd", pwd));
        return users != null && users.size() > 0 ? users.get(0) : null;
    }

//    public Company modify(Company company){
//        update(company);
//        return company;
//    }

    /**
     * 检查该userName的公司是否存在
     * @param companyUserName
     * @return
     */
    public Company checkHasExist(String companyUserName){
        Company company1 = get(companyUserName);
        if (company1 != null){//已经存在
            return company1;
        }else{
            return null;
        }
    }
}
