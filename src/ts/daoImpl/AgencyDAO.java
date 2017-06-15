package ts.daoImpl;

import ts.daoBase.BaseDao;
import ts.model.Agency;


public class AgencyDAO extends BaseDao<Agency,Integer>{

    AgencyDAO() {
        super(Agency.class);
    }

    //注册
    public Agency register(Agency agency) {

        return new Agency();
    }

    //登陆
    public Agency login(String phone, String pwd) {

        return new Agency();
    }

    //修改内容
    public Agency modify(Agency agency) {

        return new Agency();
    }

}
