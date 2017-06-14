package ts.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by wr on 2017/6/14.
 */
@Entity
@Table(name = "book", schema = "ticketorder", catalog = "")
@XmlRootElement(name = "book")
public class Book implements Serializable{
    private Integer id;
    private Integer seatNum;
    private Integer seatType;
    private Timestamp orderTime;
    private Integer status;
    private static final long serialVersionUID = -3267943602377867497L;
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator="MODEL_book")
    @org.hibernate.annotations.GenericGenerator(name="MODEL_book", strategy="native")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "seatNum", nullable = false)
    public Integer getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(Integer seatNum) {
        this.seatNum = seatNum;
    }

    @Basic
    @Column(name = "seatType", nullable = false)
    public Integer getSeatType() {
        return seatType;
    }

    public void setSeatType(Integer seatType) {
        this.seatType = seatType;
    }

    @Basic
    @Column(name = "orderTime", nullable = false)
    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book that = (Book) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (seatNum != null ? !seatNum.equals(that.seatNum) : that.seatNum != null) return false;
        if (seatType != null ? !seatType.equals(that.seatType) : that.seatType != null) return false;
        if (orderTime != null ? !orderTime.equals(that.orderTime) : that.orderTime != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (seatNum != null ? seatNum.hashCode() : 0);
        result = 31 * result + (seatType != null ? seatType.hashCode() : 0);
        result = 31 * result + (orderTime != null ? orderTime.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", seatNum=" + seatNum +
                ", seatType=" + seatType +
                ", orderTime=" + orderTime +
                ", status=" + status +
                '}';
    }
}
