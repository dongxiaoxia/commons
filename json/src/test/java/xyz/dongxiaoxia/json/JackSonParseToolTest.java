package xyz.dongxiaoxia.json;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 01don on 2016/11/14.
 */
public class JackSonParseToolTest {
    private Map<String,Object> map ;

    @Before
    public void setUp() throws Exception {
        map = new HashMap<>();
        map.put("aaa","aaa");
        map.put("bbb", Arrays.asList("aaa","safaf"));
    }

    @Test
    public void parse2java() throws Exception {
        Map map1 = JackSonParseTool.parse2java(JackSonParseTool.parse2Json(map),HashMap.class);
        map1.keySet().forEach(key -> {
            System.out.println(key+" : " + map1.get(key));
        });
    }

    @Test
    public void parse2HashMap() throws Exception {
        Map map1 = JackSonParseTool.parse2HashMap(JackSonParseTool.parse2Json(map),String.class,Object.class);
        map1.keySet().forEach(key -> {
            System.out.println(key+" : " + map1.get(key));
        });
    }

    @Test
    public void parse2java1() throws Exception {
    }

    @Test
    public void parse2java2() throws Exception {

    }

    @Test
    public void parse2Json() throws Exception {
        System.out.println(JackSonParseTool.parse2Json(map));
    }

    @Test
    public void parse2OutputStream() throws Exception {

    }

    @Test
    public void parse2ByteArray() throws Exception {

    }

}