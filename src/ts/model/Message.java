package ts.model;

import org.json.JSONObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回信息的类
 */
@XmlRootElement(name="Message")
public class Message {
    private static Map<Integer, String> map = new HashMap<>();
    private int code;
    private String msg;

    public static Map<Integer, String> getMap() {
        return map;
    }

    public int getCode() {
        return code;
    }

    @XmlElement
    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    @XmlElement
    private void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        return jsonObject.toString();
    }

    public Message(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Message(int code) {
        this.code = code;
        this.msg = map.get(code);
    }

    public Message() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        if (code != message.code) return false;
        return msg != null ? msg.equals(message.msg) : message.msg == null;
    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        return result;
    }

    public static final class CODE{
        public static final int URL_NOT_FOUND = -2;
        public static final int UNKNOWN_ERROR = -1;
        public static final int SUCCESS = 1;
        public static final  int PARAM_UNCOMPLTED = -3;//参数不完整

        //与token有关的信息 1000 - 1999
        public static final int NO_TOKEN = 1000;
        public static final int TOKEN_ERROR = 1001;

        //登录失败
        public static final int LOGIN_FAILED = 2000;

        //与Company有关的信息 5000 - 5999
        public static  final int COMPANT_HAS_EXIST = 5000;



    }

    static {
        map.put(CODE.COMPANT_HAS_EXIST,"该用户名已经存在");

    }
}
