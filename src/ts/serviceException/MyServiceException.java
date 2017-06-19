package ts.serviceException;

import ts.model.Message;

/**
 * 未知错误
 */
public class MyServiceException extends Exception{
    protected Message msg;
    public MyServiceException() {
        this("未知错误");
    }

    public MyServiceException(String message) {
        super(message);
        msg = new Message(Message.CODE.UNKNOWN_ERROR, message);
    }
}
