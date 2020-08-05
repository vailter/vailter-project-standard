package com.vailter.standard.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, String> decodeMap(String value) {

        try {
            //json转map<String,String>
            //JavaType jvt = mapper.getTypeFactory().constructParametricType(HashMap.class, String.class, String.class);
            //return mapper.readValue(value, jvt);
            return mapper.readValue(value, HashMap.class);
        } catch (Exception e) {
            return null;
        }
    }
}
