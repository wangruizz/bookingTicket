package ts.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name = "company", schema = "ticketorder", catalog = "")
@XmlRootElement(name = "company")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Company implements Serializable{
    private String name;
    private String username;
    private String pwd;
    private String phone;

    private String token;

    @XmlElement
    @Transient
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private static final long serialVersionUID = -3267943602377867497L;

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
    @Id
    @Column(name = "username", nullable = false, length = 254)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlElement
    @Basic
    @Column(name = "pwd", nullable = false, length = 254)
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @XmlElement
    @Basic
    @Column(name = "phone", nullable = true, length = 254)
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

        Company that = (Company) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (pwd != null ? !pwd.equals(that.pwd) : that.pwd != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (pwd != null ? pwd.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", pwd='" + pwd + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
