package com.coffee.roastnbrew.utils;

import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.json.*;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Slf4j
public class JSONUtils {

    public static final String EMPTY_JSON_LIST_STRING = "[]";
    public static final String EMPTY_JSON_OBJECT_STRING = "{}";
    public static final Gson gson = new Gson();

    public static final ObjectMapper objectMapper;

    public static final ObjectMapper objectMapperTransformed;

    static {
        SimpleModule jsonTransformModule = new SimpleModule();
        objectMapperTransformed = new ObjectMapper()
                .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
                .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .registerModule(jsonTransformModule)
        ;
        objectMapper = new ObjectMapper()
                .setSerializationInclusion(Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
                .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
                .registerModule(jsonTransformModule)
                .registerModule(new JavaTimeModule());
    }

    private JSONUtils() {
    }

    public static Map<String, Object> toMap(JsonObject json, boolean parseIntegersAsLong) {
        if (null == json) {
            return new LinkedHashMap<>();
        }
        // Using linked hash-map to maintain order
        Map<String, Object> map = new LinkedHashMap<>();

        for (String key : json.keySet()) {
            JsonValue value = json.get(key);
            Object val = getPrimitiveValue(value, parseIntegersAsLong);
            map.put(key, val);
        }
        return map;
    }

    public static Map<String, Object> toMap(JsonObject json) {
        return toMap(json, false);
    }

    public static List<Object> toList(JsonArray array, boolean parseIntegersAsLong) {
        List<Object> list = new ArrayList<>();
        for (JsonValue value : array) {
            Object val = getPrimitiveValue(value, parseIntegersAsLong);
            list.add(val);
        }

        return list;
    }

    public static List<Object> toList(JsonArray array) {
        return toList(array, false);
    }

    public static Object getPrimitiveValue(JsonValue value, boolean parseIntegersAsLong) {
        Object primitive = null;
        JsonValue.ValueType type = value.getValueType();
        switch (type) {
            case ARRAY:
                primitive = toList((JsonArray) value, parseIntegersAsLong);
                break;
            case OBJECT:
                primitive = toMap((JsonObject) value, parseIntegersAsLong);
                break;
            case STRING:
                primitive = ((JsonString) value).getString();
                break;
            case TRUE:
                primitive = true;
                break;
            case FALSE:
                primitive = false;
                break;
            case NUMBER:
                if (((JsonNumber) value).isIntegral()) {
                    if (parseIntegersAsLong) {
                        primitive = ((JsonNumber) value).longValue();
                    } else {
                        try {
                            primitive = ((JsonNumber) value).intValueExact();
                        } catch (ArithmeticException e) {
                            primitive = ((JsonNumber) value).longValue();
                        }
                    }
                } else {
                    primitive = ((JsonNumber) value).doubleValue();
                }
                break;
            case NULL:
                break;
        }

        return primitive;
    }

    public static <T> T jsonStringToType(String jsonString, TypeReference<T> typeReference) throws IOException {
        if (jsonString == null) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, typeReference);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }


    public static <T> T jsonStringToType(String jsonString, Class<T> type) throws IOException {
        if (StringUtils.isNullOrEmpty(jsonString)) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, type);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public static <T> T jsonNodeToType(JsonNode jsonNode, Class<T> type) throws JsonProcessingException {
        return objectMapper.treeToValue(jsonNode, type);

    }

