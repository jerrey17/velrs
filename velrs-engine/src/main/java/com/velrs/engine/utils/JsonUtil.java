package com.velrs.engine.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class JsonUtil {


    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    private static final ObjectWriter PRETTY_OBJECT_WRITER = OBJECT_MAPPER.writerWithDefaultPrettyPrinter();

    public static String toJsonString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toJsonBytes(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJsonStringPretty(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return PRETTY_OBJECT_WRITER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toJsonBytesPretty(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return PRETTY_OBJECT_WRITER.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(byte[] json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(byte[] json, JavaType javaType) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String json, JavaType javaType) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map parse(byte[] json) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map parse(String json) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, getListJavaType(clazz));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseArray(byte[] json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, getListJavaType(clazz));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static JavaType getListJavaType(Class clazz) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
    }

    public static TypeFactory getTypeFactory() {
        return OBJECT_MAPPER.getTypeFactory();
    }

    public static class CommonType {
        public static final JavaType MAP_STRING_STRING = getTypeFactory().constructParametricType(Map.class, String.class, String.class);
    }

    public static <T> T parsetParametricType(String json, TypeReference<T> typeReference) {
        if (json == null) {
            return null;
        }
        JavaType javaType = getTypeFactory().constructType(typeReference.getType());
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
