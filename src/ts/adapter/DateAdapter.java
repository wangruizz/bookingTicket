package ts.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class DateAdapter extends XmlAdapter<String, Date> {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date unmarshal(String v) throws Exception {
        java.util.Date date = sdf.parse(v);
        return new Date(date.getTime());
    }

    @Override
    public String marshal(Date v) throws Exception {
        return sdf.format(v);
    }
}
