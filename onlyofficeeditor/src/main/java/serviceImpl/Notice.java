/**
 * ©2016 yunmel Inc. All rights reserved.
 */
package main.java.serviceImpl;

import org.apache.solr.client.solrj.beans.Field;

/**
 * <p>class: Help </p>
 * <p>desc: 新闻实体bean </p>
 * <p>company: 云麦尔科技 </p>
 * @author 云麦尔 · xuyq
 * @since 1.0 - 2016年8月21日
 */
public class Notice {
  @Field("id")
  private String id;
  @Field
  private String title;
  @Field
  private String subject;
  @Field
  private String description;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Notice [id=" + id + ", title=" + title + ", subject=" + subject + ", description="
        + description + "]";
  }

}