    public static Map<String, Object> jsonStringToMap(String jsonString) throws IOException {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public static <T extends List> T jsonStringToList(
            final String jsonString, final TypeReference<T> typeReference) throws IOException {

        return jsonStringToCollection(jsonString, typeReference);
    }

    public static <T> List<T> jsonStringToList(
            final String jsonString) throws IOException {

        return jsonStringToCollection(jsonString, new TypeReference<List<T>>() {
        });
    }

    public static <T> Set<T> jsonStringToSet(
            final String jsonString) throws IOException {

        return jsonStringToCollection(jsonString, new TypeReference<Set<T>>() {
        });
    }

    public static <T> T jsonStringToCollection(
            final String jsonString, final TypeReference<T> typeReference) throws IOException {
        return fromJson(jsonString, typeReference);
    }

    public static <T> T fromJson(final String jsonString,
                                 final TypeReference<T> typeReference) throws IOException {
        try {
            return objectMapper.readValue(jsonString, typeReference);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public static <T> T fromJsonObject(final JsonObject jsonObject, final TypeReference<T> typeReference) throws IOException {
        return fromJson(jsonObject.toString(), typeReference);
    }

    public static JsonObject mapToJson(Map<?, ?> map) throws JsonProcessingException {
        return objToJson(map);
    }

    public static JsonObject objToJson(Object obj) throws JsonProcessingException {
        return jsonStringToJsonObj(objectMapper.writeValueAsString(obj));
    }

    public static JsonArray listToJson(List<?> list) throws JsonProcessingException {
        return jsonStringToJsonArray(objectMapper.writeValueAsString(list));
    }

    public static String listToJsonString(List<?> list) throws JsonProcessingException {
        return objectMapper.writeValueAsString(list);
    }

    public static JsonArray jsonStringToJsonArray(String jsonString) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            return jsonReader.readArray();
        }
    }

    public static <T> T convertValue(Object value, Class<T> toValueType) {
        return objectMapper.convertValue(value, toValueType);
    }

    public static <T> T convertValue(Object value, Class<T> toValueType, ObjectMapper customObjectMapper) {
        return customObjectMapper.convertValue(value, toValueType);
    }

    public static boolean isNotNull(JsonObject json, String key) {
        return json.get(key) != null && !json.isNull(key);
    }

    public static boolean isJsonValid(String jsonInString) {
        try {
            objectMapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static JsonNode readTree(String value) throws IOException {
        return objectMapper.readTree(value);
    }

    public static JsonNode readTree(JsonParser parser) throws IOException {
        return objectMapper.readTree(parser);
    }

    public static JsonNode readTree(byte[] value) throws IOException {
        return objectMapper.readTree(value);
    }

    public static String mapToJsonString(Map<String, ?> map) throws IOException {
        return mapToJsonString(map, false);
    }

    public static ObjectNode objectToJsonNode(Object object) {
        return objectMapper.convertValue(object, ObjectNode.class);

    }


    public static String mapToJsonString(Map<String, ?> map, boolean pretty)
            throws IOException {
        try {
            if (pretty) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            } else {
                return objectMapper.writeValueAsString(map);
            }
        } catch (JsonProcessingException e) {
            throw new IOException(e);
        }
    }

    public static String objectToJsonString(Object object) throws CoffeeException {
        return objectToJsonString(object, false);
    }

    public static byte[] objectToByteArray(Object object) throws CoffeeException {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new CoffeeException("Object serialization failed " + e.getMessage());
        }
    }

    public static <T> T byteArrayToObject(byte[] value, Class<T> tClass) throws IOException {
        return objectMapper.readValue(value, tClass);
    }

    public static <T> T byteArrayToTypeReference(byte[] value, TypeReference<T> tClass) throws IOException {
        return objectMapper.readValue(value, tClass);
    }

    public static String objectToJsonString(Object object, boolean pretty) throws CoffeeException {
        try {
            if (pretty) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            } else {
                return objectMapper.writeValueAsString(object);
            }
        } catch (JsonProcessingException e) {
            throw new CoffeeException("object can not be json serialized");
        }
    }

    public static void addNonNull(JsonObjectBuilder jsonObjectBuilder, String key, Object value) {
        if (value != null) {
            if (value instanceof Long) {
                jsonObjectBuilder.add(key, (Long) value);
            } else if (value instanceof String) {
                jsonObjectBuilder.add(key, (String) value);
            } else if (value instanceof JsonValue) {
                jsonObjectBuilder.add(key, (JsonValue) value);
            } else if (value instanceof BigInteger) {
                jsonObjectBuilder.add(key, (BigInteger) value);
            } else if (value instanceof BigDecimal) {
                jsonObjectBuilder.add(key, (BigDecimal) value);
            } else if (value instanceof Integer) {
                jsonObjectBuilder.add(key, (Integer) value);
            } else if (value instanceof Double) {
                jsonObjectBuilder.add(key, (Double) value);
            } else if (value instanceof Boolean) {
                jsonObjectBuilder.add(key, (Boolean) value);
            }
        }
    }

