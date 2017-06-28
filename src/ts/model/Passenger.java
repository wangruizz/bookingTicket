package ts.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "passenger", schema = "ticketorder", catalog = "")
@XmlRootElement(name = "passenger")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Passenger implements Serializable {
    private Integer id;
    private int sex;//1:男；0：女
    private String name;
    private String idcard;
    private String phone;
    private Agency agency;
    private static final long serialVersionUID = -3267943602377867497L;

    @XmlElement
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "MODEL_passenger")
    @org.hibernate.annotations.GenericGenerator(name = "MODEL_passenger", strategy = "native")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlElement
    @Basic
    @Column(name = "sex", nullable = false)
    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @XmlElement
    @Basic
    @Column(name = "name", nullable = false, length = 254)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    @Basic
    @Column(name = "idcard", nullable = false, length = 254)
    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    @XmlElement
    @Basic
    @Column(name = "phone", nullable = false, length = 254)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @XmlElement
    @OneToOne
    @JoinColumn(name = "agencyID", referencedColumnName = "id")
    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passenger)) return false;

        Passenger passenger = (Passenger) o;

        if (getSex() != passenger.getSex()) return false;
        if (getId() != null ? !getId().equals(passenger.getId()) : passenger.getId() != null) return false;
        if (getName() != null ? !getName().equals(passenger.getName()) : passenger.getName() != null) return false;
        if (getIdcard() != null ? !getIdcard().equals(passenger.getIdcard()) : passenger.getIdcard() != null)
            return false;
        if (getPhone() != null ? !getPhone().equals(passenger.getPhone()) : passenger.getPhone() != null) return false;
        return getAgency() != null ? getAgency().equals(passenger.getAgency()) : passenger.getAgency() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getSex();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getIdcard() != null ? getIdcard().hashCode() : 0);
        result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
        result = 31 * result + (getAgency() != null ? getAgency().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", sex='" + sex + '\'' +
                ", name='" + name + '\'' +
                ", idcard='" + idcard + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
