package xyz.dongxiaoxia.commons.persistence.uya.datasource;

import xyz.dongxiaoxia.commons.security.Base64Util;
import xyz.dongxiaoxia.commons.security.DESUtil;

/**
 * DataSource密码加密辅助类，采用DES加密与Base64加密处理
 *
 * @author dongxiaoxia
 * @create 2017-12-27 20:49:57
 */
public class DataSourceSecurity {
    private static String secretKey = "DES-SECRET-KEY";

    public static String decrypt(String password) {
        try {
            return new String(DESUtil.decrypt(Base64Util.decode(password), secretKey));
        } catch (Exception e) {
            return password;
        }
    }

    public static String encrypt(String password) throws Exception {
        return Base64Util.encode(DESUtil.encrypt(password.getBytes(), secretKey));
    }
}
