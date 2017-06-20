package ts.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by wr on 2017/6/14.
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name = "agency", schema = "ticketorder", catalog = "")
@XmlRootElement(name = "agency")
public class Agency implements Serializable {
    private Integer id;
    private String pwd;
    private String name;
    private String address;
    private String contacts;
    private String phone;
    private static final long serialVersionUID = -3267943602377867497L;
    @Transient
    private String token; //token 信息

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator="MODEL_agency")
    @org.hibernate.annotations.GenericGenerator(name="MODEL_agency", strategy="native")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "pwd", nullable = false, length = 254)
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 254)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "address", nullable = true, length = 254)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "contacts", nullable = true, length = 254)
    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    @Basic
    @Column(name = "phone", nullable = false, length = 254)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Agency that = (Agency) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (pwd != null ? !pwd.equals(that.pwd) : that.pwd != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (contacts != null ? !contacts.equals(that.contacts) : that.contacts != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (pwd != null ? pwd.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (contacts != null ? contacts.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Agency{" +
                "id=" + id +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contacts='" + contacts + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
