package main.java.daoImpl;

import org.hibernate.criterion.Restrictions;

import main.java.daoBase.BaseDao;
import main.java.entities.User;
import java.util.List;

public class UserDao extends BaseDao<User, String>{

    public UserDao(){
        super(User.class);
    }

    public User login(String name,String pwd){
        List<User> users = findBy("ID", true, Restrictions.eq("name", name),Restrictions.eq("pwd", pwd));
        return users != null && users.size() > 0 ? users.get(0) : null;
    }
}