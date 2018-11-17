package main.java.daoBase;

import org.apache.solr.common.SolrDocumentList;

import main.java.entities.CommonResult;


public interface IRichText {
	/**
	 * 给出路径，将该路径下所有的富文本文件上传至Solr并建立索引
	 * @author lidanmax
	 * @param path 放置待检索富文本文档的路径
	 * @return void
	 * */
	CommonResult<Integer> updateRichTextDocument(String path);
	
	/**
	 * 根据用户传来的待查询字段名和字段值全文检索富文本文档
	 * @author lidanmax
	 * @param fieldName 待检索字段名
	 * @param fieldValue 待检索字段值
	 * @return 
	 * */
	CommonResult<SolrDocumentList> queryRichTextDocument(String searchInput);
	/**
	 * 将单个文件上传至Solr服务器并建立索引
	 * @author lidanmax
	 * @param path 存放富文本文件的路径
	 * @return CommonResult对象，其中的Integer是上传文件的数量
	 * 2017/03/03通过测试
	 * */
	CommonResult<Integer> updateSingleRichTextDocument(String fileFullPath);
}
