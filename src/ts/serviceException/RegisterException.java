package ts.serviceException;

import ts.model.Message;

/**
 * Created by wr on 2017/6/19.
 */
public class RegisterException extends ServiceException {
    public RegisterException(){
        super("手机号已经注册过，注册失败");
        message1 = new Message(Message.CODE.AGENCY_REGISTER_FAILED);
    }
}
