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

        //与token有关的信息 1000 - 1999
        public static final int NO_TOKEN = 1000;
        public static final int TOKEN_ERROR = 1001;

        //与Company有关的信息 5000 - 5999
        public static  final int COMPANT_HAS_EXIST = 5000;


        //和旅行社有关 6000-6999
        public static final int AGENCY_LOGIN_FAILED = 6000;
        public static final int AGENCY_REGISTER_FAILED = 6001;//注册失败
        public static final int AGENCY_MOTIFY_FAILED = 6002;//注册失败
        //和旅客相关 7000-7999
        public static final int  PASSENGER_INCOMPLICT = 7000;
        public static final int PASSENGER_NOT_EXIST = 7001;
    }

    static {
        map.put(CODE.COMPANT_HAS_EXIST,"该用户名已经存在");
        map.put(CODE.PASSENGER_INCOMPLICT,"乘客信息不完整，无法正常添加");
        map.put(CODE.AGENCY_LOGIN_FAILED,"登录信息有误，无法登录，请重试");
        map.put(CODE.PASSENGER_NOT_EXIST,"乘客不存在,操作失败");//passenger not exist
        map.put(CODE.AGENCY_REGISTER_FAILED,"该手机号已经注册，请换号重新注册");//passenger not exist
        map.put(CODE.AGENCY_MOTIFY_FAILED,"修改失败，请注意姓名和电话是否填全");
    }
}
