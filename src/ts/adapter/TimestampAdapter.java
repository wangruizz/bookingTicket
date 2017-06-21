package ts.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampAdapter extends XmlAdapter<String, Timestamp>{
    @Override
    public Timestamp unmarshal(String v) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Timestamp(sdf.parse(v).getTime());
    }

    @Override
    public String marshal(Timestamp v) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(v.getTime()));
    }
}
