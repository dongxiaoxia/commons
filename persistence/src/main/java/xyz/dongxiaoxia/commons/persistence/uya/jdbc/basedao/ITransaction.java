package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

/**
 * @author dongxiaoxia
 * @create 2016-07-12 18:43
 */
public interface ITransaction {
    void exec() throws Exception;
}