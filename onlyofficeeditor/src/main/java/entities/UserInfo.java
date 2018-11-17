package main.java.entities;

import java.io.Serializable;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.Column;
import javax.persistence.Id;



@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="UserInfo")
@XmlRootElement(name="UserInfo")
public class UserInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6899152987896840262L;

    public UserInfo() {
    }

    @Column(name="username", nullable=false, length=10)
    @Id
    private String username;

    @Column(name="pwd", nullable=false, length=20)
    private String pwd;	

   
    @XmlElement
    public String getUsername() {
		return username;
	}

    
	public void setUsername(String username) {
		this.username = username;
	}

    @XmlElement
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String toString() {
        return toString(false);
    }

    public String toString(boolean idOnly) {
        if (idOnly) {
            return  getUsername();
        }
        else {
            StringBuffer sb = new StringBuffer();
            sb.append("UserInfo[ ");
            sb.append("userName=").append(getUsername()).append(" ");
            sb.append("PWD=").append(getPwd()).append(" ");
            sb.append("]");
            return sb.toString();
        }
    }
}