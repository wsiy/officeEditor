package main.java.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.apache.solr.common.SolrDocumentList;

import main.java.entities.CommonResult;
import main.java.entities.RichTextDocument;


@Path("/RichText")
public interface IRichTextService {
	/**
	 * 将用户上传到webapp下的所有的富文本文件上传至Solr并建立索引（这里是D:\\apache-tomcat-8.5.34\\webapps\\onlyofficeeditor\\app_data\\10.130.19.2\\）
	 * @author lidanmax
	 * @param path 放置待检索富文本文档的路径
	 * @return void
	 * */

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    @Path("/updateAllRichTextDocument/")
	CommonResult<Integer> updateAllRichTextDocument();
    
    
	/**
	 * 给出单个文件的路径，将该路径下所有的富文本文件上传至Solr并建立索引
	 * @author lidanmax
	 * @param path 放置待检索富文本文档的路径
	 * @return void
	 * */

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    @Path("/updateSingleRichTextDocument/{fileName}")
    CommonResult<Integer> updateSingleRichTextDocument(@PathParam("fileName")String fileName);
    
	/**
	 * 删除索引
	 * @author lidanmax
	 * @param path 放置待检索富文本文档的路径
	 * @return void
	 * */
    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    @Path("/deleteAllRichTextDocument/")
	CommonResult<Integer> deleteAllRichTextDocument();
    
    /**
	 * 删除指定文件的索引
	 * @return CommonResult, indicate remove result
	 * */
    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    @Path("/deleteSingleRichTextDocument/{fileName}")
	CommonResult<Integer> deleteSingleRichTextDocument(@PathParam("fileName")String fileName);
    
	/**
	 * 根据用户传来的待查询字段名和字段值全文检索富文本文档
	 * @author lidanmax
	 * @param fieldName 待检索字段名
	 * @param fieldValue 待检索字段值
	 * @return 
	 * */
    @GET
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON })
    @Path("/queryRichText/{searchInput}")
	List<RichTextDocument> queryRichText(@PathParam("searchInput")String searchInput);
    


    @GET
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON })
    @Path("/updateRichTextDocument/{path}")
	CommonResult<Integer> updateAllRichTextDocument(@PathParam("path")String path);
    
    @GET
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON })
    @Path("/filterDocByType/{type}/{searchInput}")
	List<RichTextDocument> filterDocByType(@PathParam("type")String type, @PathParam("searchInput")String searchInput);

}
