package main.java.daoImpl;

import java.util.List;

import main.java.daoBase.BaseDao;
import main.java.entities.Classfication;


public class ClassficationDao extends BaseDao<Classfication, String>{

    public ClassficationDao(){
        super(Classfication.class);
    }
    
	public String getIdByName(String name) {
		List<Classfication> classfications  = super.findBy("name", name, "ID", true);
		if (classfications.size() > 1 || classfications.size() < 0) {
			System.err.println("-——————————————————in DomainService.getDocumentsByType(typeName) the typeName didn't exist");
			return null;
		}
		String classficationID = classfications.get(0).getID();
		return classficationID;
	}

}