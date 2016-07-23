package xyz.dongxiaoxia.commons.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
  
/**
 * 
 * 开发公司：itboy.net<br/>
 * 版权：itboy.net<br/>
 * <p>
 * 
 * JSON工具类
 * 
 * <p>
 * 
 * 区分　责任人　日期　　　　说明<br/>
 * 创建　周柏成　2015年12月25日 　<br/>
 * <p>
 * *******
 * <p>
 * @author zhou-baicheng
 * @email  i@itboy.net
 * @version 1.0,2015年12月25日 <br/>
 * 
 */
public class JSONUtils {  
    /** 
     * 格式化日期 
     */
    private Map<String, Object> jsonMap = new HashMap<String, Object>();  
  
    /** 
     * 清理map 
     * @author zhou-baicheng 
     */  
    public void clear() {  
        jsonMap.clear();  
    }  
  
    /** 
     * 添加元素 <br/> 
     * @author zhou-baicheng 
     * @param key 键 
     * @param value 值 
     * @return Map 
     */  
    public Map<String, Object> put(String key, Object value) {  
        jsonMap.put(key, value);  
        return jsonMap;  
    }  
  
    /** 
     * 判断是否要加引号 
     * return (value instanceof Integer || value instanceof Boolean 
     * || value instanceof Double || value instanceof Float 
     * || value instanceof Short || value instanceof Long || value 
     * instanceof Byte); 
     * @author zhou-baicheng 
     * @param value 
     * @return boolean 
     */  
    private static boolean isNoQuote(Object value) {  
        if (value instanceof Integer) {  
            return true;  
        } else if (value instanceof Boolean) {  
            return true;  
        } else if (value instanceof Double) {  
            return true;  
        } else if (value instanceof Float) {  
            return true;  
        } else if (value instanceof Short) {  
            return true;  
        } else if (value instanceof Long) {  
            return true;  
        } else if (value instanceof Byte) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
  
    /** 
     * 判断是否要加引号 
     * @author zhou-baicheng 
     * @param value 
     * @return boolean 
     */  
    private static boolean isQuote(Object value) {  
        if (value instanceof String) {  
            return true;  
        } else if (value instanceof Character) {  
            return true;  
        } else {  
            return false;  
        }  
  
    }  
  
    /** 
     * 返回形如{'apple':'red','lemon':'yellow'}的字符串 
     * @author zhou-baicheng 
     * @return String 
     * @see java.lang.Object#toString() 
     */  
    @SuppressWarnings("unchecked")  
    @Override  
    public String toString() {  
        StringBuffer sb = new StringBuffer();  
        sb.append("{");  
        Set<Entry<String, Object>> set = jsonMap.entrySet();  
        for (Entry<String, Object> entry : set) {  
            Object value = entry.getValue();  
            if (value == null) {  
                continue;// 对于null值，不进行处理，页面上的js取不到值时也是null  
            }  
            sb.append("'").append(entry.getKey()).append("':");  
            if (value instanceof JSONUtils) {  
                sb.append(value.toString());  
            } else if (isNoQuote(value)) {  
                sb.append(value);  
            } else if (value instanceof Date) {  
                sb.append("'").append(new SimpleDateFormat(
                        "yyyy-MM-dd").format(value)).append("'");
            } else if (isQuote(value)) {  
                sb.append("'").append(value).append("'");  
            } else if (value.getClass().isArray()) {  
                sb.append(arrayToStr(value));  
            } else if (value instanceof Map) {  
                sb.append(fromObject((Map<String, Object>) value).toString());  
            } else if (value instanceof List) {  
                sb.append(listToStr((List<Object>) value));  
            } else {  
                sb.append(fromObject(value).toString());  
            }  
            sb.append(",");  
        }  
        int len = sb.length();  
        if (len > 1) {  
            sb.delete(len - 1, len);  
        }  
        sb.append("}");  
        return sb.toString();  
    }  
  
    /** 
     * 数组拼接 
     * @author zhou-baicheng 
     * @param array 数组 
     * @return String 
     */  
    public static String arrayToStr(Object array) {  
        if (!array.getClass().isArray()) {  
            return "[]";  
        }  
        StringBuffer sb = new StringBuffer();  
        sb.append("[");  
        int len = Array.getLength(array);  
        Object v = null;  
        for (int i = 0; i < len; i++) {  
            v = Array.get(array, i);  
            if (v instanceof Date) {  
                sb.append("'").append(new SimpleDateFormat(
                        "yyyy-MM-dd").format(v)).append("'").append(",");
            } else if (isQuote(v)) {  
                sb.append("'").append(v).append("'").append(",");  
            } else if (isNoQuote(v)) {  
                sb.append(i).append(",");  
            } else {  
                sb.append(fromObject(v)).append(",");  
            }  
        }  
        len = sb.length();  
        if (len > 1) {  
            sb.delete(len - 1, len);  
        }  
        sb.append("]");  
        return sb.toString();  
    }  
  
    /** 
     * list 集合 生成 
     * @author zhou-baicheng 
     * @param list 集合 
     * @return String 
     */  
    @SuppressWarnings("unchecked")  
    public static String listToStr(List<Object> list) {  
        if (list == null) {  
            return null;  
        }  
        StringBuffer sb = new StringBuffer();  
        sb.append("[");  
        Object value = null;  
        for (java.util.Iterator<Object> it = list.iterator(); it.hasNext();) {  
            value = it.next();  
            if (value instanceof Map) {  
                sb.append(fromObject((Map) value).toString()).append(",");  
            } else if (isNoQuote(value)) {  
                sb.append(value).append(",");  
            } else if (isQuote(value)) {  
                sb.append("'").append(value).append("'").append(",");  
            } else {  
                sb.append(fromObject(value).toString()).append(",");  
            }  
        }  
        int len = sb.length();  
        if (len > 1) {  
            sb.delete(len - 1, len);  
        }  
        sb.append("]");  
        return sb.toString();  
    }  
  
    /** 
     * 从一个bean装载数据，返回一个JsonUtil对象。 <br/> 
     * @author zhou-baicheng 
     * @param bean 实体 
     * @return JSONUtils 
     */  
    @SuppressWarnings("unchecked")  
    public static JSONUtils fromObject(Object bean) {  
        JSONUtils json = new JSONUtils();  
        if (bean == null) {  
            return json;  
        }  
        Class cls = bean.getClass();  
        Field[] fs = cls.getDeclaredFields();  
        Object value = null;  
        String fieldName = null;  
        Method method = null;  
        int len = fs.length;  
        for (int i = 0; i < len; i++) {  
            fieldName = fs[i].getName();  
            try {  
                method = cls.getMethod(getGetter(fieldName), (Class[]) null);  
                value = method.invoke(bean, (Object[]) null);  
            } catch (Exception e) {  
                e.printStackTrace();  
                continue;  
            }  
            json.put(fieldName, value);  
        }  
        return json;  
    }  
  
    /** 
     * 从Map中装载数据 <br/> 
     * @author zhou-baicheng 
     * @param map map 
     * @return JSONUtils 
     */  
    public static JSONUtils fromObject(Map<String, Object> map) {  
        JSONUtils json = new JSONUtils();  
        if (map == null) {  
            return json;  
        }  
        json.getMap().putAll(map);  
        return json;  
    }  
  
    private static String getGetter(String property) {  
        return "get" + property.substring(0, 1).toUpperCase()  
                + property.substring(1, property.length());  
    }  
  
    public Map<String, Object> getMap() {  
        return this.jsonMap;  
    }  
  
    /** 
     * 此处为方法说明 
     * @author zhou-baicheng 
     * @param args aa 
     */  
    public static void main(String[] args) { 
        Map<String,Object> map1 = new HashMap<String,Object>(); 
        map1.put("name", "张三"); 
        map1.put("age", 2); 
        Map<String,Object> map2 = new HashMap<String,Object>(); 
        map2.put("name", "张三"); 
        map2.put("age", 22); 
        List<Map<String,Object>> list = new LinkedList<Map<String,Object>>(); 
        list.add(map1); 
        list.add(map2); 
        List<Integer> li = new LinkedList<Integer>(); 
        li.add(1); 
        li.add(1); 
        li.add(1); 
        System.out.println(JSONUtils.fromObject(map1)); 
        JSONUtils util = new JSONUtils(); 
        util.put("name", "张三"); 
        util.put("blog", "http://www.sojson.com/blog/"); 
        util.put("ttt", true); 
        util.put("dd", 1.3d); 
        util.put("ff", 1.3f); 
        util.put("date", new java.util.Date()); 
        int[] a = new int[] { 2, 3, 4, 5 }; 
        util.put("arr", a); 
        System.out.println(util);

        System.out.println(JSONUtils.fromObject(map1).toString());
        System.out.println(map2);
        System.out.println(list);
        System.out.println(li);
        System.out.println(util);
    }
  
}  
