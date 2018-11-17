package main.java.entities;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;


/***
 * 核心文章
 * @author 12556
 *
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="core_document")
@XmlRootElement(name="CoreDocument")
public class CoreDocument {
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="MODEL_CoreDocument_ID_GENERATOR")	
	@GenericGenerator(name="MODEL_CoreDocument_ID_GENERATOR", strategy = "increment")	
	int ID;


	@Column(name="content", nullable=true, length=3000)	
	String content;
	
	@Column(name="characteristic", nullable = true, length=30)
	String characteristic;
	
	@Column(name="createDate", nullable = false)
	Date createDate;
	
	@Column(name="analyse", nullable = true, length=30)
	String analyse;

	
    @ManyToOne(targetEntity=Classfication.class, fetch=FetchType.LAZY)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns({ @JoinColumn(name="classificationID", referencedColumnName="ID") })
	Classfication classfication;
    
    
    @ManyToOne(targetEntity=Expression.class, fetch=FetchType.LAZY)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns({ @JoinColumn(name="expressionID", referencedColumnName="ID") })
	Expression expression;
    
    @ManyToOne(targetEntity=Document.class, fetch=FetchType.LAZY)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns({ @JoinColumn(name="documentID", referencedColumnName="ID") })
	Document document;
    
    

	@XmlElement
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	@XmlElement
	public String getContent() {
		return content;
	}
    
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(String characteristic) {
		this.characteristic = characteristic;
	}

	@XmlElement
	public String getCreateDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(createDate);
	}
    
	public void setCreateDate(Date date) {
		this.createDate = date;
	}
	@XmlElement
	public Classfication getClassfication() {
		return classfication;
	}
	public void setClassfication(Classfication classfication) {
		this.classfication = classfication;
	}

	@XmlElement
	public Expression getExpression() {
		return expression;
	}
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	@XmlElement
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	@XmlElement
	public String getAnalyse() {
		return analyse;
	}
    
	public void setAnalyse(String analyse) {
		this.analyse = analyse;
	}


	@Override
	public String toString() {
		return "CoreDocument [ID=" + ID + ", content=" + content + ", character=" + characteristic + ", createDate="
				+ createDate + ", analyse=" + analyse + ", classfication=" + classfication + ", expression="
				+ expression + ", document=" + document + "]";
	}
	
	
}
