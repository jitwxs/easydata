package com.github.jitwxs.easydata.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.StringValue;
import com.google.protobuf.util.JsonFormat;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Protobuf serialization and deserialization
 *
 * @author jitwxs
 * @since 2021-08-22 18:30
 */
public class ProtobufUtils {
    private static final JsonFormat.Printer printer;
    private static final JsonFormat.Parser parser;

    static {
        JsonFormat.TypeRegistry registry = JsonFormat.TypeRegistry.newBuilder()
                .add(StringValue.getDescriptor())
                .build();
        printer = JsonFormat
                .printer()
                .usingTypeRegistry(registry)
                .includingDefaultValueFields()
                .omittingInsignificantWhitespace();

        parser = JsonFormat
                .parser()
                .usingTypeRegistry(registry);
    }

    /**
     * convert protobuf message to json string
     *
     * @param message protobuf message
     * @return json string
     */
    public static String toJson(Message message) {
        if (message == null) {
            return "";
        }

        try {
            return printer.print(message);
        } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * convert protobuf message map to json string
     *
     * @param messageMap (any key, protobuf message)
     * @return json string
     */
    public static String toJson(Map<?, ? extends Message> messageMap) {
        if (messageMap == null) {
            return "";
        }
        if (messageMap.isEmpty()) {
            return "{}";
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        messageMap.forEach((k, v) -> {
            sb.append("\"").append(JSON.toJSONString(k)).append("\":").append(toJson(v)).append(",");
        });
        sb.deleteCharAt(sb.length() - 1).append("}");
        return sb.toString();
    }

    /**
     * convert protobuf message list to json string
     *
     * @param messageList protobuf message list
     * @return json string
     */
    public static String toJson(List<? extends MessageOrBuilder> messageList) {
        if (messageList == null) {
            return "";
        }
        if (messageList.isEmpty()) {
            return "[]";
        }

        try {
            StringBuilder builder = new StringBuilder(1024);
            builder.append("[");
            for (MessageOrBuilder message : messageList) {
                printer.appendTo(message, builder);
                builder.append(",");
            }
            return builder.deleteCharAt(builder.length() - 1).append("]").toString();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * deserialization json to protobuf message
     *
     * @param json   {@link #toJson(Message)} 得到
     * @param target protobuf message class
     * @param <T>    protobuf message generic
     * @return protobuf message instance
     */
    public static <T> T toBean(String json, Class<T> target) {
        if (StringUtils.isBlank(json)) {
            return null;
        }

        try {
            final Method method = target.getMethod("newBuilder");
            final Message.Builder builder = (Message.Builder) method.invoke(null);

            parser.merge(json, builder);

            return (T) builder.build();
        } catch (Exception e) {
            throw new RuntimeException("ProtobufUtil toMessage happen error, class: " + target + ", json: " + json, e);
        }
    }

    /**
     * deserialization Message List
     *
     * @param json   {@link #toJson(List)} 得到
     * @param target protobuf message class
     * @param <T>    protobuf message generic
     * @return protobuf message instance list
     */
    public static <T extends Message> List<T> toBeanList(String json, Class<T> target) {
        if (StringUtils.isBlank(json)) {
            return Collections.emptyList();
        }

        final JSONArray jsonArray = JSON.parseArray(json);

        final List<T> resultList = new ArrayList<>(jsonArray.size());

        for (int i = 0; i < jsonArray.size(); i++) {
            resultList.add(toBean(jsonArray.getString(i), target));
        }

        return resultList;
    }

    /**
     * deserialization Message Map
     *
     * @param json       {@link #toJson(Map)} 得到
     * @param keyClass   result map key class type
     * @param <K>        result map key class generic
     * @param valueClass result map value class type
     * @param <V>        result map value class generic
     * @return protobuf message instance map
     */
    public static <K, V extends Message> Map<K, V> toBeanMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (StringUtils.isBlank(json)) {
            return Collections.emptyMap();
        }

        final JSONObject jsonObject = JSON.parseObject(json);

        final Map<K, V> map = Maps.newHashMapWithExpectedSize(jsonObject.size());
        for (String key : jsonObject.keySet()) {
            final K k = JSONObject.parseObject(key, keyClass);
            final V v = toBean(jsonObject.getString(key), valueClass);

            map.put(k, v);
        }

        return map;
    }
}
