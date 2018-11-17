package main.java.serviceImpl;

import main.java.daoImpl.ClassficationDao;
import main.java.daoImpl.CoreDucumentDao;
import main.java.daoImpl.DocumentDao;
import main.java.daoImpl.DocumentTypeDao;
import main.java.daoImpl.ExpressionDao;
import main.java.daoImpl.UserDao;
import main.java.entities.CoreDocument;
import main.java.entities.Classfication;
import main.java.entities.Document;
import main.java.entities.DocumentType;
import main.java.entities.Expression;
import main.java.entities.Message;
import main.java.entities.PageBeanCoreDocument;
import main.java.entities.PageBeanDocument;
import main.java.entities.User;
import main.java.service.IDomainService;
import main.java.util.DocumentManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.core.Response;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class DomainService implements IDomainService{
 
    private UserDao userDao;
	private DocumentDao documentDao;
	private DocumentTypeDao documentTypeDao;
	private CoreDucumentDao coreDucumentDao;
	private ClassficationDao classficationDao;
	private ExpressionDao  expressionDao ;
	
	public UserDao getUserDao() {
		return userDao;
	}


	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	

	public DocumentDao getDocumentDao() {
		return documentDao;
	}


	public void setDocumentDao(DocumentDao documentDao) {
		this.documentDao = documentDao;
	}


	public DocumentTypeDao getDocumentTypeDao() {
		return documentTypeDao;
	}


	public void setDocumentTypeDao(DocumentTypeDao documentTypeDao) {
		this.documentTypeDao = documentTypeDao;
	}


	public CoreDucumentDao getCoreDucumentDao() {
		return coreDucumentDao;
	}


	public void setCoreDucumentDao(CoreDucumentDao coreDucumentDao) {
		this.coreDucumentDao = coreDucumentDao;
	}
	
	public ClassficationDao getClassficationDao() {
		return classficationDao;
	}

	public void setClassficationDao(ClassficationDao classficationDao) {
		this.classficationDao = classficationDao;
	}

	public ExpressionDao  getExpressionDao() {
		return expressionDao ;
	}

	public void setExpressionDao(ExpressionDao  expressionDao ) {
		this.expressionDao  = expressionDao ;
	}

	@Override
    public Response doLogin(String userName, String pwd) {
        User user = userDao.login(userName, pwd);
        if (user != null) {
            return Response.ok(user).header("EntityClass", "User").build();
        }
        return Response.ok(new Message(Message.CODE.LIGIN_FAILED)).header("EntityClass", "Message").build();
    }
	

	@Override
	public List<User> getAllUsers() {
		List<User> ans = userDao.getAll(); 
		System.out.println(ans);
		return ans;
	}
	

	@Override
	public List<Document> getDocuments() {
		return documentDao.getAll(); 
	}

	@Override
	public PageBeanDocument getDocumentsByName(String fileName) {
		 List<Document> pbDocument = documentDao.findLike("fileName", fileName, "ID", true);
		 if( pbDocument.size() == 0) {
				PageBeanDocument pb = new PageBeanDocument( );
				return pb;
			}else {
				int pageNum = 1;
				PageBeanDocument pb = new PageBeanDocument( pbDocument,pageNum);
				System.out.println(pb.getPerList().toString());
				System.out.println(pb.getPerList().size());
				return pb;
			}
	}
	
	@Override
	public List<Document> getDocumentsByNameInCore(String fileName) {
		 return documentDao.findLike("fileName", fileName, "ID", true);
	}
	
	@Override
	public List<Document> getDocumentsByAuthor(String author) {
		return documentDao.findLike("author", author, "ID", true);
	}
	
	
	
	@Override
	public PageBeanDocument getDocumentsByCreateDate(Date createDate) {
		List<Document> pbDocument =documentDao.findBy("createDate",createDate,"ID", true);
		if( pbDocument.size() == 0) {
			PageBeanDocument pb = new PageBeanDocument( );
			return pb;
		}else {
			int pageNum = 1;
			PageBeanDocument pb = new PageBeanDocument( pbDocument,pageNum);
			return pb;
		}
	}
	
	@Override
	public PageBeanDocument getDocumentsByTimeOrder(Boolean isAsc) {
		List<Document> pbDocument = documentDao.getAll("createDate", isAsc);
		if( pbDocument.size() == 0) {
			PageBeanDocument pb = new PageBeanDocument( );
			return pb;
		}else {
			int pageNum = 1;
			PageBeanDocument pb = new PageBeanDocument( pbDocument,pageNum);
			return pb;
		}
	}

	@Override
	public  PageBeanDocument getDocumentsByType(String typeName) {
		String typeID = documentTypeDao.name2ID(typeName);
		List<Document> pbDocument = documentDao.findBy("documentType.ID", typeID, "fileName", false);
		if( pbDocument.size() == 0) {
			PageBeanDocument pb = new PageBeanDocument( );
			return pb;
		}else {
			int pageNum = 1;
			PageBeanDocument pb = new PageBeanDocument( pbDocument,pageNum);
			return pb;
		}
	}
	
	
	@Override
	public  List<Document> getDocumentsByTypeInCore(String typeName) {
		String typeID = documentTypeDao.name2ID(typeName);
		return documentDao.findBy("documentType.ID", typeID, "fileName", false);
	}
	

	@Override
	public Response deleteDocumentByID(int documentID) {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+documentID);
		documentDao.removeById(documentID);
        return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
	}
	
	@Override
	public Response collectDocumentByID(int documentID) {
		Document documentTemp = documentDao.get(documentID);
		documentTemp.setCollected(1);
		documentDao.update(documentTemp);
        return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
	}

	/**************************************************************************************************************************/
    /*
	 * methods for CoreDocumentInfo page
	 */
	
	@Override
	public List<CoreDocument> getCoreDocuments() {
		return coreDucumentDao.getAll();
	}


	@Override
	public PageBeanCoreDocument getCoreDocumentsByDate (Date createDateEarliest, Date createDateLatest) {
		List<CoreDocument> pbCoreDocument = new ArrayList<CoreDocument>();
		List<CoreDocument> coreDocumentsTemp = new ArrayList<CoreDocument>();
		Calendar ca = Calendar.getInstance();
		ca.setTime(createDateEarliest);
		while(ca.getTime().compareTo(createDateLatest)<=0){
			  java.sql.Date dateS =new java.sql.Date(ca.getTime().getTime());
		      coreDocumentsTemp = coreDucumentDao.findBy("createDate",dateS,"ID", true);
			  if (coreDocumentsTemp.size() >= 1) {
				  pbCoreDocument.addAll(coreDocumentsTemp);
			  }
		      ca.add(ca.DATE, 1);
		} 
		if( pbCoreDocument.size() == 0) {
			PageBeanCoreDocument pbc = new PageBeanCoreDocument();
			return pbc;
		}else {
			int pageNum = 1;
			PageBeanCoreDocument pbc = new PageBeanCoreDocument( pbCoreDocument,pageNum);
			return pbc;
		}
	}


	@Override
	public PageBeanCoreDocument getCoreDocumentsByType(String typeName) {
		String typeID = documentTypeDao.name2ID(typeName);
		List<Document> documents = documentDao.findBy("documentType.ID", typeID, "fileName", false);
		List<CoreDocument> pbCoreDocument = coreDucumentDao.getCoreDocByDoc(documents);
		
		
		if( pbCoreDocument.size() == 0) {
			PageBeanCoreDocument pbc = new PageBeanCoreDocument();
			return pbc;
		}else {
			int pageNum = 1;
			PageBeanCoreDocument pbc = new PageBeanCoreDocument( pbCoreDocument,pageNum);
			return pbc;
		}
	}

	@Override
	public PageBeanCoreDocument getCoreDocumentsByName(String fileName) {
		List<Document> documents = documentDao.findLike("fileName", fileName, "ID", true);
		System.out.println("documents"+ documents.size());
		List<CoreDocument> pbCoreDocument = coreDucumentDao.getCoreDocByDoc(documents);
		if( pbCoreDocument.size() == 0) {
			PageBeanCoreDocument pbc = new PageBeanCoreDocument();
			return pbc;
		}else {
			int pageNum = 1;
			PageBeanCoreDocument pbc = new PageBeanCoreDocument( pbCoreDocument,pageNum);
			return pbc;
		}
		
	}
	
	@Override
	public PageBeanCoreDocument getCoreDocumentsByClassfication(String classficationName) {
		String classficationID = classficationDao.getIdByName(classficationName);
		List<CoreDocument> pbCoreDocument = coreDucumentDao.findBy("classfication.ID", classficationID, "ID", false);
		if( pbCoreDocument.size() == 0) {
			PageBeanCoreDocument pbc = new PageBeanCoreDocument();
			return pbc;
		}else {
			int pageNum = 1;
			PageBeanCoreDocument pbc = new PageBeanCoreDocument( pbCoreDocument,pageNum);
			return pbc;
		}
		
	}

	@Override
	public PageBeanCoreDocument getCoreDocumentsByExpression(String expressionName) {
		String expressionID = expressionDao.getIdByName(expressionName);
		List<CoreDocument> pbCoreDocument = coreDucumentDao.findBy("expression.ID", expressionID, "ID", false);
		if( pbCoreDocument.size() == 0) {
			PageBeanCoreDocument pbc = new PageBeanCoreDocument();
			return pbc;
		}else {
			int pageNum = 1;
			PageBeanCoreDocument pbc = new PageBeanCoreDocument( pbCoreDocument,pageNum);
			return pbc;
		}
	}


	@Override
	public Response deleteCoreDocumentByID(int coreDocumentID) {
		coreDucumentDao.removeById(coreDocumentID);
        return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
	}
	
	
	@Override
	public PageBeanCoreDocument getCoreDocumentsByTimeOrder(Boolean isAsc) {
		List<CoreDocument> pbCoreDocument = coreDucumentDao.getAll("createDate", isAsc);
		if( pbCoreDocument.size() == 0) {
			PageBeanCoreDocument pbc = new PageBeanCoreDocument();
			return pbc;
		}else {
			int pageNum = 1;
			PageBeanCoreDocument pbc = new PageBeanCoreDocument( pbCoreDocument,pageNum);
			return pbc;
		}
	}


	/**************************************************************************************************************************/
	
	@Override
	public List<Document> getDocumentsByNameType(String name, String type) {
		String typeID = documentTypeDao.name2ID(type);
		List<Document> document1 = documentDao.findBy("documentType.ID", typeID, "ID", true);
		List<Document> document2 = documentDao.findLike("fileName", name, "ID", true);
		List<Document> document = new ArrayList<Document>();
		for(int i=0;i<document1.size();i++) {
			for(int j=0;j<document2.size();j++) {
				if(document1.get(i).getID() == document2.get(j).getID()) {
					document.add(document1.get(i));
					break;
				}
			}
		}
		return document;
	}
	
	@Override
	public List<Document> getDocumentsByTypeAuthor(String type,String author) {
		String typeID = documentTypeDao.name2ID(type);
		List<Document> document1 = documentDao.findBy("documentType.ID", typeID, "ID", true);
		List<Document> document2 = documentDao.findLike("author", author, "ID", true);
		List<Document> document = new ArrayList<Document>();
		for(int i=0;i<document1.size();i++) {
			for(int j=0;j<document2.size();j++) {
				if(document1.get(i).getID() == document2.get(j).getID()) {
					document.add(document1.get(i));
					break;
				}
			}
		}
		return document;
	}
	
	@Override
	public List<Document> getDocumentsByNameAuthor(String name,String author) {
		List<Document> document1 = documentDao.findLike("fileName", name, "ID", true);
		List<Document> document2 = documentDao.findLike("author", author, "ID", true);
		List<Document> document = new ArrayList<Document>();
		for(int i=0;i<document1.size();i++) {
			for(int j=0;j<document2.size();j++) {
				if(document1.get(i).getID() == document2.get(j).getID()) {
					document.add(document1.get(i));
					break;
				}
			}
		}
		return document;
	}
	
	@Override
	public List<Document> getDocumentsByNameTypeAuthor(String name,String type,String author) {
		String typeID = documentTypeDao.name2ID(type);
		List<Document> document1 = documentDao.findBy("documentType.ID", typeID, "ID", true);
		List<Document> document2 = documentDao.findLike("fileName", name, "ID", true);
		List<Document> document3 = documentDao.findLike("author", author, "ID", true);
		List<Document> document = new ArrayList<Document>();
		for(int i=0;i<document1.size();i++) {
			for(int j=0;j<document2.size();j++) {
				for(int k=0;k<document3.size();k++) {
					if(document1.get(i).getID() == document2.get(j).getID()&&document1.get(i).getID() == document3.get(k).getID()) {
						document.add(document1.get(i));
						return document;
					}
				}
			}
		}
		return null;
	}

	/*
	 * 新建CoreDocument(non-Javadoc)
	 * @see main.java.service.IDomainService#addCoreDocument(java.lang.String)
	 */
	@Override
	public Response addCoreDocument(String data) throws ParseException {
		/*
		 * CoreDocument
		 */
		CoreDocument coreDocument = new CoreDocument();
		JSONObject json = JSONObject.fromObject(data);
		
		/*
		 * Document
		 */
		
		String docName=json.getString("filename");
		String docType=json.getString("type");
		String docAuthor=json.getString("author");
		List<Document> documents = getDocumentsByNameTypeAuthor(docName,docType,docAuthor);
		
		coreDocument.setDocument(documents.get(0));
		/*
		 * Classfication
		 */
		Classfication classfication  = new Classfication();
		String className=json.getString("classi");
		String classID = classficationDao.getIdByName(className);
		classfication.setID(classID);
		classfication.setName(className);
		coreDocument.setClassfication(classfication);
		/*
		 * Expression
		 */
		Expression expression = new Expression();
		String expressName=json.getString("expre");
		String expressID = expressionDao.getIdByName(expressName);
		expression.setID(expressID);
		expression.setName(expressName);
		coreDocument.setExpression(expression);

		/*
		 * Date
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.sql.Date coreDate =new java.sql.Date(sdf.parse(json.getString("time")).getTime());
		coreDocument.setCreateDate(coreDate);
		//Analyse
		coreDocument.setAnalyse(json.getString("anay"));
		//Character
		coreDocument.setCharacteristic(json.getString("chara"));
		//Content
		coreDocument.setContent(json.getString("content"));
		//SAVE
		coreDucumentDao.save(coreDocument);
		return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
	}


	@Override
	public String  viewOnline(String fileName) {
//		String filePath = "D:"+"\\"+"apache-tomcat-8.5.34-windows-x64"+"\\"+"apache-tomcat-8.5.34"+"\\"+"webapps"+"\\"+"onlyofficeeditor"+"\\"+"app_data"+"\\"+"192.168.172.1"+"\\"+fileName+".docx";
		String filePath = "C:\\Users\\OOEdi\\Desktop\\11.txt";
		StringBuilder result = new StringBuilder();
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = bfr.readLine()) != null) {
                result.append(lineTxt).append("\n");
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        List<String> list = new ArrayList<String>();  
//        list.add(result.toString());  
//        System.out.println("898989"+list);
//        JSONArray array = new JSONArray();  
//        array.add(list);   
//        System.out.println("787878"+array);
        return result.toString();					
	}

	//初始Document分页
	@Override  
	public PageBeanDocument findInitDocument() {
		int pageNum = 1;
		List<Document> totalList = documentDao.getAll();
		PageBeanDocument pb = new PageBeanDocument(totalList,pageNum);
		return pb;
	}
	
	//更新Document
	@Override  
	public PageBeanDocument findPartDocument(List<Document> totalList) {
		int pageNum = 1;
		PageBeanDocument pb = new PageBeanDocument(totalList, pageNum);
		return pb;
	}
	
	//初始CoreDocument分页
	@Override  
	public PageBeanCoreDocument findInitCoreDocument() {
		int pageNum = 1;
		List<CoreDocument> totalList = coreDucumentDao.getAll();
		PageBeanCoreDocument pbc = new PageBeanCoreDocument(totalList,pageNum);
		return pbc;
	}
	
	//更新CoreDocument分页
	@Override  
	public PageBeanCoreDocument findPartCoreDocument(List<CoreDocument> totalList) {
		int pageNum = 1;
		PageBeanCoreDocument pb = new PageBeanCoreDocument(totalList, pageNum);
		return pb;
	}

	/*
	 * 新建Document(non-Javadoc)
	 * @see main.java.service.IDomainService#addDocument(java.lang.String)
	 */
	@Override
	public Response addDocument(String data) throws ParseException {
		System.out.println("进来啦");
		/*
		 * Document
		 */
		Document document = new Document();
		JSONObject json = JSONObject.fromObject(data);
		System.out.println(json);
		
		/*
		 * fileName
		 */
		String fileName = json.getString("filename");
		List<Document> documentTemp = documentDao.getAll();
		if(documentTemp.size()==0) {//文章列表为空
			document.setFileName(fileName);
			document.setSameNum(0);//无重名document
		}
		//else 文章列表不为空
		for(int i=0;i<documentTemp.size();i++) { //获得当前列表中文件名
			String fileNameTemp = documentTemp.get(i).getFileName();
			if(fileName.equals(fileNameTemp)) {//document重名
				int sameNum = documentTemp.get(i).getSameNum()+1;
				fileName = fileName +"("+sameNum+")";
				document.setFileName(fileName);
				documentTemp.get(i).setSameNum(sameNum);
				break;
			}else {
				document.setFileName(fileName);
				document.setSameNum(0);//无重名document
			}
		}
		
		//author
		document.setAuthor(json.getString("author"));
		//collected
		document.setCollected(0);
		//inCore
		document.setInCore(0);
		/*
		 * DocumentType
		 */
		DocumentType documentType  = new DocumentType();
		String typeName=json.getString("type");
		System.out.println("8888888"+json.getString("type"));
		String typeID = documentTypeDao.name2ID(typeName);
		
		documentType.setID(typeID);
		documentType.setName(typeName);
		document.setDocumentType(documentType);
		
		/*
		 * Date
		 */
		//获得当前日期  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        java.util.Date d = new java.util.Date();
        String dateNowStr = sdf.format(d);  //格式化后的日期  
		java.sql.Date coreDate =new java.sql.Date(sdf.parse(dateNowStr).getTime());
		document.setCreateDate(coreDate);
		
		//SAVE
		documentDao.save(document);
		return Response.ok(new Message(Message.CODE.SUCCESS)).header("EntityClass", "Message").build();
	}

	/**
	 * 未向外界开放，即未提供Rest，“在线编辑”页面的右侧筛选栏调用。
	 * @return 被收藏的文档list
	 */	public List<Document> getCollectedDocuments() {
		 return documentDao.findBy("collected", 1, "ID", false);
	}

	/**
	 * 根据文档名查询核心文章
	 * 这个返回值没有分页
	 */
	public List<CoreDocument> getCoreDocumentsByClassfication2(String classficationName) {
		String classficationID = classficationDao.getIdByName(classficationName);
		List<CoreDocument> coreDocuments = coreDucumentDao.findBy("classfication.ID", classficationID, "ID", false);
		return coreDocuments;
	}
	
	
}
