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

    /**
     * 登录验证
     * @param userName
     * @param pwd
     * @return
     */
    public Company login(String userName,String pwd){
        List<Company> users = findBy("username", true, Restrictions.eq("username", userName),Restrictions.eq("pwd", pwd));
        System.out.println("users"+users);
        return users != null && users.size() > 0 ? users.get(0) : null;
    }

    /**
     * 修改公司信息
     * @param company
     * @return
     */
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
        List<Company> list = findBy("username", companyUserName, "username", true);
        return list.size() > 0;
    }
}
