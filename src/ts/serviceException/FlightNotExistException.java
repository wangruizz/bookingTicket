package ts.serviceException;

import ts.model.Message;

public class FlightNotExistException extends ServiceException {
    public FlightNotExistException() {
        super("航班不存在");
        message1 = new Message(Message.CODE.FLIGHT_NOT_EXIST);
    }
}
