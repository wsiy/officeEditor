package main.java.daoImpl;

import java.util.ArrayList;
import java.util.List;

import main.java.daoBase.BaseDao;
import main.java.entities.CoreDocument;
import main.java.entities.Document;

public class CoreDucumentDao extends BaseDao<CoreDocument, String>{
	DocumentTypeDao documentTypeDao = new DocumentTypeDao();
	DocumentDao documentDao = new DocumentDao();
	
	
	public DocumentTypeDao getDocumentTypeDao() {
		return documentTypeDao;
	}

	public void setDocumentTypeDao(DocumentTypeDao documentTypeDao) {
		this.documentTypeDao = documentTypeDao;
	}
	
	public DocumentTypeDao getDocumentDao() {
		return documentTypeDao;
	}

	public void setDocumentDao(DocumentDao documentDao) {
		this.documentDao = documentDao;
	}

	public CoreDucumentDao() {
		super(CoreDocument.class);
	}

	public CoreDocument get(int id) { // load结果集为空时抛异常，这里进行捕获并仅返回null处理
		try {
			CoreDocument tmp =  getHibernateTemplate().get(getEntityClass(), id);
		//	System.out.println(tmp);
			return tmp;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void removeById(int id) {
		remove(get(id));
	}
	
	public List<CoreDocument> getCoreDocByDoc(List<Document> documents) {
		List<CoreDocument> coreDocuments = new ArrayList<CoreDocument>();
		for(int i=0;i<documents.size();i++) {
			int docID = documents.get(i).getID();
			List<CoreDocument> coreDocumentsTmp = super.findBy("document.ID", docID, "ID", true);
			if (coreDocumentsTmp.size() >= 1) {
				coreDocuments.addAll(coreDocumentsTmp);
			}
		}
		return coreDocuments;
	}
	
	/*
	 * 得到起始位置startIndex，长度endIndex的CoreDocument链表
	 */
	public List<CoreDocument> getCoreDocumentPart(int startIndex, int endIndex) {
		List<CoreDocument> coreDocuments = getAll();
		List<CoreDocument> coreDocumentPart = new ArrayList<CoreDocument>();
		for(int i = startIndex; i < (startIndex+endIndex); i++ ) {
			coreDocumentPart.add(coreDocuments.get(i));
		}	
		return coreDocumentPart;
	}

}
