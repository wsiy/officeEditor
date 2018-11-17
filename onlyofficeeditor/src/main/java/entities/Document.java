package main.java.entities;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;


/**
 * 公文 的实体类
 * @author 12556
 *
 */
@Entity
@org.hibernate.annotations.Proxy(lazy=false)
@Table(name="document")
@XmlRootElement(name="Document")
public class Document {
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue(generator="MODEL_Document_ID_GENERATOR")	
	@GenericGenerator(name="MODEL_Document_ID_GENERATOR", strategy = "increment")	
	int ID;
	
	@Column(name="fileName", nullable = false, length=50)
	String fileName;
	
	@Column(name="author", nullable = false, length=30)
	String author;
	
    @ManyToOne(targetEntity=DocumentType.class, fetch=FetchType.LAZY)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumn(name="documentType", referencedColumnName="ID")
	DocumentType documentType;
	
    /**!!!!!!!!!!!!!!!!是否可以为空 ，有待考虑!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!**/
	@Column(name="uri", nullable = true)
	String uri;
	
	@Column(name="createDate", nullable = false)
	Date createDate;
	
	@Column(name="collected", nullable = false)
	int collected;
	
	@Column(name="inCore", nullable = false)
	int inCore;
	
	@Column(name="sameNum", nullable = false)
	int sameNum;
	
	@XmlElement
    public int getSameNum() {
		return sameNum;
	}

	public void setSameNum(int sameNum) {
		this.sameNum = sameNum;
	}
	
	@XmlElement
	public int getInCore() {
		return inCore;
	}

	public void setInCore(int inCore) {
		this.inCore = inCore;
	}

	@XmlElement
	public int getID() {
		return ID;
	}
    
	public void setID(int iD) {
		ID = iD;
	}
	@XmlElement
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@XmlElement
	public String getAuthor() {
		return author;
	}
    
	public void setAuthor(String author) {
		this.author = author;
	}
	
	@XmlElement
	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}
	
	@XmlElement
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	

	@XmlElement
	public String getCreateDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(createDate);
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@XmlElement
	public int getCollected() {
		return collected;
	}

	public void setCollected(int collected) {
		this.collected = collected;
	}

	@Override
	public String toString() {
		return "Document [ID=" + ID + ", fileName=" + fileName + ", author=" + author + ", documentType=" + documentType
				+ ", uri=" + uri + ", createDate=" + createDate + ", collected=" + collected + ", inCore=" + inCore
				+ "]";
	}
	
}
