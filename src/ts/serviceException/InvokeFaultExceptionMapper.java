package ts.serviceException;

import ts.model.Message;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvokeFaultExceptionMapper implements ExceptionMapper {

    public Response toResponse(Throwable ex) {
        StackTraceElement[] trace = new StackTraceElement[1];
        trace[0] = ex.getStackTrace()[0];
        ex.setStackTrace(trace);
        ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.type("application/json;charset=UTF-8");

        System.err.println("\n\n-----------Error Message Start---------------\n\n");
        ex.printStackTrace();
        System.err.println("\n\n-----------Error Message End---------------\n\n");

        if (ex instanceof ClientErrorException){
            rb.entity(new Message(Message.CODE.URL_NOT_FOUND));
        }
        return rb.build();
    }
}  
