package xyz.dongxiaoxia.commons.persistence.uya.jdbc.basedao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.statementcreater.IStatementCreater;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.Common;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.JdbcUtil;
import xyz.dongxiaoxia.commons.persistence.uya.jdbc.util.OutSQL;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 15:02
 */
public class DAOHandler extends DAOBase {
    private static Logger log = LoggerFactory.getLogger(DAOHandler.class);

    public DAOHandler(IStatementCreater creater) {
        super.psCreater = creater;
    }

    @Override
    public Object insert(Object bean) throws Exception {
        return insert(bean,insertUpdateTimeOut);
    }

    public Object insert(Object bean, int insertUpdateTimeOut) throws Exception{
        Class<?> beanCls = bean.getClass();
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Object rst = null;
        OutSQL sql = new OutSQL();
        try {
            conn = connHelper.get();
            ps = psCreater.createInsert(bean,conn,sql);
            ps.setQueryTimeout(insertUpdateTimeOut);
            long startTime = System.currentTimeMillis();
            ps.executeUpdate();
            printlnSqlAndTime(sql.getRealSql(), startTime);
            boolean isProc = false;
            Class<?>[] claAry = ps.getClass().getInterfaces();
            for (Class<?> cls :claAry){
                if (cls == CallableStatement.class){
                    isProc = true;
                    break;
                }
            }
            List<Field> identityFields = Common.getIdentityFields(beanCls);
            if (isProc){
                if (identityFields.size() == 1){
                    rst = ((CallableStatement)ps).getObject(Common.getDBCloumnName(beanCls,identityFields.get(0)));
                }
            }else{
                if (identityFields.size() == 1){
                    rs = ps.getGeneratedKeys();
                    if (rs.next()){
                        List<Field> idFieldList = Common.getIdFields(beanCls);
                        if (idFieldList.size() == 1){
                            if (idFieldList.get(0).getType()==int.class || idFieldList.get(0).getType() == Integer.class){
                                rst = rs.getInt(1);
                            }
                            else if (idFieldList.get(0).getType() == long.class || idFieldList.get(0).getType() == Long.class){
                                    rst = rs.getLong(1);
                            }else {
                                rst = rs.getObject(1);
                            }
                        }else {
                            rst = rs.getObject(1);
                        }
                    }
                }else if (identityFields.size() == 0){
                    List<Field> idFields = Common.getIdFields(beanCls);
                    if (idFields.size() == 1){
                        Field id = idFields.get(0);
                        id.setAccessible(true);
                        rst = id.get(bean);
                    }
                }
            }
        }catch (Exception e){
            log.error("insert error sql:" + sql.getSql(), e);
            throw e;
        }finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(ps);
            connHelper.release(conn);
        }
        return rst;
    }
}
