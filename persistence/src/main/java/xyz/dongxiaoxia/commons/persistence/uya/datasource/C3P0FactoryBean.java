package xyz.dongxiaoxia.commons.persistence.uya.datasource;

import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;

/**
 * 自定义C3P0数据源，实现配置文件密码加密
 *
 * @author dongxiaoxia
 * @create 2017-12-27 20:49:57
 */
public class C3P0FactoryBean implements FactoryBean {
    private Properties properties;

    public Object getObject() throws Exception {
        return this.getProperties();
    }

    public Class getObjectType() {
        return Properties.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties inProperties) {
        this.properties = inProperties;
        this.properties.put("password", DataSourceSecurity.decrypt(this.properties.getProperty("password")));
    }
}

