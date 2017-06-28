package ts.serviceException;

import ts.model.Message;

public class ServiceException extends Exception {
    protected Message message1;

    public ServiceException(String message) {
        super(message);
        message1 = new Message(Message.CODE.UNKNOWN_ERROR, message);
    }

    public ServiceException() {
        this("未知错误");
    }
}
