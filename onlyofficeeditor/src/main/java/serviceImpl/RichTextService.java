package main.java.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import main.java.daoImpl.RichTextDao;
import main.java.entities.CommonResult;
import main.java.entities.CoreDocument;
import main.java.entities.Document;
import main.java.entities.RichTextDocument;
import main.java.service.IRichTextService;
import main.java.util.ConfigManager;
import main.java.util.EntityUtil;

public class RichTextService implements IRichTextService {
	static String OFFICE_STORAGE_PATH = ConfigManager.GetProperty("files.local.storage.path");
	private RichTextDao richTextDao;
	private DomainService domainService;
	private int qianhou_num = 40 ;
	
	
	public DomainService getDomainService() {
		return domainService;
	}

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	public RichTextDao getRichTextDao() {
		return richTextDao;
	}

	public void setRichTextDao(RichTextDao richTextDao) {
		this.richTextDao = richTextDao;
	}

	/**
	 * 根据用户传过来的待查询字段名和该字段值全文检索富文本文件
	 * 
	 * @author lidanmax
	 * @param searchInput 用户在搜索框输入的文本
	 * @return 2017/03/02
	 */
	public CommonResult<List<RichTextDocument>> queryRichText3(String searchInput) {
//		richTextDao = new RichTextDao();
		CommonResult<SolrDocumentList> response = richTextDao.queryRichTextDocument(searchInput); // 根据用户输入的文本查富文本文件
		System.out.println("RichTextService.queryRichText3() 函数的查询返回值是 " +response);
		List<RichTextDocument> richTextDocumentList = new ArrayList<>();
		CommonResult<List<RichTextDocument>> result = new CommonResult<>(); // 用来装结果的CommonResult对象
		try {
			for (SolrDocument doc : response.getData()) {
				RichTextDocument entity = new RichTextDocument();
				EntityUtil.setFieldsFromDocumentForRichText(entity, doc);
				richTextDocumentList.add(entity);
			}
			System.out.println("RichTextService.queryRichText3() 函数处理后的返回值是 "+richTextDocumentList);
			result.setData(richTextDocumentList);
			result.setMessage("This is RichTextService. Query succeeded.");
			result.setSuccess(true);
		} catch (SecurityException e) {
			result.setMessage("This is RichTextService. " + "Query failed. The stack trace is: " + e.getMessage());
			result.setSuccess(false);
		} catch (IllegalArgumentException e) {
			result.setMessage("This is RichTextService. " + "Query failed. The stack trace is: " + e.getMessage());
			result.setSuccess(false);
		}

		return result;
	}

	/**
	 * 将文件存储路径下所有的富文本文件上传至Solr并建立索引
	 * 
	 * @author lidanmax
	 * @param path 放置待检索富文本文档的路径
	 * @return
	 */
	@Override
	public CommonResult<Integer> updateAllRichTextDocument() {
		System.out.println("RichTextService.updateRichTextDocument() storagePath =" + OFFICE_STORAGE_PATH);
		return richTextDao.updateRichTextDocument(OFFICE_STORAGE_PATH);
	}

	/**
	 * 将参数中文件存储路径下所有的富文本文件上传至Solr并建立索引
	 * 
	 * @author lidanmax
	 * @param path 放置待检索富文本文档的路径
	 * @return
	 */
	@Override
	public CommonResult<Integer> updateAllRichTextDocument(String path) {
//		richTextDao = new RichTextDao();
		return richTextDao.updateRichTextDocument(path);
	}

