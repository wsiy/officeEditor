package main.java.daoImpl;

import java.util.List;

import main.java.daoBase.BaseDao;
import main.java.entities.DocumentType;

public class DocumentTypeDao extends BaseDao<DocumentType, String>{
	public DocumentTypeDao() {
		super(DocumentType.class);
	}
	
	public String name2ID(String name) {
		List<DocumentType> documentTypes = super.findBy("name", name, "ID", true);
		if (documentTypes.size() > 1 || documentTypes.size() < 0) {
			System.err.println("-——————————————————in DomainService.getDocumentsByType(typeName) the typeName didn't exist");
			return null;
		}
		String typeID = documentTypes.get(0).getID();
		return typeID;
	}
}
