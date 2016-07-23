package xyz.dongxiaoxia.commons.persistence.uya.jdbc.example;

import xyz.dongxiaoxia.commons.persistence.uya.jdbc.annotation.Id;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.annotation.Table;

/**
 * @author dongxiaoxia
 * @create 2016-07-12 16:41
 */
@Table
public class User {
    @Id
    private int id;
    private String username;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
