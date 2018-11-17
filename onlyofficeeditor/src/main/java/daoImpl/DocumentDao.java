package main.java.daoImpl;

import java.util.ArrayList;
import java.util.List;

import main.java.daoBase.BaseDao;
import main.java.entities.CoreDocument;
import main.java.entities.Document;

public class DocumentDao extends BaseDao<Document, String>{
	
	public  DocumentDao() {
		super(Document.class);
	}
	
	
	public Document get(int id) { // load结果集为空时抛异常，这里进行捕获并仅返回null处理
		try {
			Document tmp =  getHibernateTemplate().get(getEntityClass(), id);
		//	System.out.println(tmp);
			return tmp;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void removeById(int id) {
		remove(get(id));
	}

}
