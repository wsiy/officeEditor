package main.java.entities;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 富文本文件搜索返回结果的包装类
 * @author lidanmax
 * 2017/03/03
 * 注：由于不存入数据库，因此该类仅用于包装，不设id字段
 * */
@XmlRootElement(name="RichTextDocument")
public class RichTextDocument extends Entity{
	private String id;  // 用来唯一标识该文档的参数，通常使用文档标题
	private String title;  // 文档标题
	private List<String> authors;  // 作者
	private String documentFormat;  // 文档类型，如：application/pdf
	private String createDate;  // 创建时间
	private List<String> creators;  // 创建人
	private String lastModifyDate;  // 最近一次修改时间
	private String pageNumber;  // 页数
	private String text;  // 正文 
	
	

	
	public RichTextDocument(String title) {
		super();
		this.title = title;
	}
	


	public RichTextDocument() {
		super();
	}



    public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	@XmlElement
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
   
    @XmlElement
	public String getDocumentFormat() {
		return documentFormat;
	}
	public void setDocumentFormat(String documentFormat) {
		this.documentFormat = documentFormat;
	}
    @XmlElement
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	@XmlElement
    public List<String> getAuthors() {
		return authors;
	}
	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}
	@XmlElement
	public List<String> getCreators() {
		return creators;
	}
	public void setCreators(List<String> creators) {
		this.creators = creators;
	}
	@XmlElement
	public String getLastModifyDate() {
		return lastModifyDate;
	}
	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

    @XmlElement
	public String getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
    @XmlElement
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}



	@Override
	public String toString() {
		return "RichTextDocument [title=" + title + ", text=" + text + "]";
	}

	
}
