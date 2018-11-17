package main.java.daoImpl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.AnalysisPhase;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.TokenInfo;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;

import org.apache.commons.collections.FastHashMap;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.AnalysisPhase;
import org.apache.solr.client.solrj.response.AnalysisResponseBase.TokenInfo;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import main.java.daoBase.IRichText;
import main.java.entities.CommonResult;
import main.java.entities.RichTextDocument;
import main.java.util.ConfigManager;
import main.java.util.EntityUtil;



/**
 * RichTextDAO实现类
 * @author lidanmax
 * 2017/03/02
 * */
public class RichTextDao implements IRichText{
	static String solrServerURL = ConfigManager.GetProperty("solr.server.path");
	private static  HttpSolrClient solr = new HttpSolrClient.Builder(solrServerURL).build(); // 该类公用的Solr客户端对象	
	
	/**
	 * 给出文件路径，本方法将该路径下所有富文本文件上传至Solr服务器并建立索引
	 * @author lidanmax
	 * @param path 存放富文本文件的路径
	 * @return CommonResult对象，其中的Integer是上传文件的数量
	 * 2017/03/03通过测试
	 * */
	@Override
	public CommonResult<Integer> updateRichTextDocument(String path){
		CommonResult<Integer> result = new CommonResult<>();  // 用来装结果的CommonResult
		List<String> pathList = new ArrayList<>();  // 用来装path目录下所有文件路径的List
		pathList = getAllPaths(pathList, path);
		for(String filePath : pathList){
			System.out.println("RichTextDao.updateRichTextDocument() 当前上传的文件是"+filePath);
			updateSingleRichTextDocument(filePath);
		}
		result.setSuccess(true);
		result.setData(pathList.size());
		result.setMessage("Documents updated successfully.");
		return result;
	}

	

