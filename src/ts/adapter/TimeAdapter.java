package ts.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Time;


public class TimeAdapter extends XmlAdapter<String, Time>{
    @Override
    public Time unmarshal(String v) throws Exception {
        String[] s = v.split(":");
        int hour = Integer.valueOf(s[0]);
        int minute = Integer.valueOf(s[1]);
        return new Time(hour, minute, 0);
    }

    @Override
    public String marshal(Time v) throws Exception {
        return v.toString();
    }
}
