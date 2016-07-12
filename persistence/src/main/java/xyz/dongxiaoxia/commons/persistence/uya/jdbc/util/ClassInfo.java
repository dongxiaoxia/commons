package xyz.dongxiaoxia.commons.persistence.uya.jdbc.util;

import xyz.dongxiaoxia.commons.persistence.uya.jdbc.annotation.*;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dongxiaoxia
 * @create 2016-07-11 17:01
 */
public class ClassInfo {

    /**
     * key:field.getName()
     * value:field
     */
    private Map<String, Field> mapIDField;

    /**
     * key: field.getName()  value:field
     */
    private Map<String, Field> mapAllDBField;

    private Map<String, Field> mapInsertableField;

    private Map<String, Field> mapUpdatableField;

    private Map<String, Field> mapIdentityField;

    private Map<String, String> mapDBColumnName;

    private Map<String, Method> mapSetMethod;

    private Map<String, Method> mapGetMethod;

    /**
     * key: field.getName()  value:field
     * add by haoxb
     * 针对表结构相同，表名不同的个性处理
     * 针对表结构相同，表名不同的情况，通过相同的一个实体类做到兼容
     */
    private Map<String, Field> mapTableRenameField;
    /**
     * key: field.getName()  value:Method
     */
    private Map<String, Method> mapTableRenameSetMethod;

    /**
     * key: field.getName()  value:Method
     */
    private Map<String, Method> mapTableRenameGetMethod;

    private String tableName;

    private ProcedureName procdure;

    public ClassInfo(Class<?> clazz) throws Exception {
        this.setTableName(getTableName(clazz));
        this.setMapIDField(getIdFields(clazz));
        this.setMapAllDBField(getAllDBFields(clazz));
        this.setMapInsertableField(getInsertableFields(clazz));
        this.setMapUpdatableField(getUpdatableFields(clazz));
        this.setMapDBColumnName(getColumnNames(clazz));
        this.setMapSetMethod(getSetterMethod(clazz));
        this.setMapGetMethod(getGetterMethod(clazz));
        this.setProcdure(getProc(clazz));
        this.setMapIdentityField(getIdentityFields(clazz));
        /**
         * 针对表结构相同，表名不同的个性处理
         * @author haoxb 2013-05-15
         */
        this.setMapTableRenameField(getTableRenameFields(clazz));
        this.setMapTableRenameGetMethod(getTableRenameGetterMethod(clazz));
        this.setMapTableRenameSetMethod(getTableRenameSetterMethod(clazz));
    }