	@Override
	public CommonResult<Integer> updateSingleRichTextDocument(String fileFullPath){
		String solrId = fileFullPath.substring(fileFullPath.lastIndexOf("\\") + 1);  // 获取文件名作为该文件的唯一标识
		String nowTime = getSolrDate(new Date());
		String solrServerURL = ConfigManager.GetProperty("solr.server.path");
		HttpSolrClient solr = new HttpSolrClient.Builder(solrServerURL).build(); // 该类公用的Solr客户端对象	
		CommonResult<Integer> result = new CommonResult<>();  // 用来装结果的CommonResult
		ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
		String createDate = getSolrDate(new Date());
		try {
			up.addFile(new File(fileFullPath), solrServerURL);
			up.setParam("captureAttr", "true");
			up.setParam("literal.id", solrId);
			up.setParam("fmap.meta_creation_date", "createDate");
			
			//up.setParam("fmap.stream_name", "title");//后加的未经测试
			up.setParam("fmap.content", "text");

			up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
			solr.request(up);

			QueryResponse rsp = solr.query(new SolrQuery("*文种*"));
			System.out.println(rsp);
			solr.close();
			// 设置返回结果
			result.setSuccess(true);
			result.setData(Integer.parseInt("1"));
			result.setMessage("Documents updated successfully.");
		}
		
		catch (IOException e) {
			System.out.println("RichTextService.updateSingleRichTextDocument() 文件"+solrId+"上传失败");
//			e.printStackTrace();
		} catch (SolrServerException e) {
			System.out.println("RichTextService.updateSingleRichTextDocument() solrServer异常");
//			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 根据用户传来的待查询字段名和字段值全文检索富文本文档
	 * @author lidanmax
	 * @param searchInput 搜索框输入的文本
	 * @return 
	 * 2017/03/03 测试通过
	 * */
	@Override
	public CommonResult<SolrDocumentList> queryRichTextDocument(String searchInput) {
		System.out.println("RichTextDao.queryRichTextDocument()"+"searchInput "+searchInput);
		 HttpSolrClient solr = new HttpSolrClient.Builder(solrServerURL).build(); ////////////////
		CommonResult<SolrDocumentList> result = new CommonResult<>();  // 用来装结果的CommonResult对象
		//
		//System.out.println(searchInput+"LV");
	
	  // System.out.println(str);
		//
		SolrParams solrParams = getSolrParams(searchInput);
		System.out.println("SSSSSSSSSSSSSSSSSSS  "+solrParams);
		try {
			QueryResponse response = solr.query(solrParams);
			SolrDocumentList responseResults = response.getResults();
			// 设置返回结果
			
//			
//			NamedList<Object> res = response.getResponse();
//	        NamedList<?> highlighting = (NamedList<?>) res.get("highlighting");
//	        for (int i = 0; i < highlighting.size(); i++) {
//	            System.out.println("输出"+highlighting.getName(i) + "：" + highlighting.getVal(i));
//	        }
//	        for (SolrDocument rest : responseResults) {
//	            System.out.println("无语"+rest.toString());
//	        }
//	        
	        
			result.setSuccess(true);
			//System.out.println(responseResults);
			result.setData(responseResults);
			result.setMessage("Get query result successfully.");
		} catch (SolrServerException e) {
			result.setSuccess(false);
			result.setMessage("Get query result fail. The stack trace: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("Get query result fail. The stack trace: " + e.getMessage());
		}
		return result;
	}
	
	/**
	 * 删除所有富文本索引
	 * @author lidanmax
	 * @param
	 * @return
	 * 2017/03/06
	 * */
	public CommonResult<Integer> deleteAllRichTextDocument(){
		CommonResult<Integer> result = new CommonResult<>();
		try {
			UpdateResponse wtf = solr.deleteByQuery("*:*");
			solr.commit();
			NamedList<Object> wtf2 = wtf.getResponse();
			result.setData(wtf2.size());
			result.setSuccess(true);
			result.setMessage("All indexes deleted.");
		} catch (SolrServerException | IOException e) {
			result.setData(0);
			result.setMessage("Indexes delete failed. The stack trace: " + e.getMessage());
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * 根据文件名删除索引
	 * @author lidanmax
	 * @param solrId 唯一标定富文本的指标
	 * @return
	 * */
	public CommonResult<Integer> deleteRichTextDocumentBySolrName(String solrName){
		CommonResult<Integer> result = new CommonResult<>();
		try {
			UpdateResponse wtf = solr.deleteById(solrName);
			solr.commit();
			NamedList<Object> wtf2 = wtf.getResponse();
			result.setData(wtf2.size());
			result.setMessage("Delete index by solrName succeeded. solrName is: " + solrName);
			result.setSuccess(true);
		} catch (SolrServerException | IOException e) {
			result.setData(0);
			result.setMessage("Delete index by solrName failed. The stack trace is: " + e.getMessage());
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * 给出路径，返回该路径下的所有路径，至文件为止
	 * @author lidanmax
	 * @param pathList 用来装所有文件路径字符串的列表
	 * @param path 待递归搜索的文件路径
	 * @return 所有已穷尽路径的字符串列表
	 * 2017/.3/.2 16:34通过测试
	 * */
	private static List<String> getAllPaths(List<String> pathList, String path){
		File[] files = new File(path).listFiles();
		for(File file : files){
			File[] innerFiles = file.listFiles();
			if(innerFiles == null){  // 说明这个路径已经是一个文件，则将其装到pathList中
				pathList.add(file.getAbsolutePath());
			}else{
				getAllPaths(pathList, file.getAbsolutePath());
			}
		}
		return pathList;
	}
	
	/**
	 * 输入搜索框内的字符串，返回包装好的ModifiableSolrParams对象
	 * @author lidanmax
	 * @param params 包装好的ModifiableSolrParams对象
	 * @return
	 * 2017/03/04测试通过
	 * */
	private static SolrParams getSolrParams(String searchInput){
		System.out.println("RichTextDao.getSolrParams() 传入参数是"+searchInput+"--");
		ModifiableSolrParams params = new ModifiableSolrParams();  // 这是最后要返回的结果
		// 开始处理搜索字符串
		String [] processedInput = null;  // 用来装trim且split by space后结果的字符串数组
		if(searchInput == null || "".equals(searchInput)){  // 当输入为空或空字符串时认为搜索所有
			params.add("q", "*:*");
		}else{  
			processedInput = searchInput.trim().split(" ");  // 头尾去空后用空格来分割
			// 首先得到RichTextDocumentEntity里面所有的field List
			List<String> fieldNameList = EntityUtil.getFieldNameList(new RichTextDocument());
			String queryString = null;
			StringBuilder temp = new StringBuilder();
			for(String keyWord : processedInput){
				StringBuilder queryStringBuilder = new StringBuilder();
				for(String fieldName : fieldNameList){
					queryStringBuilder.append(fieldName).append(":").append(keyWord.trim()).append(" OR ");
				}
				System.out.println("key:"+keyWord);
				queryString = queryStringBuilder.toString().substring(0, queryStringBuilder.toString().lastIndexOf(" ")-3);
				temp.append(queryString).append(" OR ");
			}
			String wtf = temp.toString().substring(0, temp.toString().lastIndexOf(" ")-4);
//			wtf += " AND group:true AND group.field:title AND group.limit:1";
			System.out.println("wtf"+wtf);
			params.add("q", wtf);
		}
		
		return params;
	}
	
	
	/**
	 * 将java Date格式转成Solr支持的UTC时间
	 * @param date
	 * @return
	 */
	public static String getSolrDate(Date date) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
		sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));
		String result = sdf1.format(date) + "T" + sdf2.format(date) + "Z";
		return result;
	}

	
	/**
                *          给指定的语句分词。
     * 
     * @param sentence 被分词的语句
     * @return 分词结果
     */
    public static List<String> getAnalysis(String searchInput) {   	
    	System.out.println("RichTextDao.getAnalysis() what??????????????");
        FieldAnalysisRequest request = new FieldAnalysisRequest();
        request.addFieldName("title");// 字段名，随便指定一个支持中文分词的字段
        request.setFieldValue("");// 字段值，可以为空字符串，但是需要显式指定此参数
        request.setQuery(searchInput);
        FieldAnalysisResponse response = null;       
            try {
				response = request.process(solr);
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       
        List<String> results = new ArrayList<String>();
        Iterator<AnalysisPhase> it = response.getFieldNameAnalysis("title")
                .getQueryPhases().iterator();
        while(it.hasNext()) {
          AnalysisPhase pharse = (AnalysisPhase)it.next();
          List<TokenInfo> list = pharse.getTokens();
          for (TokenInfo info : list) {
              results.add(info.getText());
          }

        }
    	System.out.println("RichTextDao.getAnalysis() what??????????????"+results);
        return results;
    }



	public static void main(String[] args) {
		
//		CommonResult<Integer> wtf = new RichTextDao().updateRichTextDocument("D:\\webmagic_data");
		new RichTextDao().deleteAllRichTextDocument();
//		System.out.println(wtf.getMessage());
//		System.out.println(wtf.getData());
		
	}
}
