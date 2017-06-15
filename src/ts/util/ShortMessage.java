package ts.util;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.eclipse.jetty.util.ajax.JSON;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ab on 2017/6/14.
 */
public class ShortMessage {

    //官网的URL, http请求就用这个链接
    private final String url = "http://gw.api.taobao.com/router/rest?format=json";
//    private String url = "http://gw.api.tbsandbox.com/router/rest";
    //成为开发者，创建应用后系统自动生成
    private final String appkey = "24292629";
    private final String secret = "b0c8abe213bf3d6538c324285842ba12";
    private static volatile ShortMessage instance = null;
    private final String TEMPLATE_ORDER_SUCCESS = "SMS_71330113";
    private SimpleDateFormat sdf = null;


    public static ShortMessage getInstance() {
        if (null == instance) {
            synchronized (ShortMessage.class) {
                if (null == instance) {
                    instance = new ShortMessage();
                }
            }
        }
        return instance;
    }

    ShortMessage() {
        sdf = new SimpleDateFormat("yy/MM/dd HH:mm");
    }

    private boolean sendMessage(String templateCode, String content, String teleNumber) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setSmsType("normal");
        req.setSmsFreeSignName("机票预订系统");
        req.setSmsParamString(content);
        req.setRecNum(teleNumber);
        req.setSmsTemplateCode(templateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);

        try {
            JSONObject ans = new JSONObject(rsp.getBody());
            JSONObject success = ans.getJSONObject("alibaba_aliqin_fc_sms_num_send_response");
            if (success == null) {
                JSONObject error = ans.getJSONObject("error_response");
                if (error == null) {
                    throw new JSONException("no error");
                }
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println(rsp.getBody());
        return true;
    }

    public boolean orderSuccess(String name, Date time, String flightID, String orderID, String teleNumber) {
        String sendTime = sdf.format(time);
        String content = "{"
                + "\"name\":\"" + name + "\","
                + "\"time\":\"" + sendTime + "\","
                + "\"flightID\":\"" + flightID + "\","
                + "\"number\":\"" + orderID + "\""
                + "}";
//        System.out.println(content);
        try {
            sendMessage(TEMPLATE_ORDER_SUCCESS, content, teleNumber);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return true;
    }

//    public static void main(String[] args) {
//        ShortMessage shortMessage = ShortMessage.getInstance();
//        System.out.println(shortMessage.orderSuccess("王博文", new Date(1497598287000l), "XG3302", "2942358238", "13069977657"));
//    }

}