    /**
     * 获取表名
     *
     * @param clazz
     * @return
     */
    private String getTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            if (!table.name().equalsIgnoreCase("className")) {
                return table.name();
            }
        }
        String name = clazz.getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }

    /**
     * 获得所有允许插入的字段
     *
     * @param clazz
     * @return
     */
    private Map<String, Field> getInsertableFields(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (!f.isAnnotationPresent(NotDBColumn.class)) {
                if (f.isAnnotationPresent(Id.class)) {
                    Id id = f.getAnnotation(Id.class);
                    if (id.insertable()) {
                        fieldMap.put(f.getName(), f);
                    }
                } else {
                    Column column = f.getAnnotation(Column.class);
                    if (column != null) {
                        if (!column.defauleDBValue()) {
                            fieldMap.put(f.getName(), f);
                        }
                    } else {
                        fieldMap.put(f.getName(), f);
                    }
                }
            }
        }
        return fieldMap;
    }

    /**
     * 获得DB所有字段
     *
     * @param clazz
     * @return
     */
    private Map<String, Field> getAllDBFields(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (!f.isAnnotationPresent(NotDBColumn.class)) {
                if (!"seriaVersionSSID".equalsIgnoreCase(f.getName())) {
                    fieldMap.put(f.getName(), f);
                }
            }
        }
        return fieldMap;
    }

    /**
     * 获得主键字段
     *
     * @param clazz
     * @return
     */
    private Map<String, Field> getIdFields(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(Id.class)) {
                fieldMap.put(f.getName(), f);
            }
        }
        return fieldMap;
    }

    /**
     * 获得所有允许更新的字段
     *
     * @param clazz
     * @return
     */
    private Map<String, Field> getUpdatableFields(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (!f.isAnnotationPresent(NotDBColumn.class)) {
                if (f.isAnnotationPresent(Id.class)) {
                    Id id = f.getAnnotation(Id.class);
                    if (id.updatable()) {
                        fieldMap.put(f.getName(), f);
                    }
                } else {
                    Column column = f.getAnnotation(Column.class);
                    if (column != null) {
                        if (!column.defauleDBValue()) {
                            fieldMap.put(f.getName(), f);
                        }
                    } else {
                        fieldMap.put(f.getName(), f);
                    }
                }
            }
        }
        return fieldMap;
    }

    /**
     * 获得字段名
     *
     * @param clazz
     * @return
     */
    private Map<String, String> getColumnNames(Class<?> clazz) {
        Map<String, String> nameMap = new HashMap<>();
        Collection<Field> fields = mapAllDBField.values();
        for (Field f : fields) {
            if (f.isAnnotationPresent(Column.class)) {
                Column column = f.getAnnotation(Column.class);
                if (!column.name().equalsIgnoreCase("fieldName")) {
                    nameMap.put(f.getName(), column.name());
                } else {
                    nameMap.put(f.getName(), f.getName());
                }
            } else {
                nameMap.put(f.getName(), f.getName());
            }
        }
        return nameMap;
    }

    /*
     * 获得字段对应的set方法
	 */
    private Map<String, Method> getSetterMethod(Class<?> clazz) throws Exception {
        Map<String, Method> methodMap = new HashMap<>();
        Collection<Field> fields = mapAllDBField.values();
        PropertyDescriptor[] IPropDesc = Introspector.getBeanInfo(clazz, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
        for (Field f : fields) {
            for (PropertyDescriptor propertyDescriptor : IPropDesc) {
                if (propertyDescriptor.getName().equalsIgnoreCase(f.getName())) {
                    Method setMethod = propertyDescriptor.getWriteMethod();
                    methodMap.put(f.getName(), setMethod);
                    break;
                }
            }
        }
        for (Field f : fields) {
            Method setterMethod = methodMap.get(f.getName());
            if (setterMethod != null) {
                String setFunName = "";
                if (f.isAnnotationPresent(Column.class)) {
                    Column column = f.getAnnotation(Column.class);
                    if (!column.setFuncName().equalsIgnoreCase("setField")) {
                        setFunName = column.setFuncName();
                    } else {
                        setFunName = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                    }
                } else {
                    setFunName = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                }
                for (Method m : clazz.getMethods()) {
                    if (m.getName().equalsIgnoreCase(setFunName)) {
                        setterMethod = m;
                        break;
                    }
                }
                methodMap.put(f.getName(), setterMethod);
            }
        }
        for (Field f : fields) {
            Method setterMethod = methodMap.get(f.getName());
            if (setterMethod == null) {
                throw new Exception("can't find set method fieldL" + f.getName() + " class:" + clazz.getName());
            }
        }
        return methodMap;
    }

    /*
         * 获得字段对应的get方法
         */
    private Map<String, Method> getGetterMethod(Class<?> clazz) throws Exception {
        Map<String, Method> methodMap = new HashMap<>();
        Collection<Field> fields = mapAllDBField.values();
        PropertyDescriptor[] IPropDesc = Introspector.getBeanInfo(clazz, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
        for (Field f : fields) {
            for (PropertyDescriptor propertyDescriptor : IPropDesc) {
                if (propertyDescriptor.getName().equalsIgnoreCase(f.getName())) {
                    Method getMethod = propertyDescriptor.getReadMethod();
                    methodMap.put(f.getName(), getMethod);
                    break;
                }
            }
        }
        for (Field f : fields) {
            Method getterMethod = methodMap.get(f.getName());
            if (getterMethod != null) {
                String getFunName = "";
                if (f.isAnnotationPresent(Column.class)) {
                    Column column = f.getAnnotation(Column.class);
                    if (!column.getFuncName().equalsIgnoreCase("getField")) {
                        getFunName = column.getFuncName();
                    } else {
                        getFunName = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                    }
                } else {
                    getFunName = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                }
                for (Method m : clazz.getMethods()) {
                    if (m.getName().equalsIgnoreCase(getFunName)) {
                        getterMethod = m;
                        break;
                    }
                }
                methodMap.put(f.getName(), getterMethod);
            }
        }
        for (Field f : fields) {
            Method getterMethod = methodMap.get(f.getName());
            if (getterMethod == null) {
                throw new Exception("can't find get method fieldL" + f.getName() + " class:" + clazz.getName());
            }
        }
        return methodMap;
    }

    private ProcedureName getProc(Class<?> clazz) {
        return clazz.getAnnotation(ProcedureName.class);
    }

    /**
     * 获得自增字段
     *
     * @param clazz
     * @return
     */
    private Map<String, Field> getIdentityFields(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(Id.class)) {
                Id id = f.getAnnotation(Id.class);
                if (!id.insertable() && !id.updatable()) {
                    fieldMap.put(f.getName(), f);
                }
            }
        }
        return fieldMap;
    }

    /**
     * 获得重命名表名
     *
     * @param clazz
     * @return
     */
    private Map<String, Field> getTableRenameFields(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(TableRename.class)) {
                fieldMap.put(f.getName(), f);
            }
        }
        return fieldMap;
    }

    /*
        * 获得TableRename字段对应的set方法
        */
    private Map<String, Method> getTableRenameSetterMethod(Class<?> clazz) throws Exception {
        Map<String, Method> methodMap = new HashMap<>();
        Collection<Field> fields = mapAllDBField.values();
        PropertyDescriptor[] IPropDesc = Introspector.getBeanInfo(clazz, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
        for (Field f : fields) {
            for (PropertyDescriptor propertyDescriptor : IPropDesc) {
                if (propertyDescriptor.getName().equalsIgnoreCase(f.getName())) {
                    Method setMethod = propertyDescriptor.getWriteMethod();
                    methodMap.put(f.getName(), setMethod);
                    break;
                }
            }
        }
        for (Field f : fields) {
            Method setterMethod = methodMap.get(f.getName());
            if (setterMethod != null) {
                String setFunName = "";
                if (f.isAnnotationPresent(Column.class)) {
                    Column column = f.getAnnotation(Column.class);
                    if (!column.setFuncName().equalsIgnoreCase("setField")) {
                        setFunName = column.setFuncName();
                    } else {
                        setFunName = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                    }
                } else {
                    setFunName = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                }
                for (Method m : clazz.getMethods()) {
                    if (m.getName().equalsIgnoreCase(setFunName)) {
                        setterMethod = m;
                        break;
                    }
                }
                methodMap.put(f.getName(), setterMethod);
            }
        }
        for (Field f : fields) {
            Method setterMethod = methodMap.get(f.getName());
            if (setterMethod == null) {
                throw new Exception("can't find set method fieldL" + f.getName() + " class:" + clazz.getName());
            }
        }
        return methodMap;
    }

    /**
     * 获得TableRename字段对应的get方法
     * @param clazz
     * @return
     * @throws Exception
     */
    private Map<String, Method> getTableRenameGetterMethod(Class<?> clazz) throws Exception {
        Map<String, Method> methodMap = new HashMap<>();
        Collection<Field> fields = mapAllDBField.values();
        PropertyDescriptor[] IPropDesc = Introspector.getBeanInfo(clazz, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
        for (Field f : fields) {
            for (PropertyDescriptor propertyDescriptor : IPropDesc) {
                if (propertyDescriptor.getName().equalsIgnoreCase(f.getName())) {
                    Method getMethod = propertyDescriptor.getReadMethod();
                    methodMap.put(f.getName(), getMethod);
                    break;
                }
            }
        }
        for (Field f : fields) {
            Method getterMethod = methodMap.get(f.getName());
            if (getterMethod != null) {
                String getFunName = "";
                if (f.isAnnotationPresent(Column.class)) {
                    Column column = f.getAnnotation(Column.class);
                    if (!column.getFuncName().equalsIgnoreCase("getField")) {
                        getFunName = column.getFuncName();
                    } else {
                        getFunName = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                    }
                } else {
                    getFunName = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                }
                for (Method m : clazz.getMethods()) {
                    if (m.getName().equalsIgnoreCase(getFunName)) {
                        getterMethod = m;
                        break;
                    }
                }
                methodMap.put(f.getName(), getterMethod);
            }
        }
        for (Field f : fields) {
            Method getterMethod = methodMap.get(f.getName());
            if (getterMethod == null) {
                throw new Exception("can't find get method fieldL" + f.getName() + " class:" + clazz.getName());
            }
        }
        return methodMap;
    }

    public Map<String, Field> getMapIDField() {
        return mapIDField;
    }

    public void setMapIDField(Map<String, Field> mapIDField) {
        this.mapIDField = mapIDField;
    }

    public Map<String, Field> getMapAllDBField() {
        return mapAllDBField;
    }

    public void setMapAllDBField(Map<String, Field> mapAllDBField) {
        this.mapAllDBField = mapAllDBField;
    }

    public Map<String, Field> getMapInsertableField() {
        return mapInsertableField;
    }

    public void setMapInsertableField(Map<String, Field> mapInsertableField) {
        this.mapInsertableField = mapInsertableField;
    }

    public Map<String, Field> getMapUpdatableField() {
        return mapUpdatableField;
    }

    public void setMapUpdatableField(Map<String, Field> mapUpdatableField) {
        this.mapUpdatableField = mapUpdatableField;
    }

    public Map<String, String> getMapDBColumnName() {
        return mapDBColumnName;
    }

    public void setMapDBColumnName(Map<String, String> mapDBColumnName) {
        this.mapDBColumnName = mapDBColumnName;
    }

    public Map<String, Method> getMapSetMethod() {
        return mapSetMethod;
    }

    public void setMapSetMethod(Map<String, Method> mapSetMethod) {
        this.mapSetMethod = mapSetMethod;
    }

    public Map<String, Method> getMapGetMethod() {
        return mapGetMethod;
    }

    public void setMapGetMethod(Map<String, Method> mapGetMethod) {
        this.mapGetMethod = mapGetMethod;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setProcdure(ProcedureName procdure) {
        this.procdure = procdure;
    }

    public ProcedureName getProcdure() {
        return procdure;
    }

    public void setMapIdentityField(Map<String, Field> mapIdentityField) {
        this.mapIdentityField = mapIdentityField;
    }

    public Map<String, Field> getMapIdentityField() {
        return mapIdentityField;
    }

    public Map<String, Field> getMapTableRenameField() {
        return mapTableRenameField;
    }

    public void setMapTableRenameField(Map<String, Field> mapTableRenameField) {
        this.mapTableRenameField = mapTableRenameField;
    }


    public Map<String, Method> getMapTableRenameSetMethod() {
        return mapTableRenameSetMethod;
    }


    public void setMapTableRenameSetMethod(
            Map<String, Method> mapTableRenameSetMethod) {
        this.mapTableRenameSetMethod = mapTableRenameSetMethod;
    }


    public Map<String, Method> getMapTableRenameGetMethod() {
        return mapTableRenameGetMethod;
    }


    public void setMapTableRenameGetMethod(
            Map<String, Method> mapTableRenameGetMethod) {
        this.mapTableRenameGetMethod = mapTableRenameGetMethod;
    }
}
