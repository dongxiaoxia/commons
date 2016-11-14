package xyz.dongxiaoxia.json;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by 01don on 2016/11/14.
 */
public class JsonParserMapperTool {
    private static JsonParserMapperTool jsonParserMapperHolder = null;
    private ObjectMapper mapper;

    static {
        jsonParserMapperHolder = new JsonParserMapperTool();
        jsonParserMapperHolder.mapper = jsonParserMapperHolder.initMapper();
    }

    public static ObjectMapper getMapper(){
        return jsonParserMapperHolder.mapper;
    }

    private ObjectMapper initMapper(){
        if (mapper!=null){
            return mapper;
        }else{
            synchronized (JsonParserMapperTool.class){
                if (mapper == null){
                    mapper = ObjectMapperTool.initObjectMapper();
                }
                return mapper;
            }
        }
    }
}
