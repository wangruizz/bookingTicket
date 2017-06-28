package ts.serviceException;

import ts.model.Message;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.text.ParseException;

@Provider
public class InvokeFaultExceptionMapper implements ExceptionMapper {

    public Response toResponse(Throwable ex) {
        StackTraceElement[] trace = new StackTraceElement[1];
        trace[0] = ex.getStackTrace()[0];
        ex.setStackTrace(trace);
        ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.type("application/json;charset=UTF-8");

        System.err.println("\n\n-----------Error Message Start---------------\n\n");
        System.out.println(ex.getClass());
        ex.printStackTrace();
        System.err.println("\n\n-----------Error Message End---------------\n\n");

        if (ex instanceof ParseException) {
            rb.entity(new Message(Message.CODE.DATE_FORMAT_ERROR));
        } else if (ex instanceof ServiceException) {
            rb.entity(((ServiceException) ex).message1);
        } else if (ex instanceof BadRequestException) {
            rb.entity(new Message(Message.CODE.BAD_REQUEST));
        } else if (ex instanceof ClientErrorException) {
            rb.entity(new Message(Message.CODE.URL_NOT_FOUND));
        }
        return rb.build();
    }
}