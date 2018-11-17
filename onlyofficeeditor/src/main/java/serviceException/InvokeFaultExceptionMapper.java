package main.java.serviceException;

import java.util.Locale;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import main.java.entities.Message;

@Provider
public class InvokeFaultExceptionMapper implements ExceptionMapper {

    public Response toResponse(Throwable ex) {
        StackTraceElement[] trace = new StackTraceElement[1];
        trace[0] = ex.getStackTrace()[0];
        ex.setStackTrace(trace);
        ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.type("application/json;charset=UTF-8");

//        if (ex instanceof HibernateObjectRetrievalFailureException) {//自定义的异常类
//            HibernateObjectRetrievalFailureException e = (HibernateObjectRetrievalFailureException) ex;
//            rb.entity(e.getMessage());
//        }else if (ex instanceof ServiceException) {//自定义的异常类
//            ServiceException e = (ServiceException) ex;
//            rb.entity(e.getMessage());
//        }else{
//            rb.entity(ex);
//        }
        System.err.println("\n\n-----------Error Message Start---------------\n\n");
        ex.printStackTrace();
        System.err.println("\n\n-----------Error Message End---------------\n\n");

        rb.entity(new Message(Message.CODE.UNKNOWN_ERROR));//向前端屏蔽错误信息
        rb.language(Locale.SIMPLIFIED_CHINESE);
        return rb.build();
    }
}  
