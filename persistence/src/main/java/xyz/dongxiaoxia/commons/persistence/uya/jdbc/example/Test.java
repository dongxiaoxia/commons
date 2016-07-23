package xyz.dongxiaoxia.commons.persistence.uya.jdbc.example;

import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbhelper.DBHelper;

import java.util.List;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 15:26
 */
public class Test {
    public static void main(String[] args) throws Exception {
//        User user = new User();
//        user.setUsername("dongxiaoxia");
//        user.setPassword("qwerdfasdfadsf");
//        Object o = getDbHelper().sql.insert(user);
        List<User> users = DBHelper.getDbHelper().sql.getListBySQL(User.class, "select * from user");
        users.stream().forEach(u -> System.out.println(u.getUsername()+":"+u.getPassword()));
    }
}
