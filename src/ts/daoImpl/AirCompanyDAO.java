package ts.daoImpl;

import org.hibernate.criterion.Restrictions;
import ts.daoBase.BaseDao;
import ts.model.Company;

import java.util.List;

/**
 * Created by 12556 on 2017/6/15.
 */
public class AirCompanyDAO extends BaseDao<Company,String> {

    public AirCompanyDAO() {
        super(Company.class);
    }


    public Company register(Company company){
        if (!checkHasExist(company.getUsername())){
            save(company);
            return  company;
        }else {
            return get(company.getUsername());
        }
    }

    public Company login(String userName,String pwd){
        List<Company> users = findBy("username", true, Restrictions.eq("username", userName),Restrictions.eq("pwd", pwd));
       // List<Company> users = findBy("username", true, Restrictions.eq("username", userName));
        System.out.println("users"+users);
        return users != null && users.size() > 0 ? users.get(0) : null;
    }

    public Company modify(Company company){
        update(company);
        return company;
    }

    /**
     * 检查该userName的公司是否存在
     * @param companyUserName
     * @return
     */
    public boolean checkHasExist(String companyUserName){
        Company company1 = get(companyUserName);
        return company1 != null;
    }
}
