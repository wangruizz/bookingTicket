package ts.model;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by wr on 2017/6/19.
 */
@Entity
@Table(name = "history", schema = "ticketorder", catalog = "")
@OptimisticLocking(type = OptimisticLockType.VERSION)
@XmlRootElement(name = "history")
public class History implements Serializable{
    private Integer id;
    private Timestamp version;
    private Date departureDate;
    private Time delayTime;
    private Integer status;
    private Integer businessNum;//剩余经济舱座位个数
    private Integer economyNum;//剩余商务舱座位个数
    private Flight flight;
    private static final long serialVersionUID = -3267943602377867497L;
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator="MODEL_history")
    @org.hibernate.annotations.GenericGenerator(name="MODEL_history", strategy="native")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Version
    public Timestamp getVersion() {
        return version;
    }

    private void setVersion(Timestamp version) {
        this.version = version;
    }

    @Basic
    @Column(name = "departureDate", nullable = false)
    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    @Basic
    @Column(name = "delayTime", nullable = false)
    public Time getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Time delayTime) {
        this.delayTime = delayTime;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "businessNum", nullable = false)
    public Integer getBusinessNum() {
        return businessNum;
    }

    public void setBusinessNum(Integer businessNum) {
        this.businessNum = businessNum;
    }

    @Basic
    @Column(name = "economyNum", nullable = false)
    public Integer getEconomyNum() {
        return economyNum;
    }

    public void setEconomyNum(Integer economyNum) {
        this.economyNum = economyNum;
    }
    @OneToOne
    @JoinColumn(name = "flightID",referencedColumnName = "id")
    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        History that = (History) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (departureDate != null ? !departureDate.equals(that.departureDate) : that.departureDate != null)
            return false;
        if (delayTime != null ? !delayTime.equals(that.delayTime) : that.delayTime != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (businessNum != null ? !businessNum.equals(that.businessNum) : that.businessNum != null) return false;
        if (economyNum != null ? !economyNum.equals(that.economyNum) : that.economyNum != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (departureDate != null ? departureDate.hashCode() : 0);
        result = 31 * result + (delayTime != null ? delayTime.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (businessNum != null ? businessNum.hashCode() : 0);
        result = 31 * result + (economyNum != null ? economyNum.hashCode() : 0);
        return result;
    }
    public static final class STATUS{
        public static final int HISTORY_FLIGHT_NOMAL = 0; //正常状态
        public static final int HISTORY_FLIGHT_DELAY = 1; //航班延误
        public static final int HISTORY_FLIGHT_CANCEL = -1; //航班取消
    }
}
