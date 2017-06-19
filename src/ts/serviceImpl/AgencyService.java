package ts.serviceImpl;

import ts.daoImpl.AgencyDAO;
import ts.daoImpl.BookDAO;
import ts.daoImpl.PassengerDAO;
import ts.model.Agency;
import ts.model.Message;
import ts.model.Passenger;
import ts.serviceException.PassengerNotExistException;
import ts.serviceInterface.IAgencyService;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class AgencyService implements IAgencyService {

    private PassengerDAO passengerDAO;
    private AgencyDAO agencyDAO;
    private BookDAO bookDAO;

    public PassengerDAO getPassengerDAO() {
        return passengerDAO;
    }

    public void setPassengerDAO(PassengerDAO passengerDAO) {
        this.passengerDAO = passengerDAO;
    }

    public AgencyDAO getAgencyDAO() {
        return agencyDAO;
    }

    public void setAgencyDAO(AgencyDAO agencyDAO) {
        this.agencyDAO = agencyDAO;
    }

    public BookDAO getBookDAO() {
        return bookDAO;
    }

    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    //查找乘客
    @Override
    public List<Passenger> searchPassenger(int agencyID, String function, String parameter) throws PassengerNotExistException {
        List<Passenger> passengers = new ArrayList<>();
        switch (function) {
            case "name" :
                passengers = passengerDAO.queryByName(parameter, agencyID);
                if(passengers==null){
                    throw new PassengerNotExistException();
                }
                break;
            case "phone" :
                passengers = passengerDAO.queryByPhone(parameter, agencyID);
                if(passengers==null){
                    throw new PassengerNotExistException();
                }
                break;
            case "idcard" :
                Passenger passenger = passengerDAO.queryByIDCard(parameter, agencyID);
                passengers.add(passenger);
                if(passengers==null){
                    throw new PassengerNotExistException();
                }
                break;
        }
        return passengers;
    }
    /*
    * 修改失败的情况如下：
    * 1、信息不完整*/
    @Override
    public Response motifyPassenger(Passenger passenger) {
        if(passengerDAO.complete(passenger)==false){
            return Response.ok(new Message(Message.CODE.PASSENGER_INCOMPLICT)).header("Entityclass","Message").build();
        }else{
            passengerDAO.save(passenger);
            return Response.ok().header("EntityClass","Passenger").build();
        }
    }
    //添加乘客
    @Override
    public Response addPassenger(Passenger passenger) {
        if(passengerDAO.complete(passenger)==false){//如果乘客信息不完整
            return Response.ok(new Message(Message.CODE.PASSENGER_INCOMPLICT)).header("Entityclass","Message").build();
        }else{
            passengerDAO.save(passenger);
            return Response.ok().header("EntityClass","Passenger").build();
        }
    }

}
