package main.java.daoImpl;


import java.util.List;

import main.java.daoBase.BaseDao;
import main.java.entities.Expression; ;


public class ExpressionDao extends BaseDao<Expression , String>{

    public ExpressionDao  (){
        super(Expression.class);
    }
    
	public String getIdByName(String name) {
		List<Expression> expressions   = super.findBy("name", name, "ID", true);
		if (expressions.size() > 1 || expressions.size() < 0) {
			System.err.println("-——————————————————in DomainService.getIdByName(expressionName) the expressionName didn't exist");
			return null;
		}
		String expressionID = expressions.get(0).getID();
		return expressionID;
	}

}