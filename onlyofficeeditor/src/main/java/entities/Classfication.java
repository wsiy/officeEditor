package main.java.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="classfication")
@XmlRootElement(name="Classfication")
public class Classfication implements Serializable{

	private static final long serialVersionUID = 8823089522547169518L;

	@Column(name="ID", nullable=false, length=10)	
	@Id	
	@GeneratedValue(generator="MODEL_Classfication_ID_GENERATOR")	
	@org.hibernate.annotations.GenericGenerator(name="MODEL_Classfication_ID_GENERATOR", strategy="assigned")	
	String ID;

	@Column(name="name", nullable = false, length=20)
	String name;
    
	public String getID() {
		return ID;
	}
	
    @XmlElement
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
    @XmlElement
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Classfication [ID=" + ID + ", name=" + name + "]";
	}
	
}
