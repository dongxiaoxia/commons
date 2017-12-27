package xyz.dongxiaoxia.commons.persistence.uya.datasource;

import org.logicalcobwebs.proxool.ProxoolDataSource;

/**
 * 自定义Proxool数据源，实现配置文件密码加密
 *
 * @author dongxiaoxia
 * @create 2017-12-27 20:49:57
 */
public class DefaultProxoolDataSource extends ProxoolDataSource {

    public void setPassword(String password) {
        super.setPassword(DataSourceSecurity.decrypt(password));
        String url = this.reSetUrl(super.getDriverUrl(), super.getPassword());
        super.setDriverUrl(url);
    }

    public String reSetUrl(String url, String pwd) {
        int begin = url.indexOf("password=");
        if (begin > 0) {
            StringBuffer buf = new StringBuffer();
            buf.append(url.substring(0, begin + 10)).append(pwd);
            String subs = url.substring(begin + 10);
            int end = subs.lastIndexOf(64);
            if (end > 0) {
                buf.append(subs.substring(end));
            }
            return buf.toString();
        } else {
            return url;
        }
    }
}