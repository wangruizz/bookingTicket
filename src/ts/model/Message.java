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
        public static final int DATE_FORMAT_ERROR = -4;
        public static final int BAD_REQUEST = -3;
        public static final int URL_NOT_FOUND = -2;
        public static final int UNKNOWN_ERROR = -1;
        public static final int SUCCESS = 1;

        //与token有关的信息 1000 - 1999
        public static final int NO_TOKEN = 1000;
        public static final int TOKEN_ERROR = 1001;


        //登录失败
        public static final int LOGIN_FAILED = 2000;

        //与航班有关的信息3000
        public static final int FLIGHT_NOT_EXIST = 3000;
        public static final int FLIGHT_HAS_CANCELLED = 3001;
        public static final int FLIGHT_IS_NORMAL = 3002;
        public static final int FLIGHT_SEAT_TYPE_ERROR = 3003;


        //与Company有关的信息 5000 - 5999
        public static final int COMPANY_HAS_EXIST = 5000;
        public static final int COMPANY_NOT_EXIST = 5001;


        //和旅行社有关 6000-6999
        public static final int AGENCY_LOGIN_FAILED = 6000;
        public static final int AGENCY_REGISTER_FAILED = 6001;//注册失败
        public static final int AGENCY_MODIFY_FAILED = 6002;//注册失败
        public static final int AGENCY_NOT_EXISTED = 6003;//旅行社不存在
        public static final int PWD_IS_WRONG = 6004;//密码输入错误

        //和旅客相关 7000-7999
        public static final int  PASSENGER_INCOMPLICT = 7000;
        public static final int PASSENGER_NOT_EXIST = 7001;
        public static final int PHONE_IS_WRONG = 7002;

        //订单有关  8000-8999
        public static final int BOOK_FAILED = 8000;//预定失败
        public static final int BOOK_CANCEL_FAILED = 8001;//取消订单失败
        public static final int BOOK_PAY_FAILED = 8002;//付款失败
        public static final int BOOK_PRINT_FAILED = 8003;//打印机票失败
        public static final int BOOK_NOT_ALL = 8004;//订单信息不完整，预订失败
        public static final int BOOK_QUERY_FAILED = 8005;//订单查询失败
        public static final int TICKET_QUERY_FAILED = 8006;//机票查询失败
        public static final int TICKET_HAS_PAYED = 8007;//机票已付款
        public static final int TICKET_HAS_EXIST = 8008;//订单已存在（重复订票）
        public static final int TICKET_NOT_EXIST = 8009;//订单不存在
    }

    static {
        map.put(CODE.PASSENGER_INCOMPLICT,"乘客信息不完整，无法正常添加");
        map.put(CODE.AGENCY_LOGIN_FAILED,"登录信息有误，无法登录，请重试");
        map.put(CODE.PASSENGER_NOT_EXIST,"乘客不存在,操作失败");//passenger not exist
        map.put(CODE.AGENCY_REGISTER_FAILED,"该手机号已经注册，请换号重新注册");//passenger not exist
        map.put(CODE.AGENCY_MODIFY_FAILED,"修改失败，请注意姓名和电话是否填全");
        map.put(CODE.BOOK_FAILED,"预订失败");
        map.put(CODE.BOOK_CANCEL_FAILED,"取消订单失败");
        map.put(CODE.BOOK_PAY_FAILED,"付款失败");
        map.put(CODE.BOOK_PRINT_FAILED,"打印机票失败");
        map.put(CODE.BOOK_NOT_ALL,"订单信息不完整，预订失败");
        map.put(CODE.BOOK_QUERY_FAILED,"订单查询失败");
        map.put(CODE.COMPANY_HAS_EXIST, "该公司用户名已经存在");
        map.put(CODE.COMPANY_NOT_EXIST, "该公司用户名不存在");
        map.put(CODE.LOGIN_FAILED, "登录失败");
        map.put(CODE.SUCCESS, "操作成功");
        map.put(CODE.UNKNOWN_ERROR, "未知错误");
        map.put(CODE.URL_NOT_FOUND, "URL错误");
        map.put(CODE.NO_TOKEN, "没有Token");
        map.put(CODE.TOKEN_ERROR, "Token错误");
        map.put(CODE.BAD_REQUEST, "请求错误");
        map.put(CODE.PHONE_IS_WRONG, "手机号格式不对");
        map.put(CODE.TICKET_QUERY_FAILED, "机票查询失败");
        map.put(CODE.DATE_FORMAT_ERROR, "日期格式错误");
        map.put(CODE.TICKET_HAS_PAYED, "机票已付款");
        map.put(CODE.AGENCY_NOT_EXISTED, "旅行社不存在");
        map.put(CODE.PWD_IS_WRONG, "密码输入错误");
        map.put(CODE.FLIGHT_NOT_EXIST, "航班不存在");
        map.put(CODE.FLIGHT_HAS_CANCELLED, "航班已取消");
        map.put(CODE.FLIGHT_IS_NORMAL, "航班正常");
        map.put(CODE.FLIGHT_SEAT_TYPE_ERROR, "航班座位类型错误");
        map.put(CODE.TICKET_HAS_EXIST, "请勿重复订票");
        map.put(CODE.TICKET_NOT_EXIST, "订单不存在");
    }
}
