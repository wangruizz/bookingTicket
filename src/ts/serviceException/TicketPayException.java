package ts.serviceException;

import ts.model.Message;

public class TicketPayException extends ServiceException {
    public TicketPayException() {
        super("已付款");
        message1 = new Message(Message.CODE.TICKET_HAS_PAYED);
    }
}
