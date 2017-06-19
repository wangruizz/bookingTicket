package ts.serviceException;

import ts.model.Message;

/**
 * Created by wr on 2017/6/19.
 */
public class PassengerNotExistException extends ServiceException {
    public PassengerNotExistException() {
        super("passenger not exist");
        message1 = new Message(Message.CODE.PASSENGER_NOT_EXIST);
    }
}
