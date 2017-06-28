package ts.interceptor;


import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.XMLMessage;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import ts.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 拦截器，用于权限认证
 */
public class Authorization extends AbstractPhaseInterceptor<XMLMessage> {

    public Authorization() {
        super(Phase.PRE_INVOKE);//调用之前
    }

    @Override
    public void handleMessage(XMLMessage message) throws Fault {
        HttpServletRequest request = (HttpServletRequest) message.get("HTTP.REQUEST");
        HttpServletResponse response = (HttpServletResponse) message.get("HTTP.RESPONSE");
        String uri = (String) message.get(Message.REQUEST_URI);
        if (goNext(uri)) { //不是登录和注册
            String token = request.getHeader("token");
            try {
                if (token == null || token.length() == 0) { //没有token
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.write(new ts.model.Message(ts.model.Message.CODE.NO_TOKEN).toString());
                    //启动out interceptor
                    message.getInterceptorChain().doInterceptStartingAfter(message, "org.apache.cxf.jaxrs.interceptor.JAXRSOutInterceptor");
                    out.flush();
                } else {
                    try {
                        JwtUtils.parseJWT(token);
                        //token有效
                    } catch (Exception e) {
                        //token无效
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        out.write(new ts.model.Message(ts.model.Message.CODE.TOKEN_ERROR).toString());
                        out.flush();
                        message.getInterceptorChain().doInterceptStartingAfter(message, "org.apache.cxf.jaxrs.interceptor.JAXRSOutInterceptor");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean goNext(String uri) {
        String[] reg = {"^/CXF/REST/((Agency/AgencyRegister)|(Company/register))$",
                "^/CXF/REST/((Agency)|(Company))/doLogin/\\S*$",
                "^/CXF/REST/Company/fdasfgasdfgasd$",
                "^/CXF/REST/Agency/queryTicket/\\S*$"};
        for (String aReg : reg) {
            if (uri.matches(aReg)) {
                return false;
            }
        }
        return true;
    }
}
