package main.java.entities;

import java.io.Serializable;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="user")
@XmlRootElement(name="User")
public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6899152987896840262L;

    @Column(name="ID", nullable=false, length=10)
    @Id
    @GeneratedValue(generator="MODEL_USERINFO_UID_GENERATOR")
    @org.hibernate.annotations.GenericGenerator(name="MODEL_USERINFO_UID_GENERATOR", strategy="native")
    private String ID;
    
    @Column(name="name", nullable=false, length=30)
    private String name;

    @Column(name="pwd", nullable=false, length=30)
    private String pwd;	

   
    
    @XmlElement
	public String getPwd() {
		return pwd;
	}
    @XmlElement
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
    @XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String toString() {
        return toString(false);
    }

    public String toString(boolean idOnly) {
        if (idOnly) {
            return  getName();
        }
        else {
            StringBuffer sb = new StringBuffer();
            sb.append("UserInfo[ ");
            sb.append("userName=").append(getName()).append(" ");
            sb.append("PWD=").append(getPwd()).append(" ");
            sb.append("]");
            return sb.toString();
        }
    }
}