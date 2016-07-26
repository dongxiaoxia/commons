package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

/**
 * 事物管理lambda接口，处理具体的业务逻辑，不必关心事物的开启、提交、回滚、关闭等操作。
 *
 * @author dongxiaoxia
 * @create 2016-07-12 18:43
 * @see DAOHelper#execTransaction(ITransaction)
 */
public interface ITransaction {
    void exec() throws Exception;
}