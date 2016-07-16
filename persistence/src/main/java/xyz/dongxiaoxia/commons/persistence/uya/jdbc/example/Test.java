package xyz.dongxiaoxia.commons.persistence.uya.jdbc.example;

import xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbhelper.DBHelper;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 15:26
 */
public class Test {
    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setUsername("dongxiaoxia");
        user.setPassword("qwerdfasdfadsf");
        DBHelper.getDbHelper().sql.insert(user);
    }
}