	@Override
	public List<RichTextDocument> queryRichText(String searchInput) {
		Date createTimeAfterFormatDate = null;//未格式化的文档创建时间（date型）
		String createTimeBeforeFormatStr = null;//未格式化的文档创建时间
		String createTimeAfterFormatStr = "未知日期";//已经格式化的文档创建时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		String strr = "" ;
		List<String> results = richTextDao.getAnalysis(searchInput);
		List<String> all = new ArrayList<String>();
		
	    for (int i =0;i<results.size()/2; i++) { //分词        
	        strr += "*"+results.get(i)+"* ";
	        all.add(results.get(i));     
	    }
		CommonResult<SolrDocumentList> response = richTextDao.queryRichTextDocument(strr); // 根据用户输入的文本查富文本文件CommonResult<SolrDocumentList>
		//System.out.println(response);
		List<RichTextDocument> richTextDocumentList = new ArrayList<>();
		for (SolrDocument doc : response.getData()) {
			RichTextDocument entity = new RichTextDocument();
			EntityUtil.setFieldsFromDocumentForRichText(entity, doc);
			richTextDocumentList.add(entity);
		}

		for (int i = 0; i < richTextDocumentList.size(); i++) {// 截取关键字前后一段
			String str = richTextDocumentList.get(i).getText();
			String documentName = richTextDocumentList.get(i).getId();
			/*将数据库中文档创建时间类型转化成"年-月-日"的格式*/
			try {
				createTimeBeforeFormatStr = richTextDocumentList.get(i).getCreateDate();
				if (createTimeBeforeFormatStr != null) {
					createTimeAfterFormatDate = sdf.parse(createTimeBeforeFormatStr);
					createTimeAfterFormatStr = sdf.format(createTimeAfterFormatDate);
				}
				richTextDocumentList.get(i).setCreateDate(createTimeAfterFormatStr);
			} catch (ParseException e) {
				System.out.println("RichTextService.queryRichText() 出现日期格式转换异常");
				e.printStackTrace();
			}
	        /*处理文档的text*/
			String res_tmp = null ;
			for (String var : all) {//筛选出一段
				
				// String tmp = searchInput.substring(1,searchInput.length()-2);//去掉两端的*
				int index = str.indexOf(var);
				System.out.println("LV" + searchInput + "LV");
				int from, end;
				if (index - qianhou_num <= 0)
					from = 0;
				else
					from = index - qianhou_num;
				if (index + qianhou_num >= str.length())
					end = str.length();
				else
					end = index + qianhou_num;

				if (index!=-1) {
					res_tmp = str.substring(from, end) ;
					richTextDocumentList.get(i).setText(res_tmp);
					break;
				}
			}
			System.out.println(all.toString()+"测试用的");
			for(String key : all) {//加颜色
				String k_tmp = "";
				try {
					k_tmp = res_tmp.replaceAll(key, "<font color=\"red\">"+key+"</font>");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				res_tmp = k_tmp ;
				richTextDocumentList.get(i).setText(res_tmp);
			}
			richTextDocumentList.get(i).setText(documentName+res_tmp);
		}
		return richTextDocumentList;
	}

	@Override
	public CommonResult<Integer> deleteAllRichTextDocument() {
		return richTextDao.deleteAllRichTextDocument();
	}

	//deleteSingleRichTextDocument
	@Override
	public CommonResult<Integer> deleteSingleRichTextDocument(String fileName) {
		return richTextDao.deleteRichTextDocumentBySolrName(fileName);
	}
	
	@Override
	public CommonResult<Integer> updateSingleRichTextDocument(String fileName) {
		String fileFullPath =  OFFICE_STORAGE_PATH + fileName;
		return richTextDao.updateSingleRichTextDocument(fileFullPath);
	}
	

	/**
	 * 根据文档类型过滤文档
	 * @param type,including "全部"、"提纲"、"观点"、"其他"、"收藏"
	 * @param allRichDoc
	 * @return
	 */
	@Override
	public List<RichTextDocument> filterDocByType(String type,String searchInput){
		List<RichTextDocument> allRichDoc = queryRichText(searchInput);
		System.out.println("RichTextService.filterDocByType() 接受的参数：：："+allRichDoc);
		if (type.equals("全部")) {
			return allRichDoc;
		}else {
			List<RichTextDocument> tmpAllRichDoc = allRichDoc;
			List<RichTextDocument> resRichDoc = new ArrayList<RichTextDocument>();
			for (RichTextDocument richTextDocument : tmpAllRichDoc) {
				String docName = richTextDocument.getId();
				if (inTheClassification(docName,type)) {
					System.out.println("RichTextService.filterDocByType() 该文档名"+docName+"存在于该分类"+type);
					resRichDoc.add(richTextDocument);
				}else {
					System.out.println("RichTextService.filterDocByType() 该文档名"+docName+"不存在于该分类"+type);
				}
			}
			return resRichDoc;
		}
	}
	


	/**
	 * 根据文件名查询改文件是否属于该分类
	 * @param docName
	 * @param type
	 * @return
	 */
	private boolean inTheClassification(String docName, String classification) {
		List<String> thisClassDocTitles = getTitlesInTheClassification(classification);
		System.out.println("RichTextService.inTheClassification() 获得的文档名字是"+thisClassDocTitles);
		if (thisClassDocTitles.contains(docName)) {
			return true;
		}
		return false;
	}

	/**
	 * 获得某个分类名的全部文档名
	 * @param classification 分类名，包括“我的收藏”、“提纲”、“观点”、“其他”
	 * @return 该分类的全部文档名
	 */
	private List<String> getTitlesInTheClassification(String classification) {
		List<String> ansTitles = new ArrayList<String>();
		if (classification.equals("我的收藏")) {
			List<Document> thisClassDoc =  new ArrayList<Document>();
			thisClassDoc = domainService.getCollectedDocuments();
			System.out.println("RichTextService.getTitlesInTheClassification() 被收藏的文档是"+thisClassDoc);
			for (Document document : thisClassDoc) {
				ansTitles.add(document.getFileName());
			}
		}else {
			List<CoreDocument> thisClassDoc = domainService.getCoreDocumentsByClassfication2(classification);
			for (CoreDocument coreDocument : thisClassDoc) {
				ansTitles.add(coreDocument.getDocument().getFileName());
			}
		}
		return ansTitles;
	}

}

