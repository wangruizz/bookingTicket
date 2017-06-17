package ts.serviceImpl;

import ts.daoImpl.PassengerDAO;
import ts.model.Passenger;
import ts.serviceInterface.IAgencyService;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class AgencyService implements IAgencyService {

    private PassengerDAO passengerDAO;

    public PassengerDAO getPassengerDAO() {
        return passengerDAO;
    }

    public void setPassengerDAO(PassengerDAO passengerDAO) {
        this.passengerDAO = passengerDAO;
    }

    @Override
    public List<Passenger> searchPassenger(int agencyID, String function, String parameter) {
        List<Passenger> passengers = new ArrayList<>();
        switch (function) {
            case "name" :
                passengers = passengerDAO.queryByName(parameter, agencyID);
                break;
            case "phone" :
                passengers = passengerDAO.queryByPhone(parameter, agencyID);
                break;
            case "idcard" :
                Passenger passenger = passengerDAO.queryByIDCard(parameter, agencyID);
                passengers.add(passenger);
                break;
        }
        return passengers;
    }
}
