package ts.serviceException;

import ts.model.Message;

/**
 * Created by wr on 2017/6/20.
 */
public class PhoneWrongException extends ServiceException {
    public PhoneWrongException(){
        super("电话号码格式不对");
        message1 = new Message(Message.CODE.PHONE_IS_WRONG);
    }

}