    public static JsonObject jsonStringToJsonObj(String jsonString) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            return jsonReader.readObject();
        }
    }

    public static JsonNode jsonStringtoJsonNode(String jsonString) throws IOException {
        return objectMapper.readTree(jsonString);
    }

    public static JsonValue jsonStringToJsonValue(String jsonString) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            return jsonReader.read();
        }
    }

    public static JsonStructure jsonStringToJsonStructure(String jsonString) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            return jsonReader.read();
        }
    }

    public static JsonValue getProperty(JsonObject json, String key, JsonValue defaultValue,
                                        boolean throwErrorIfNotPresent) throws CoffeeException {
        return getProperty(json, key, defaultValue, throwErrorIfNotPresent, CoffeeException.class);
    }

    public static JsonValue getProperty(JsonObject json, String key, JsonValue defaultValue,
                                        boolean throwErrorIfNotPresent, Class<? extends CoffeeException> exceptionType)
            throws CoffeeException {

        if (json == null) {
            throw getExceptionWithMessage(exceptionType, " Json cannot be null. ");
        }
        if (StringUtils.isNullOrEmpty(key)) {
            throw getExceptionWithMessage(exceptionType, " Key cannot be null. ");
        }
        if (!json.containsKey(key) || json.isNull(key)) {
            if (throwErrorIfNotPresent) {
                throw getExceptionWithMessage(exceptionType,
                        String.format(" %s must not be blank. ", key));
            } else {
                return defaultValue;
            }
        }
        return json.get(key);
    }

    public static String getString(JsonObject json, String key, JsonValue defaultValue, boolean throwErrorIfNotPresent)
            throws CoffeeException {
        return ((JsonString) JSONUtils.getProperty(json, key, defaultValue, throwErrorIfNotPresent)).getString();
    }

    private static CoffeeException getExceptionWithMessage(
            Class<? extends CoffeeException> exceptionType, String message) throws CoffeeException {
        try {
            return exceptionType.getDeclaredConstructor(String.class).newInstance(message);
        } catch (Exception e) {
            throw new CoffeeException(message);
        }
    }

    public static JsonObject addKeyValue(JsonObject source, String key, String value) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(key, value);
        source.forEach(builder::add);
        return builder.build();
    }

    public static JsonObject addKeyValue(JsonObject source, String key, boolean value) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(key, value);
        source.forEach(builder::add);
        return builder.build();
    }

    public static Map<String, Object> toMap(Object object) throws JsonProcessingException {
        if (object instanceof String) {
            return toMap(jsonStringToJsonObj((String) object));
        }
        return toMap(objToJson(object));
    }

    public static String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static String toTransformedJson(Object obj) throws JsonProcessingException {
        return objectMapperTransformed.writeValueAsString(obj);
    }

    public static <T> T fromJson(String jsonInString, Class<T> type) throws IOException {
        return objectMapper.readValue(jsonInString, type);
    }

    public static <T> T fromMap(Map<String, Object> params, Class<T> type) throws IOException {
        return objectMapper.readValue(objectMapper.writeValueAsString(params), type);
    }

    public static JsonObject renameJsonObjectField(JsonObject jsonObject, String oldName, String newName) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        jsonObject.forEach((k, v) -> {
            String finalName = k.equals(oldName) ? newName : k;
            objectBuilder.add(finalName, v);
        });
        return objectBuilder.build();
    }

    /**
     * Checks if JSON value as string in DB is a null object
     */
    public static boolean isJsonStrNull(Object value) {
        return value == null || value instanceof String &&
                (((String) value).isEmpty() || ((String) value).equalsIgnoreCase("null"));
    }

    public static <T extends Enum<T>> T getEnum(String enumStr, Class<T> enumType) {
        return optEnum(enumStr, enumType, null);
    }

    public static <T extends Enum<T>> T optEnum(String enumStr, Class<T> enumType, T defaultEnum) {
        if (enumStr != null) {
            return gson.fromJson(enumStr, enumType);
        }
        return defaultEnum;
    }

    public static boolean isNotBlank(JsonArray jsonArray) {
        return !isBlank(jsonArray);
    }

    public static boolean isBlank(JsonArray jsonArray) {
        return null == jsonArray || jsonArray.isEmpty();
    }

}
