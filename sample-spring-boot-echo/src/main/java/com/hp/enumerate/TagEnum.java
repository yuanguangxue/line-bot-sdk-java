package com.hp.enumerate;

import com.google.common.annotations.VisibleForTesting;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public enum TagEnum {
    technology("技术"),
    communication("沟通"),
    team("团队"),
    document("文档"),
    other("其他");

    String description;

    public String getDescription() {
        return description;
    }

    TagEnum(String description) {
        this.description = description;
    }

    /**
     *
     * @param jsonArray
     * @param tagEnum
     */
    @VisibleForTesting
    static void pushJsonObject(JSONArray jsonArray, TagEnum tagEnum){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value",tagEnum.name());
        jsonObject.put("text",tagEnum.getDescription());
        jsonArray.put(jsonObject);
    }

    /**
     *
     * @return
     */
    public static Map<String,TagEnum> toMap(){
        final Map<String,TagEnum> map = new HashMap<>();
        Arrays.stream(TagEnum.values()).forEach(tagEnum -> {
            map.put(tagEnum.name(),tagEnum);
        });
        return map;
    }

    /**
     *
     * @return
     */
    @SuppressWarnings({"rawtypes","unckeck"})
    public static JSONArray toJsonArray(){
        final JSONArray jsonArray = new JSONArray();
        Arrays.stream(TagEnum.values())
               .sorted(Comparator.comparing(TagEnum::ordinal))
               .collect(Collectors.toList())
               .forEach(tagEnum -> pushJsonObject(jsonArray,tagEnum));
        return jsonArray;
    }

    public static String toJsonString(){
        return toJsonArray().toString();
    }
}
